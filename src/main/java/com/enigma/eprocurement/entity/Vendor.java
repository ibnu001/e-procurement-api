package com.enigma.eprocurement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "m_vendor")
public class Vendor {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @OneToOne(targetEntity = Address.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

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


}