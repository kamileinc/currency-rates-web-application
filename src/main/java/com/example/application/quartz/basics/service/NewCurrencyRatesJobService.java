package com.example.application.quartz.basics.service;

import com.example.application.data.CurrencyEndpoint;
import com.example.application.data.entity.Currency;
import com.example.application.data.entity.CurrencyRate;
import com.example.application.data.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NewCurrencyRatesJobService {

    @Autowired
    CurrencyService service;

    private AtomicInteger count = new AtomicInteger();

    public void executeSampleJob(String str) {
        System.out.println("The Job has begun..." + str);
        try {
            List<Currency> currencies = CurrencyEndpoint.getCurrencies();
            List<CurrencyRate> currencyRates = CurrencyEndpoint.getCurrencyRates();
            service.saveCurrencies(currencies);
            currencyRates = CurrencyEndpoint.mapCurrencyRatesWithCurrencies(currencies, currencyRates);
            service.saveCurrencyRates(currencyRates);

        } catch (Exception e) {
            e.printStackTrace();
        }
        count.incrementAndGet();
        System.out.println("Job has finished...");
    }

    public int getNumberOfInvocations() {
        return count.get();
    }
}
