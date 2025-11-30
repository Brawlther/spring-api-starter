package com.codewithmosh.store.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
    List<ProductDto> toDto(List<Product> products);
    Product toEntity(ProductDto dto);
    @Mapping(target = "id", ignore = true)
    void update(ProductDto dto, @MappingTarget Product product);
}
