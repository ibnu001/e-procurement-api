package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.entity.Vendor;
import com.enigma.eprocurement.model.request.VendorRequest;
import com.enigma.eprocurement.model.response.VendorResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VendorService {

    Vendor create(Vendor vendor);

    Page<VendorResponse> getAll(Integer page, Integer size);

    Vendor getById(String id);

    VendorResponse update(VendorRequest request);




}
