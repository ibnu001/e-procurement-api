package com.enigma.eprocurement.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VendorRequest {

    @NotBlank(message = "vendor id is required")
    private String vendorId;

    private String name;

    private String mobilePhone;

    private String address;
}
