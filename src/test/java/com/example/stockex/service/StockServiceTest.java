package com.example.stockex.service;

import com.example.stockex.domain.Stock;
import com.example.stockex.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    void stock_decrease() {
        // when
        stockService.decrease(1L, 1L);

        //then
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(101L, stock.getQuantity());
    }

    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;

        // 병렬 작업 시 여러개의 작업을 효율적으로 처리하기 위해 제공되는 자바 라이브러리
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 어떤 스레드가 다른 스레드에서 작업이 완료될 때까지 기다리도록 해주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                }finally {
                    // latch 숫자 감소
                    latch.countDown();
                }
            });
        }

        // latch가 0이 될때까지 대기
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        // race condition이 발생한다.
        assertEquals(0L, stock.getQuantity());
    }

}