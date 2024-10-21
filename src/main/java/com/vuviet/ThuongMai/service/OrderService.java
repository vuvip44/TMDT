package com.vuviet.ThuongMai.service;

import com.vuviet.ThuongMai.dto.requestdto.ReqOrderDetailDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResOrderDTO;
import com.vuviet.ThuongMai.entity.Order;
import com.vuviet.ThuongMai.entity.OrderDetail;
import com.vuviet.ThuongMai.entity.Product;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.repository.OrderDetailRepository;
import com.vuviet.ThuongMai.repository.OrderRepository;
import com.vuviet.ThuongMai.repository.ProductRepository;
import com.vuviet.ThuongMai.repository.UserRepository;
import com.vuviet.ThuongMai.util.constant.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

        User user=this.userRepository.findById(req.getUserId()).get();
        order.setUser(user);

        ResOrderDTO resOrderDTO = new ResOrderDTO();

        resOrderDTO.setName(order.getName());
        resOrderDTO.setAddress(order.getAddress());
        resOrderDTO.setPhoneNumber(order.getPhoneNumber());
        resOrderDTO.setStatus(order.getStatus());
        resOrderDTO.setCreatedBy(order.getCreatedBy());
        resOrderDTO.setCreatedAt(order.getCreatedAt());
        resOrderDTO.setUpdatedBy(order.getUpdatedBy());
        resOrderDTO.setUpdatedAt(order.getUpdatedAt());

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

                    totalPrice+=orderDetail.getPrice();

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

}
