package com.vuviet.ThuongMai.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vuviet.ThuongMai.dto.requestdto.ReqOrderDetailDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResOrderDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Order;
import com.vuviet.ThuongMai.service.OrderService;
import com.vuviet.ThuongMai.service.ProductService;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import com.vuviet.ThuongMai.util.constant.OrderStatus;
import com.vuviet.ThuongMai.util.error.IdInValidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResOrderDTO> createOrder(@RequestBody @Valid ReqOrderDetailDTO reqOrderDetailDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.createOrder(reqOrderDetailDTO));
    }



    @PutMapping("/admin/orders")
    @ApiMessage("Update status order")
    public ResponseEntity<Order> updateStatusOrder(@RequestBody Order order) throws IdInValidException {

        Order order1=this.orderService.getById(order.getId());
        if(order1==null){
            throw new IdInValidException("Id "+ order.getId()+" không tồn tại");
        }
        if(order1.getStatus()==OrderStatus.CANCEL){
            throw new IdInValidException("Đơn hàng id "+order1.getId()+" không thể được cập nhật");
        }
        return ResponseEntity.ok(this.orderService.updateStatusOrder(order));
    }

    @GetMapping("/orders/{id}")
    @ApiMessage("Get order by id")
    public ResponseEntity<ResOrderDTO> getOrderById(@PathVariable("id") long id) throws IdInValidException {
        ResOrderDTO resOrderDTO = this.orderService.getOrderById(id);
        if(resOrderDTO == null) {
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        return ResponseEntity.ok(resOrderDTO);
    }

    @GetMapping("/orders")
    @ApiMessage("Get all orders by user")
    public ResponseEntity<ResultPageDTO> getOrdersByUser(
            @Filter Specification<Order> spec,
            Pageable pageable
    ){
        return ResponseEntity.ok(this.orderService.getAllOrderByUser(spec, pageable));
    }

    @GetMapping("/admin/orders")
    @ApiMessage("Get all orders by admin")
    public ResponseEntity<ResultPageDTO> getOrdersByAdmin(
            @Filter Specification<Order> spec,
            Pageable pageable
    ){
        return ResponseEntity.ok(this.orderService.getAllOrderByAdmin(spec, pageable));
    }

    @PutMapping("/orders")
    @ApiMessage("Cancel a order")
    public ResponseEntity<Void> cancelOrder(@RequestBody Order order) throws IdInValidException {
        if(this.orderService.getOrderById(order.getId()) == null) {
            throw new IdInValidException("Id "+order.getId()+" không tồn tại");
        }
        Order order1=this.orderService.getById(order.getId());
        if(order1.getStatus()!= OrderStatus.ORDER){
            throw new IdInValidException("Đơn hàng "+order1.getId()+" không hủy được");
        }
        this.orderService.cancelOrder(order);
        return ResponseEntity.ok(null);
    }
}
