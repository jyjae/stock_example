package com.example.stockex.service;

import com.example.stockex.domain.Stock;
import com.example.stockex.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticLockStockService {

    private StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        // 락을 걸고 데이터를 가지고 옴 (findByWithPessimisticLock)
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);

        stock.decrease(1L);

        stockRepository.saveAndFlush(stock);
    }
}
