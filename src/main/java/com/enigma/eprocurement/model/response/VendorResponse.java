package com.enigma.eprocurement.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class VendorResponse {

    private String id;
    private String name;
    private String address;

    private String mobilePhone;

}
