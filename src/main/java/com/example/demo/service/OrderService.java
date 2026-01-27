package com.example.demo.service;

import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.CustomerNotFound;
import com.example.demo.exception.ProductNotFound;
import com.example.demo.model.Customer;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.Product;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;
}
