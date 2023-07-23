package com.enigma.eprocurement.controller;

import com.enigma.eprocurement.Service.OrderService;
import com.enigma.eprocurement.model.request.OrderRequest;
import com.enigma.eprocurement.model.response.CommonResponse;
import com.enigma.eprocurement.model.response.OrderResponse;
import com.enigma.eprocurement.model.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createNewOrder(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createNewOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new order")
                        .data(orderResponse)
                        .build());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getOrderByID(@PathVariable String id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get order by id")
                        .data(orderResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllOrder(
            @RequestParam(name = "vendroName", required = false) String vendorName,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        Page<OrderResponse> orderResponses = orderService.getAllOrder(vendorName, page - 1, size);
        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(orderResponses.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all order")
                        .data(orderResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @GetMapping(path = "/export-to-csv")
    public ResponseEntity<?> getAllEmployeesInCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        orderService.writeReportToCsv(servletResponse.getWriter());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .build();
    }
}
