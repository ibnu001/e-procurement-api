package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.model.request.ProductRequest;
import com.enigma.eprocurement.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);
    List<ProductResponse> createBulk(List<ProductRequest> products);
    ProductResponse getById(String id);
    Page<ProductResponse> getAllByNameOrPrice(String name, Long price, Integer page, Integer size);
    ProductResponse update(ProductRequest request);
    void hardDeleteById(String id);
    void softDeleteById(String id);

}
