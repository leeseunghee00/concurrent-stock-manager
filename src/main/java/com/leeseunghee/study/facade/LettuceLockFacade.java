package com.leeseunghee.study.facade;

import org.springframework.stereotype.Component;

import com.leeseunghee.study.repository.RedisLockRepository;
import com.leeseunghee.study.service.StockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LettuceLockFacade {

	private final RedisLockRepository redisLockRepository;
	private final StockService stockService;

	public void decrease(Long id, Long quantity) throws InterruptedException {

		// 락 획득 시도
		while (!redisLockRepository.lock(id)) {
			Thread.sleep(100);	// 실패 시 100초 후 재시도
		}

		try	{
			stockService.decrease(id, quantity);
		} finally {
			redisLockRepository.unlock(id);
		}
	}
}
