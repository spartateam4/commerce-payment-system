package com.sparta.team4.commerce_payment_system.domain.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.entity.QProduct;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Product> searchProducts(
            String category, Integer minPrice, Integer maxPrice, Pageable pageable) {

        QProduct product = QProduct.product;
        BooleanBuilder condition = new BooleanBuilder();

        if (category != null && !category.isBlank()) {
            condition.and(product.category.eq(category));
        }
        if (minPrice != null) {
            condition.and(product.price.goe(minPrice));   // totalPrice → price
        }
        if (maxPrice != null) {
            condition.and(product.price.loe(maxPrice));   // totalPrice → price
        }

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(condition)
                .orderBy(product.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(products, pageable, total != null ? total : 0L);
    }
}