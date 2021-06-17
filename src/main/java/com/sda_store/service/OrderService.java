package com.sda_store.service;

import com.sda_store.model.Order;
import com.sda_store.model.OrderLine;

import java.util.*;

public interface OrderService {

    Order createOrder(List<OrderLine> orderLineList);

    List<Order> findAllOrders();

    List<Order> findAllOrdersByUserId(Long id);

}
