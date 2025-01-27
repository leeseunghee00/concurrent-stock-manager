package com.leeseunghee.study.facade;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.leeseunghee.study.domain.Stock;
import com.leeseunghee.study.repository.StockRepository;

@SpringBootTest
class LettuceLockFacadeTest {

	@Autowired
	private LettuceLockFacade lettuceLockFacade;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	public void before() {
		stockRepository.save(new Stock(1L, 100L));
	}

	@AfterEach
	public void after() {
		stockRepository.deleteAll();
	}

	@Test
	public void 동시_100개_요청_redis_lettuce() throws InterruptedException {
		// given -- 테스트의 상태 설정
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when -- 테스트하고자 하는 행동
		for (int i = 0; i < threadCount; i++) {
			executorService.execute(() -> {
				try {
					lettuceLockFacade.decrease(1L, 1L);
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Stock stock = stockRepository.findById(1L).orElseThrow();

		// then -- 예상되는 변화 및 결과
		assertEquals(0, stock.getQuantity());
	}
}