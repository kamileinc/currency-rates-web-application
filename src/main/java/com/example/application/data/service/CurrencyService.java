package com.example.application.data.service;

import com.example.application.data.entity.Currency;
import com.example.application.data.entity.CurrencyRate;
import com.example.application.data.repository.CurrencyRateRepository;
import com.example.application.data.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyRateRepository currencyRateRepository) {

        this.currencyRepository = currencyRepository;
        this.currencyRateRepository = currencyRateRepository;
    }

    public List<Currency> findAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Optional<Currency> findCurrency(int id) {
        return currencyRepository.findById(id);
    }

    public List<Currency> searchCurrencyForCalculator() {
        return currencyRepository.search();
    }

    public void saveCurrency(Currency currency) {
        if (currency != null) {
            try {
                currencyRepository.save(currency);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCurrencies(List<Currency> currencies) {
        if (!currencies.isEmpty()) {
            for (Currency currency : currencies) {
                saveCurrency(currency);
            }
        }
    }

    public void saveCurrencyRates(List<CurrencyRate> currencyRates) {
        if (!currencyRates.isEmpty()) {
            for (CurrencyRate currencyRate : currencyRates) {
                saveCurrencyRate(currencyRate);
            }
        }
    }

    public List<CurrencyRate> findAllCurrencyRates() {
        return currencyRateRepository.findAll();
    }

    public void saveCurrencyRate(CurrencyRate currencyRate) {
        if (currencyRate != null) {
            try {
                currencyRateRepository.save(currencyRate);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
