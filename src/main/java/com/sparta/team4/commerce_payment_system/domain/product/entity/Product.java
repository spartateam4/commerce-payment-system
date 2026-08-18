package com.sparta.team4.commerce_payment_system.domain.product.entity;

import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private int totalPrice;

    private int stock;

    private String description;

    public Product(String name, String category, int totalPrice, int stock, String description) {
        this.name = name;
        this.category = category;
        this.totalPrice = totalPrice;
        this.stock = stock;
        this.description = description;
    }
}