package com.leeseunghee.study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.leeseunghee.study.domain.Stock;
import com.leeseunghee.study.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;

	@Transactional
	public void decrease(Long id, Long quantity) {
		Stock stock = stockRepository.findById(id).orElseThrow();

		stock.decrease(quantity);

		stockRepository.save(stock);
	}

	// 방법1. synchronized: 하나의 스레드만 접근하도록 한다
	@Transactional
	public synchronized void decrease_1(Long id, Long quantity) {
		Stock stock = stockRepository.findById(id).orElseThrow();

		stock.decrease(quantity);

		stockRepository.save(stock);
	}

	// 방법3. Optimistic Lock
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void decrease_2(Long id, Long quantity) {
		Stock stock = stockRepository.findById(id).orElseThrow();

		stock.decrease(quantity);

		stockRepository.save(stock);
	}
}
