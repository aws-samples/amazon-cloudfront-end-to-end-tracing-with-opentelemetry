package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.product.entity.Product;
import com.aws.peach.domain.product.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAuroraRepository extends ProductRepository, JpaRepository<Product, String> {
}
