package com.enigma.eprocurement.specification;

import com.enigma.eprocurement.entity.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<Order> getSpecification(String dateNow, Integer month) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            LocalDate date = LocalDate.now();

            if (dateNow.equalsIgnoreCase("now")) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder
                        .function("DATE", LocalDate.class, root.get("orderDate")), date));
            }

            if (month != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder
                        .function("MONTH", Integer.class, root.get("orderDate")), month));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
