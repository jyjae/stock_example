package com.example.stockex.transaction;

import com.example.stockex.service.StockService;

public class TransactionStockService {
    
    private StockService stockService;

    public TransactionStockService(StockService stockService) {
        this.stockService = stockService;
    }
    
    public void decrease(Long id, Long quantity) {
        startTransaction();
        
        stockService.decrease(id, quantity); // 예를 들어 10 : 00에 호출했고
        
        endTransaction(); // 트랜잭션이 끝난 후 갱신이 10 : 05 에 일어났다면

        // 10 : 00 ~ 10 : 05 사이에 다른 스레드가 갱신되지 않은 값에 접근을 하게 된다.
    }

    private void endTransaction() {
    }

    private void startTransaction() {
    }


}
