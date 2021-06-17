package com.sda_store.controller;

import com.sda_store.controller.dto.shoppingCart.ShoppingCartOrderLineDto;
import com.sda_store.controller.dto.shoppingCart.ProductShoppingCartResponseDto;
import com.sda_store.controller.dto.shoppingCart.ShoppingCartResponseDto;
import com.sda_store.exception.OutOfStockException;
import com.sda_store.exception.ResourceNotFoundInDatabase;
import com.sda_store.model.*;
import com.sda_store.service.OrderService;
import com.sda_store.service.ProductService;
import com.sda_store.service.ShoppingCartService;
import com.sda_store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;
    private ProductService productService;
    private UserService userService;
    private OrderService orderService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService, UserService userService, OrderService orderService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }


    // request param face parte din URL, de exemplu products?page=0&pageSize=10 -- page, pageSize sunt request params
    // request body este atunci cand trimitem un json : { productId: 5 }
    // TODO: extract duplicate code;
    @PutMapping(path = "/shopping-cart")
    public ShoppingCartResponseDto addProductToCart(@RequestBody Long productId) {
        UserDetails loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByEmail(loggedUser.getUsername());
        Product product = productService.findById(productId);
        ShoppingCart shoppingCart = shoppingCartService.addProductToCart(product, user.getShoppingCart());
        return mapShoppingCartToResponseDto(shoppingCart);
    }

    @GetMapping(path = "/shopping-cart")
    public ShoppingCartResponseDto getShoppingCartResponse() {
        UserDetails loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByEmail(loggedUser.getUsername());
        return mapShoppingCartToResponseDto(user.getShoppingCart());
    }

    @PostMapping(path = "/order")
    public HttpStatus makeOrder(@RequestBody List<ShoppingCartOrderLineDto> shoppingCartOrderLineDtos) {
        List<OrderLine> orderLineList =
                shoppingCartOrderLineDtos
                        .stream()
                        .map(this::mapOrderLineDtoToOrderLine) // callout la o metoda din aceasta clasa fara nevoia de a mai da paramentru
                        .collect(Collectors.toList());

        Order dbOrder = orderService.createOrder(orderLineList);
        if(dbOrder != null) {
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    public OrderLine mapOrderLineDtoToOrderLine(ShoppingCartOrderLineDto shoppingCartOrderLineDto) {
        Product product = productService.findById(shoppingCartOrderLineDto.getProductId());
        if (product == null) {
            throw new ResourceNotFoundInDatabase(String.format("Product with id %d not found", shoppingCartOrderLineDto.getProductId()));
        }
        if ((product.getStock() - shoppingCartOrderLineDto.getQuantity()) < 0) {
            throw new OutOfStockException(String.format("Not enough products in stock for product: %s", product.getName()));
        }
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(shoppingCartOrderLineDto.getQuantity());
        return orderLine;
    }

    public ShoppingCartResponseDto mapShoppingCartToResponseDto(ShoppingCart shoppingCart) {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        List<ProductShoppingCartResponseDto> productShoppingCartResponseDtoList = new ArrayList<>();

        for (Product product : shoppingCart.getProductList()) {
            ProductShoppingCartResponseDto dto = new ProductShoppingCartResponseDto();
            dto.setId(product.getId());
            dto.setProductName(product.getName());
            productShoppingCartResponseDtoList.add(dto);
        }

        shoppingCartResponseDto.setProductsInCart(productShoppingCartResponseDtoList);
        return shoppingCartResponseDto;
    }


}
