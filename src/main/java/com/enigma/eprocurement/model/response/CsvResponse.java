package com.enigma.eprocurement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CsvResponse {

    String productCode;
    String orderDate;
    String productName;
    String vendor;
    String category;
    Long price;
    Integer quantity;
    Long grandTotal;
}
