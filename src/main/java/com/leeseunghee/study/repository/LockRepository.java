package com.leeseunghee.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leeseunghee.study.domain.Stock;

// 실무에서는 별도의 JDBC 를 사용할 것을 권장!
public interface LockRepository extends JpaRepository<Stock, Long> {

	@Query(value = "SELECT get_lock(:key, 3000)", nativeQuery = true)
	void getLock(String key);

	@Query(value = "SELECT release_lock(:key)", nativeQuery = true)
	void releaseLock(String key);
}
