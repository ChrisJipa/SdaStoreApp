package com.sda_store.controller;

import com.sda_store.controller.dto.order.OrderLineDto;
import com.sda_store.controller.dto.order.OrderResponseDto;
import com.sda_store.controller.dto.user.AddressDto;
import com.sda_store.model.Address;
import com.sda_store.model.Order;
import com.sda_store.model.OrderLine;
import com.sda_store.model.User;
import com.sda_store.service.OrderService;
import com.sda_store.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class OrderController {

    private UserService userService;
    private OrderService orderService;

    public OrderController(UserService userService,
                           OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping(path = "/orders")
    public List<OrderResponseDto> getOrders() {
        Authentication authentication;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedUser = userService.findByEmail(userDetails.getUsername());
        List<Order> userOrders = orderService.findAllOrdersByUserId(loggedUser.getId());
        List<OrderResponseDto> orderResponseDtoList =
                userOrders
                .stream()
                .map(this :: mapOrderToOrderResponseDto)
                .collect(Collectors.toList());

        return orderResponseDtoList;

    }

    public OrderResponseDto mapOrderToOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        List<OrderLineDto> orderLineDtoList =
                order
                        .getOrderLineList()
                        .stream()
                        .map(orderLine -> mapOrderLineToOrderLineDto(orderLine))
                        .collect(Collectors.toList());
        dto.setOrderLineDtoList(orderLineDtoList);
        Address address = order.getUser().getAddress();
        dto.setAddressDto(AddressDto.mapAddressToAddressDto(address));
        dto.setTotalPrice(order.getTotal());

        return dto;
    }

    public OrderLineDto mapOrderLineToOrderLineDto(OrderLine orderLine) {
        OrderLineDto orderLineDto = new OrderLineDto();
        orderLineDto.setProductName(orderLine.getProduct().getName());
        orderLineDto.setPrice(orderLine.getProduct().getPrice());
        orderLineDto.setQuantity(orderLine.getQuantity());
        return orderLineDto;
    }

}
