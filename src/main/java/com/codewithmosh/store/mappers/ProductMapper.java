package com.codewithmosh.store.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    List<ProductDto> toDto(List<Product> products);
}
