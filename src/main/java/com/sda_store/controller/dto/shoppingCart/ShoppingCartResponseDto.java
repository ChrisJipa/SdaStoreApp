package com.sda_store.controller.dto.shoppingCart;

import java.util.List;

public class ShoppingCartResponseDto {
    private List<ProductShoppingCartResponseDto> productsInCart;

    public List<ProductShoppingCartResponseDto> getProductsInCart() {
        return productsInCart;
    }

    public void setProductsInCart(List<ProductShoppingCartResponseDto> productsInCart) {
        this.productsInCart = productsInCart;
    }
}
