package com.concurrency_issue.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final StockRepository stockRepository;

    public synchronized void decrease(final Long id, final Long quantity) {
        Product product = stockRepository.findByProductId(id).orElseThrow();

        product.decrease(quantity);

        stockRepository.saveAndFlush(product);
    }

    public long getQuantitiy(long id) {
        return stockRepository.findByProductId(id)
                .orElseThrow()
                .getQuantity();
    }
}
