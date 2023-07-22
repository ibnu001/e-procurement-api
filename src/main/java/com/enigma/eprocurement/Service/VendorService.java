package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.entity.Vendor;

public interface VendorService {

    Vendor create(Vendor vendor);

    Vendor getById(String id);

}
