package com.enigma.eprocurement.specification;

import com.enigma.eprocurement.entity.Product;
import com.enigma.eprocurement.entity.ProductPrice;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> getSpecification(String name, Long price) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductPrice> productPrices = root.join("productPrices");
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (price != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productPrices.get("price"), price));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
