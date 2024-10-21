package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.requestdto.ReqOrderDetailDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResOrderDTO;
import com.vuviet.ThuongMai.service.OrderService;
import com.vuviet.ThuongMai.service.ProductService;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    private final ProductService productService;

    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @PostMapping("/orders")
    @ApiMessage("Create a order")
    public ResponseEntity<ResOrderDTO> createOrder(@RequestBody ReqOrderDetailDTO reqOrderDetailDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.createOrder(reqOrderDetailDTO));
    }
}
