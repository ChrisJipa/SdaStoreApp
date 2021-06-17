package com.sda_store.service;

import com.sda_store.model.Product;
import com.sda_store.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart addProductToCart(Product product, ShoppingCart shoppingCart);
    ShoppingCart removeProductFromCart(Product product, ShoppingCart shoppingCart);
    void clearShoppingCart(Long shoppingCartId);

}
