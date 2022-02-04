package com.example.application.data.repository;

import com.example.application.data.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    @Query("select c from Currency c where c.currencyRates is not empty ")
    List<Currency> search();
}