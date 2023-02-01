package com.example.stockex.service;

import com.example.stockex.domain.Stock;
import com.example.stockex.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    //@Transactional
    public synchronized void decrease(Long id, Long quantity) {
        // synchronized 를 붙여서 하나의 스레드씩 접근하게 해도 테스트가 실패했다.
        // 그 이유는 Transactional 애노테이션 작동방식 때문인데
        // 스프링에서는 이 애노테이션을 이용하면 우리가 만든 클래스를 래핑한 클래스를 새로 만들어서 실행한다. (TransactionStockService 참고)
        //  -> 트랜잭션이 끝나면 db에 업데이트를 하는데 여기서 문제가 발생한다.
        //  -> db에 업데이트를 하기 전에 다른 스레드가 decrease 메소드에 접근할 수가 있다.
        //  -> 그렇게 되면 다른 스레드는 갱신되지 않은 값을 가져가서 이전과 동일한 문제가 발생한다.
        Stock stock = stockRepository.findById(id).orElseThrow();

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
    // synchronized의 문제점 : 한 프로세스안에서만 보장이 된다.
    //                      서버가 2대 이상일 경우 데이터 접근을 여러곳에서 할 수 있게 된다. 그래서 race condition이 발생하게 된다.
    //                      -> 해결방안: mysql에서 지원해주는 방법을 사용해보자.


}
