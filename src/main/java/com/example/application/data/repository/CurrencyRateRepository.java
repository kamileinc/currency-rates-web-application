package com.example.application.data.repository;

import com.example.application.data.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {
}
