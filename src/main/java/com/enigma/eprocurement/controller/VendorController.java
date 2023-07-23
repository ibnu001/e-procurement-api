package com.enigma.eprocurement.controller;

import com.enigma.eprocurement.Service.VendorService;
import com.enigma.eprocurement.model.request.VendorRequest;
import com.enigma.eprocurement.model.response.CommonResponse;
import com.enigma.eprocurement.model.response.PagingResponse;
import com.enigma.eprocurement.model.response.VendorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/vendors")
public class VendorController {

    private final VendorService vendorService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllVendor(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        Page<VendorResponse> vendorResponses = vendorService.getAll(page - 1, size);
        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(vendorResponses.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all vendor")
                        .data(vendorResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateVendor(@RequestBody VendorRequest request) {
        VendorResponse vendorResponse = vendorService.update(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update vendor")
                        .data(vendorResponse)
                        .build());
    }
}
