package com.leeseunghee.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leeseunghee.study.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
