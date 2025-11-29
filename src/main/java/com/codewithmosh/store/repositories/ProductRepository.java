package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryId(byte categoryId);
}