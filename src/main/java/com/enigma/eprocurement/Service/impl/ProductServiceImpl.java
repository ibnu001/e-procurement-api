package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.CategoryService;
import com.enigma.eprocurement.Service.ProductPriceService;
import com.enigma.eprocurement.Service.ProductService;
import com.enigma.eprocurement.Service.VendorService;
import com.enigma.eprocurement.entity.Product;
import com.enigma.eprocurement.entity.ProductPrice;
import com.enigma.eprocurement.entity.Vendor;
import com.enigma.eprocurement.model.request.ProductRequest;
import com.enigma.eprocurement.model.response.ProductResponse;
import com.enigma.eprocurement.model.response.VendorResponse;
import com.enigma.eprocurement.repository.ProductRepository;
import com.enigma.eprocurement.specification.ProductSpecification;
import com.enigma.eprocurement.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductPriceService productPriceService;

    private final ValidationUtil validationUtil;

    private final VendorService vendorService;

    private final CategoryService categoryService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse create(ProductRequest request) {
        validationUtil.validate(request);
        Vendor vendor = vendorService.getById(request.getVendorId());

        Product product = Product.builder()
                .productCode(generateProductCode())
                .name(request.getProductName())
                .description(request.getDescription())
                .category(categoryService.getOrSave(request.getCategory()))
                .isDelete(false)
                .build();
        productRepository.saveAndFlush(product);

        ProductPrice productPrice = ProductPrice.builder()
                .price(request.getPrice())
                .stock(request.getStock())
                .vendor(vendor)
                .product(product)
                .isActive(true)
                .build();
        productPriceService.create(productPrice);

        return toProductResponse(product, productPrice, vendor);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<ProductResponse> createBulk(List<ProductRequest> products) {
        return products.stream().map(this::create).collect(Collectors.toList());
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = findByIdOrThrowNotFound(id);
        Optional<ProductPrice> productPrice = product.getProductPrices().stream()
                .filter(ProductPrice::getIsActive).findFirst();

        if (productPrice.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        Vendor vendor = productPrice.get().getVendor();

        return toProductResponse(product, productPrice.get(), vendor);
    }

    @Override
    public Page<ProductResponse> getAllByNameOrPrice(String name, Long price, Integer page, Integer size) {
        Specification<Product> specification = ProductSpecification.getSpecification(name, price);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(specification, pageable);

        Set<String> processedProductIds = new HashSet<>();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products.getContent()) {
            if (processedProductIds.contains(product.getId())) continue;


            Optional<ProductPrice> productPrice = product.getProductPrices().stream()
                    .filter(ppActive -> ppActive.getIsActive().equals(true)).findFirst();

            if (productPrice.isEmpty()) continue;

            Vendor vendor = productPrice.get().getVendor();

            productResponses.add(toProductResponse(product, productPrice.get(), vendor));
            processedProductIds.add(product.getId());
        }

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse update(ProductRequest request) {
        Product product = findByIdOrThrowNotFound(request.getProductId());
        product.setName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setCategory(categoryService.getOrSave(request.getCategory()));

        ProductPrice productPriceActive = productPriceService.findProductPriceActive(request.getProductId(), true);

        if (!productPriceActive.getVendor().getId().equals(request.getVendorId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "vendor cannot be changed");

        if (!request.getPrice().equals(productPriceActive.getPrice())) {
            productPriceActive.setIsActive(false);
            ProductPrice productPrice = productPriceService.create(ProductPrice.builder()
                    .price(request.getPrice())
                    .stock(request.getStock())
                    .product(product)
                    .vendor(productPriceActive.getVendor())
                    .isActive(true)
                    .build());
            product.addProductPrice(productPrice);
            return toProductResponse(product, productPrice, productPrice.getVendor());
        }

        productPriceActive.setStock(request.getStock());

        return toProductResponse(product, productPriceActive, productPriceActive.getVendor());
    }

    @Override
    public void hardDeleteById(String id) {
        Product product = findByIdOrThrowNotFound(id);
        productRepository.delete(product);
    }

    @Override
    public void softDeleteById(String id) {
        Product product = findByIdOrThrowNotFound(id);
        product.setIsDelete(true);
        productRepository.save(product);
    }

    private Product findByIdOrThrowNotFound(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    private static ProductResponse toProductResponse(Product product, ProductPrice productPrice, Vendor vendor) {
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getName())
                .description(product.getDescription())
                .category(product.getCategory().getName())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .vendor(VendorResponse.builder()
                        .id(vendor.getId())
                        .name(vendor.getName())
                        .mobilePhone(vendor.getMobilePhone())
                        .address(vendor.getAddress())
                        .build())
                .build();
    }

    private String generateProductCode() {
        Integer size = productRepository.findAll().size() + 1;
        return String.format("KB%02d", size);
    }
}
