package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.model.request.OrderRequest;
import com.enigma.eprocurement.model.response.OrderResponse;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Writer;
import java.util.List;

public interface OrderService {

    OrderResponse createNewOrder(OrderRequest request);
    OrderResponse getOrderById(String id);
    Page<OrderResponse> getAllOrder(String vendorname, Integer page, Integer size);
    void writeReportToCsv(Writer writer);
}
