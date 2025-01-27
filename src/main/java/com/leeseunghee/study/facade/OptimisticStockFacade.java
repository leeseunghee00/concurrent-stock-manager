package com.leeseunghee.study.facade;

import org.springframework.stereotype.Component;

import com.leeseunghee.study.service.OptimisticLockStockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OptimisticStockFacade {

	private final OptimisticLockStockService optimisticLockStockService;

	public void decrease(Long id, Long quantity) throws InterruptedException {
		while (true) {
			try	{
				optimisticLockStockService.decrease(id, quantity);
				break;
			} catch (Exception ex) {
				Thread.sleep(50);
			}
		}
	}
}
