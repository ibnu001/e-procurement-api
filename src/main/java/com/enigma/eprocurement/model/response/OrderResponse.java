package com.enigma.eprocurement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.LocalDateTimeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderResponse {

    private String orderId;
    private String orderDate;
    private VendorResponse vendor;
    private List<OrderDetailResponse> orderDetails;
    private Long grandTotal;
}
