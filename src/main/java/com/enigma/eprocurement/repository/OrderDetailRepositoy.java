package com.enigma.eprocurement.repository;

import com.enigma.eprocurement.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepositoy extends JpaRepository<OrderDetail, String> {
}
