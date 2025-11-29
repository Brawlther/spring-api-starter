package com.codewithmosh.store.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProducctController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @GetMapping("")
    public Iterable<ProductDto> getAllProducts(@RequestParam(required = false, defaultValue = "1", name = "sort") String sortStr){
        if(!Set.of("name","categoryId").contains(sortStr)){
            sortStr = "name";
        }
        return productRepository.findAll(Sort.by(sortStr)).stream().map(
            productMapper::toDto
        ).toList();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProduct(@PathVariable byte categoryId){
        var products = productRepository.findAllByCategoryId(categoryId);
        if(products.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(products));
    }
}
