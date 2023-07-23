package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.VendorService;
import com.enigma.eprocurement.entity.Vendor;
import com.enigma.eprocurement.model.request.VendorRequest;
import com.enigma.eprocurement.model.response.VendorResponse;
import com.enigma.eprocurement.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Vendor create(Vendor vendor) {
        try {
            return vendorRepository.save(vendor);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exist");
        }
    }

    @Override
    public Vendor getById(String id) {
        return vendorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "vendor not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public VendorResponse update(VendorRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Vendor vendor = getById(request.getVendorId());
        vendor.setName(request.getName());
        vendor.setAddress(request.getAddress());
        vendor.setMobilePhone(request.getMobilePhone());
        vendor.setUpdatedBy(authentication.getName());

        vendorRepository.save(vendor);

        return VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .mobilePhone(vendor.getMobilePhone())
                .address(vendor.getAddress())
                .build();
    }

    @Override
    public Page<VendorResponse> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vendor> vendors = vendorRepository.findAll(pageable);
        List<VendorResponse> vendorResponses = new ArrayList<>();

        for (Vendor vendor : vendors.getContent()) {
            VendorResponse response = VendorResponse.builder()
                    .id(vendor.getId())
                    .name(vendor.getName())
                    .mobilePhone(vendor.getMobilePhone())
                    .address(vendor.getAddress())
                    .build();

            vendorResponses.add(response);
        }
        return new PageImpl<>(vendorResponses, pageable, vendors.getTotalElements());
    }

}
