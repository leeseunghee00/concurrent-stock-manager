package com.leeseunghee.study.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.leeseunghee.study.repository.LockRepository;
import com.leeseunghee.study.service.StockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

	private final LockRepository lockRepository;
	private final StockService stockService;

	@Transactional
	public void decrease(Long id, Long quantity) {
		try {
			lockRepository.getLock(id.toString());
			stockService.decrease_2(id, quantity);
		} finally {
			lockRepository.releaseLock(id.toString());
		}
	}
}
