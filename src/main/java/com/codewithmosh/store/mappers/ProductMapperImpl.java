package com.codewithmosh.store.mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;

@Component
public class ProductMapperImpl implements ProductMapper{
    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;
        BigDecimal price = null;
        Byte categoryId = null;

        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        categoryId = product.getCategory().getId();

        if (product.getCategory() != null) {
            categoryId = product.getCategory().getId();
        }

        ProductDto productDto = new ProductDto( id, name, description, price, categoryId );

        return productDto;
    }

    @Override
    public List<ProductDto> toDto(List<Product> products) {
        if (products == null) {
            return null;
        }

        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
