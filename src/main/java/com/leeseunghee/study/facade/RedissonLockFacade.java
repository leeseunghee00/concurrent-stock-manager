package com.leeseunghee.study.facade;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.leeseunghee.study.service.StockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedissonLockFacade {

	private final RedissonClient redissonClient;
	private final StockService stockService;

	public void decrease(Long id, Long quantity) {
		RLock lock = redissonClient.getLock(id.toString());

		try	{
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);		// 대기시간, 종료시간 타임종류

			if (!available) {
				System.out.println("lock 획득 실패");
				return;
			}

			stockService.decrease(id, quantity);

		} catch (InterruptedException ex) {
			throw  new RuntimeException(ex);
		} finally {
			lock.unlock();	// 락 해제
		}
	}
}
