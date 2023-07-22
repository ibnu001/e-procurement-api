package com.enigma.eprocurement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "m_product")
public class Product {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @GenericGenerator(strategy = "com.enigma.entity.CustomIdGenerator", name = "custom-id")
    @GeneratedValue(generator = "custom-id")
    @Column(name = "product_code")
    private String productCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductPrice> productPrices;

    private String createBy;

    private Long createAt;

    private Long updateAt;

    @PrePersist
    private void onPersist() {
        if (createAt == null) createAt = System.currentTimeMillis();
    }

    @PreUpdate
    private void onUpdate() {
        if (updateAt == null) updateAt = System.currentTimeMillis();
    }

    public List<ProductPrice> getProductPrices() {
        return Collections.unmodifiableList(productPrices);
    }

    public void addProductPrice(ProductPrice productPrice) {
        productPrices.add(productPrice);
    }
}
