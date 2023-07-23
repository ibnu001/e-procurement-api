package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.OrderService;
import com.enigma.eprocurement.Service.ProductPriceService;
import com.enigma.eprocurement.Service.VendorService;
import com.enigma.eprocurement.entity.Order;
import com.enigma.eprocurement.entity.OrderDetail;
import com.enigma.eprocurement.entity.ProductPrice;
import com.enigma.eprocurement.entity.Vendor;
import com.enigma.eprocurement.model.request.OrderRequest;
import com.enigma.eprocurement.model.response.*;
import com.enigma.eprocurement.repository.OrderRepository;
import com.enigma.eprocurement.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final VendorService vendorService;

    private final ProductPriceService productPriceService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse createNewOrder(OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Vendor vendor = vendorService.getById(request.getVendorId());

        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(orderDetailRequest -> {
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());

            return OrderDetail.builder()
                    .productPrice(productPrice)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        Order order = Order.builder()
                .vendor(vendor)
                .createdBy(authentication.getName())
                .orderDetails(orderDetails)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.saveAndFlush(order);

        List<OrderDetailResponse> orderDetailResponses = getOrderDetailResponses(order);

        VendorResponse vendorResponse = getVendorResponse(vendor);

        return getOrderResponse(order, orderDetailResponses, vendorResponse);
    }


    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));

        List<OrderDetailResponse> orderDetailResponses = getOrderDetailResponses(order);

        Vendor vendor = order.getVendor();

        VendorResponse vendorResponse = getVendorResponse(vendor);

        return getOrderResponse(order, orderDetailResponses, vendorResponse);
    }

    @Override
    public Page<OrderResponse> getAllOrder(String date, Integer month, Integer page, Integer size) {

        Specification<Order> specification = OrderSpecification.getSpecification(date, month);

        Pageable pageable = PageRequest.of(page, size);

        Page<Order> orders = orderRepository.findAll(specification, pageable);

        List<OrderResponse> orderResponses = orders.stream().map(order -> {
            List<OrderDetailResponse> orderDetailResponses = getOrderDetailResponses(order);

            Vendor vendor = order.getVendor();

            VendorResponse vendorResponse = getVendorResponse(vendor);

            return getOrderResponse(order, orderDetailResponses, vendorResponse);
        }).collect(Collectors.toList());


        return new PageImpl<>(orderResponses, pageable, orders.getTotalElements());
    }

    @Override
    public void writeReportToCsv(Writer writer, String date, Integer month) {
        Specification<Order> specification = OrderSpecification.getSpecification(date, month);
        List<Order> orders = orderRepository.findAll(specification);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            List<CsvResponse> csvResponses = orders.stream()
                    .flatMap(order -> order.getOrderDetails().stream()
                            .map(orderDetail -> CsvResponse.builder()
                                    .productCode(orderDetail.getProductPrice().getProduct().getProductCode())
                                    .orderDate(orderDetail.getOrder().getOrderDate().format(formatter))
                                    .vendor(orderDetail.getProductPrice().getVendor().getName())
                                    .productName(orderDetail.getProductPrice().getProduct().getName())
                                    .category(orderDetail.getProductPrice().getProduct().getCategory().getName())
                                    .price(orderDetail.getProductPrice().getPrice())
                                    .quantity(orderDetail.getQuantity())
                                    .grandTotal(orderDetail.getProductPrice().getPrice() * orderDetail.getQuantity())
                                    .build())
                    ).collect(Collectors.toList());

            csvPrinter.printRecord("KODE BARANG", "TANGGAL", "NAMA VENDOR", "NAMA BARANG", "KATEGORI", "HARGA BARANG", "QTY", "JUMLAH");

            for (CsvResponse response : csvResponses) {
                csvPrinter.printRecord(
                        response.getProductCode(),
                        response.getOrderDate(),
                        response.getVendor(),
                        response.getProductName(),
                        response.getCategory(),
                        response.getPrice(),
                        response.getQuantity(),
                        response.getGrandTotal()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static OrderResponse getOrderResponse(Order order, List<OrderDetailResponse> orderDetailResponses, VendorResponse vendorResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedOrderDate = order.getOrderDate().format(formatter);
        Optional<Long> getGrandTotal = order.getOrderDetails().stream().map(orderDetail -> orderDetail.getQuantity() * orderDetail.getProductPrice().getPrice()).findFirst();
        Long grandTotal = 0L;
        if (getGrandTotal.isPresent()) grandTotal = getGrandTotal.get();
        return OrderResponse.builder()
                .orderId(order.getId())
                .vendor(vendorResponse)
                .grandTotal(grandTotal)
                .orderDate(formattedOrderDate)
                .orderDetails(orderDetailResponses)
                .build();
    }

    private static VendorResponse getVendorResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .address(vendor.getAddress())
                .mobilePhone(vendor.getMobilePhone())
                .build();
    }

    private static List<OrderDetailResponse> getOrderDetailResponses(Order order) {
        return order.getOrderDetails().stream().map(orderDetail -> {
            orderDetail.setOrder(order);

            ProductPrice productPrice = orderDetail.getProductPrice();
            productPrice.setStock(productPrice.getStock() - orderDetail.getQuantity());

            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    .product(ProductResponse.builder()
                            .id(productPrice.getProduct().getId())
                            .productCode(productPrice.getProduct().getProductCode())
                            .productName(productPrice.getProduct().getName())
                            .description(productPrice.getProduct().getDescription())
                            .price(productPrice.getPrice())
                            .stock(productPrice.getStock())
                            .category(productPrice.getProduct().getCategory().getName())
                            .vendor(VendorResponse.builder()
                                    .id(productPrice.getVendor().getId())
                                    .name(productPrice.getVendor().getName())
                                    .address(productPrice.getVendor().getAddress())
                                    .mobilePhone(productPrice.getVendor().getMobilePhone())
                                    .build())
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }
}
