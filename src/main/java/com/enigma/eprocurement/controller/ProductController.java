package com.enigma.eprocurement.controller;

import com.enigma.eprocurement.Service.ProductService;
import com.enigma.eprocurement.entity.Product;
import com.enigma.eprocurement.model.request.ProductRequest;
import com.enigma.eprocurement.model.response.CommonResponse;
import com.enigma.eprocurement.model.response.PagingResponse;
import com.enigma.eprocurement.model.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.undo.AbstractUndoableEdit;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createNewProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new product")
                        .data(productResponse)
                        .build());
    }

    @PostMapping(path = "/bulk")
    public ResponseEntity<?> createBulkNewProduct(@RequestBody List<ProductRequest> request) {
        List<ProductResponse> productResponses = productService.createBulk(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<List<ProductResponse>>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create bulk new product")
                        .data(productResponses)
                        .build());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        ProductResponse productResponse = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get product")
                        .data(productResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllProduct(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "price", required = false) Long price,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        Page<ProductResponse> productResponses = productService.getAllByNameOrPrice(name, price, page - 1, size);
        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(productResponses.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all product")
                        .data(productResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.update(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update product")
                        .data(productResponse)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> hardDeleteById(@PathVariable(name = "id") String id) {
        productService.hardDeleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete product")
                        .build());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> softDeleteById(@PathVariable(name = "id") String id) {
        productService.softDeleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete product")
                        .build());
    }
}
