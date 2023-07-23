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

    @Column(name = "mobile_phone", unique = true)
    private String mobilePhone;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    private void onPersist() {
        if (createdAt == null) createdAt = System.currentTimeMillis();
    }

    @PreUpdate
    private void onUpdate() {
        if (updatedAt == null) updatedAt = System.currentTimeMillis();
    }

}
