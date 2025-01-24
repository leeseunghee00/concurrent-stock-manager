package com.leeseunghee.study.service;

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
class StockServiceTest {

	@Autowired
	private StockService stockService;

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
	public void 재고감소() {
		// given -- 테스트의 상태 설정
		stockService.decrease(1L, 1L);

		// when -- 테스트하고자 하는 행동
		Stock stock = stockRepository.findById(1L).orElseThrow();

		// then -- 예상되는 변화 및 결과
		assertEquals(99, stock.getQuantity());
	}

	/**
	 * ERROR! 예상되는 재고량과 실제 차감된 재고량이 다르다
	 */
	@Test
	public void 동시_100개_요청() throws InterruptedException {
		// given -- 테스트의 상태 설정
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when -- 테스트하고자 하는 행동
		for (int i = 0; i < threadCount; i++) {
			executorService.execute(() -> {
				try {
					stockService.decrease(1L, 1L);
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