package com.enigma.eprocurement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "m_admin")
public class Admin {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

    @Column(name = "created_at")
    private Long createdAt;

    @PrePersist
    private void onPersist() {
        if (createdAt == null) createdAt = System.currentTimeMillis();
    }
}
