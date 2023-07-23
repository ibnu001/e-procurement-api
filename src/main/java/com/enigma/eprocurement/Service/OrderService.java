package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.model.request.OrderRequest;
import com.enigma.eprocurement.model.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.io.Writer;

public interface OrderService {

    OrderResponse createNewOrder(OrderRequest request);

    OrderResponse getOrderById(String id);

    Page<OrderResponse> getAllOrder(String date, Integer month, Integer page, Integer size);

    void writeReportToCsv(Writer writer, String date, Integer month);
}
