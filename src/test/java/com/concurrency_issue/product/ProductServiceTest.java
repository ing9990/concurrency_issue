package com.concurrency_issue.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(Product.registerProduct(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void stock_decrease() {
        productService.decrease(1L, 1L);

        Product product = stockRepository.findById(1L).orElseThrow();

        org.assertj.core.api.Assertions.assertThat(product.getQuantity())
                .isEqualTo(99L);
    }


    @DisplayName("동시에 100개의 재고 감소 요청")
    @Test
    void 동시에_100개의_재고_감소_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productService.decrease(1L, 1L);
                    long threadId = Thread.currentThread().getId();
                    long quantity = productService.getQuantitiy(1L);

                    System.out.println(threadId + " : " + quantity + " ");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product product = stockRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(0L, product.getQuantity());
    }
}