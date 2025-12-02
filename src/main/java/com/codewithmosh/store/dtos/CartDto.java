package com.codewithmosh.store.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    private List<CartItemDto> items = new ArrayList<CartItemDto>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}