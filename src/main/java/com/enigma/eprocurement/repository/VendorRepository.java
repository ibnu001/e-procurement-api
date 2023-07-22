package com.enigma.eprocurement.repository;

import com.enigma.eprocurement.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, String> {
}
