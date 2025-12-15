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
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;




@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    //Adding cart
    @PostMapping
    public ResponseEntity<CartDto> createCart(
        UriComponentsBuilder uriBuilder
    ){
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }


    //Adding product to cart
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addProductToCart(
        @PathVariable @NonNull
        UUID cartId,
        @RequestBody
        AddItemToCartRequest request
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.notFound().build();
        }

        var product =  productRepository.findById(request.getProductId()).orElse(null);
        if(product == null){
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);
        var cartItemDto = cartMapper.toDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    //Getting a cart
    @GetMapping("/{cartId}")    
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.notFound().build();
        }
        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    //Updating a cart
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequest request
    ){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error","Cart not found.")
            );
        }
        
        var cartItem = cart.getItem(productId);
        if(cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error","Product not found in the cart.")
            );
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }
}