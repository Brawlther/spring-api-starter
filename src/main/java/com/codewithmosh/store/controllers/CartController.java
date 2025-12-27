package com.codewithmosh.store.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItemRequest;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
    //Attributes
    private final CartService cartService;

    //Endpoints
    @PostMapping
    public ResponseEntity<CartDto> createCart(
        UriComponentsBuilder uriBuilder
    ){
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Adds a product to the cart.")
    public ResponseEntity<CartItemDto> addProductToCart(
        @PathVariable("cartId") @NonNull @Parameter(description = "The ID of the cart.")
        UUID cartId,
        @RequestBody
        AddItemToCartRequest request
    ) {
        var cartItemDto = cartService.addProductToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")    
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCart(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequest request
    ){
        var cartItem = cartService.updateCart(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId
    ){
        cartService.removeItemFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") UUID cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    //Exception Handlers
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Product not found in the cart."));
    }
}