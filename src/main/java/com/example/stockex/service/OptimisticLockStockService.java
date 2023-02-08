package com.example.stockex.service;

import com.example.stockex.domain.Stock;
import com.example.stockex.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticLockStockService {
    private final StockRepository stockRepository;

    public OptimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /*
        Optimistic Lock 같은경우에는 실패했을 때 재시도를 해주어야한다.
        충돌이 빈번하게 일어난다면 Pessimistic Lock을 사용하고 그렇지 않는다면 Optimistic Lock을 사용.
     */
    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
