package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.entity.ProductPrice;

public interface ProductPriceService {

    ProductPrice create(ProductPrice productPrice);
    ProductPrice getById(String id);
    ProductPrice findProductPriceActive(String productId, Boolean active);
}
