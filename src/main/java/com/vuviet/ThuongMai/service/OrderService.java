package com.vuviet.ThuongMai.service;

import com.vuviet.ThuongMai.dto.requestdto.ReqOrderDetailDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResOrderDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResShortOrder;
import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Order;
import com.vuviet.ThuongMai.entity.OrderDetail;
import com.vuviet.ThuongMai.entity.Product;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.repository.OrderDetailRepository;
import com.vuviet.ThuongMai.repository.OrderRepository;
import com.vuviet.ThuongMai.repository.ProductRepository;
import com.vuviet.ThuongMai.repository.UserRepository;
import com.vuviet.ThuongMai.util.constant.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ResOrderDTO createOrder(ReqOrderDetailDTO req) {
        Order order = new Order();
        order.setName(req.getName());
        order.setAddress(req.getAddress());
        order.setPhoneNumber(req.getPhoneNumber());
        order.setStatus(OrderStatus.ORDER);
        order.setPaymentStatus(req.getPaymentStatus());
        order.setPaymentMethod(req.getPaymentMethod());

        User user=this.userRepository.findById(req.getUserId()).get();
        order.setUser(user);

        order= this.orderRepository.save(order);
        ResOrderDTO resOrderDTO = new ResOrderDTO();

        resOrderDTO.setName(order.getName());
        resOrderDTO.setAddress(order.getAddress());
        resOrderDTO.setPhoneNumber(order.getPhoneNumber());
        resOrderDTO.setStatus(order.getStatus());
        resOrderDTO.setCreatedBy(order.getCreatedBy());
        resOrderDTO.setCreatedAt(order.getCreatedAt());
        resOrderDTO.setUpdatedBy(order.getUpdatedBy());
        resOrderDTO.setUpdatedAt(order.getUpdatedAt());
        resOrderDTO.setPaymentMethod(order.getPaymentMethod());
        resOrderDTO.setPaymentStatus(order.getPaymentStatus());
        long totalPrice = 0;

        List<OrderDetail> orderDetails=new ArrayList<>();

        List<ResOrderDTO.Item> items=new ArrayList<>();
        if(req.getItems()!=null){
            for(ReqOrderDetailDTO.ItemDTO item:req.getItems()){

                Optional<Product> op=this.productRepository.findById(item.getId());
                if(op.isPresent()){
                    Product product=op.get();
                    OrderDetail orderDetail=new OrderDetail();
                    orderDetail.setProduct(product);
                    orderDetail.setQuantity(item.getQuantity());
                    orderDetail.setPrice(item.getQuantity()* product.getPriceUnit());
                    orderDetail.setOrder(order);
                    totalPrice+=orderDetail.getPrice();

                    this.orderDetailRepository.save(orderDetail);
                    ResOrderDTO.Item resItem=new ResOrderDTO.Item();
                    resItem.setQuantity(item.getQuantity());
                    resItem.setPrice(item.getQuantity()* product.getPriceUnit());
                    resItem.setName(product.getName());

                    items.add(resItem);
                    orderDetails.add(orderDetail);}


            }
        }
        order.setOrderDetails(orderDetails);
        order.setTotalPrice(totalPrice);

        resOrderDTO.setItems(items);
        resOrderDTO.setTotalPrice(totalPrice);
        this.orderRepository.save(order);
        resOrderDTO.setId(order.getId());
        return resOrderDTO;
    }

    public Order updateStatusOrder(Order order) {

        Order rs=this.getById(order.getId());
        rs.setStatus(order.getStatus());

        this.orderRepository.save(rs);

        if(rs.getStatus()==OrderStatus.COMPLETE){
            List<OrderDetail> orderDetails=rs.getOrderDetails();
            for(OrderDetail orderDetail:orderDetails){
                Product product=orderDetail.getProduct();
                long unit=product.getUnitInStock();
                product.setUnitInStock(unit-orderDetail.getQuantity());
                product.setTotalSold(orderDetail.getQuantity());
                this.productRepository.save(product);
            }
        }
        return rs;

    }

    public ResOrderDTO getOrderById(long orderId) {
        // Lấy thông tin order theo ID
        Optional<Order> orderOp=this.orderRepository.findById(orderId);
        if(orderOp.isPresent()){
            Order order=orderOp.get();
            // Tạo ResOrderDTO từ order
            ResOrderDTO resOrderDTO = new ResOrderDTO();
            resOrderDTO.setId(order.getId());
            resOrderDTO.setTotalPrice(order.getTotalPrice());
            resOrderDTO.setName(order.getName());
            resOrderDTO.setPhoneNumber(order.getPhoneNumber());
            resOrderDTO.setAddress(order.getAddress());
            resOrderDTO.setCreatedAt(order.getCreatedAt());
            resOrderDTO.setUpdatedAt(order.getUpdatedAt());
            resOrderDTO.setCreatedBy(order.getCreatedBy());
            resOrderDTO.setUpdatedBy(order.getUpdatedBy());
            resOrderDTO.setStatus(order.getStatus());

            // Chuyển đổi danh sách OrderDetail sang danh sách ResOrderDTO.Item
            List<ResOrderDTO.Item> items = order.getOrderDetails().stream().map(orderDetail -> {
                ResOrderDTO.Item itemDTO = new ResOrderDTO.Item();
                itemDTO.setName(orderDetail.getProduct().getName());
                itemDTO.setQuantity(orderDetail.getQuantity());
                itemDTO.setPrice(orderDetail.getPrice());
                return itemDTO;
            }).collect(Collectors.toList());

            resOrderDTO.setItems(items);

            return resOrderDTO;
        }
        return null;

    }

    public ResultPageDTO getAllOrderByUser(Specification<Order> spec, Pageable pageable){
        Page<Order> orders=this.orderRepository.findAll(spec,pageable);
        ResultPageDTO resultPageDTO=new ResultPageDTO();
        ResultPageDTO.Meta meta=new ResultPageDTO.Meta();

        meta.setPage(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());
        meta.setTotalPage(orders.getTotalPages());
        meta.setTotalElement(orders.getTotalElements());

        resultPageDTO.setMeta(meta);
        List<Order> orderList=orders.getContent();
        List<ResShortOrder> resShortOrderList=orderList.stream()
                .map(x->{
                    ResShortOrder resShortOrder=new ResShortOrder();
                    resShortOrder.setId(x.getId());
                    resShortOrder.setTotalPrice(x.getTotalPrice());
                    return resShortOrder;
                }).toList();
        resultPageDTO.setResult(resShortOrderList);
        return resultPageDTO;
    }

    public ResultPageDTO getAllOrderByAdmin(Specification<Order> spec,Pageable pageable){
        Page<Order> orders=this.orderRepository.findAll(spec,pageable);
        ResultPageDTO resultPageDTO=new ResultPageDTO();
        ResultPageDTO.Meta meta=new ResultPageDTO.Meta();

        meta.setPage(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());
        meta.setTotalPage(orders.getTotalPages());
        meta.setTotalElement(orders.getTotalElements());

        resultPageDTO.setMeta(meta);
        resultPageDTO.setResult(orders.getContent());
        return resultPageDTO;
    }

    public void cancelOrder(Order order) {
        Order rs=this.getById(order.getId());

        rs.setStatus(OrderStatus.CANCEL);
        this.orderRepository.save(rs);


    }

    public Order getById(long id) {
        Optional<Order> orderOp=this.orderRepository.findById(id);
        if(orderOp.isPresent()){
            return orderOp.get();
        }
        return null;
    }

}
