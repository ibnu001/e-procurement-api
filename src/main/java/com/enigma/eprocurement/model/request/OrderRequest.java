package com.enigma.eprocurement.model.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderRequest {

    private String vendorId;
    private List<OrderDetailRequest> orderDetails;
}
