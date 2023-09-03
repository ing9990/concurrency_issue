package com.concurrency_issue.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_quantity", nullable = false)
    private Long quantity;

    public Product(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Product registerProduct(final Long productId, final Long quantity) {
        return new Product(productId, quantity);
    }

    public void decrease(final Long quantity) {
        if (this.quantity < quantity) {
            throw new RuntimeException("재고 부족");
        }

        this.quantity = this.quantity - quantity;
    }
}
