package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.ProductPriceService;
import com.enigma.eprocurement.entity.ProductPrice;
import com.enigma.eprocurement.repository.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductPrice create(ProductPrice productPrice) {
        return productPriceRepository.save(productPrice);
    }

    @Override
    public ProductPrice getById(String id) {
        return productPriceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product price not found"));
    }

    @Override
    public ProductPrice findProductPriceActive(String productId, Boolean active) {
        return productPriceRepository.findByProduct_IdAndIsActive(productId, active).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }
}
