package com.example.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "currencyRate",
    uniqueConstraints = @UniqueConstraint(name = "UniqueDateAndSelectedCurrency", columnNames = { "date", "selectedCurrency" }))
public class CurrencyRate extends AbstractEntity {

    @NotNull
    private String type;

    @NotNull
    private String date;

    @NotNull
    private String comparisonCurrency;

    @NotNull
    private int comparisonAmount;

    @NotNull
    @Column(name="selectedCurrency")
    private String selectedCurrency;

    @NotNull
    private double selectedAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    public CurrencyRate() {
    }

    public CurrencyRate(String type, String date, String comparisonCurrency, int comparisonAmount, String selectedCurrency, double selectedAmount) {
        this.type = type;
        this.date = date;
        this.comparisonCurrency = comparisonCurrency;
        this.comparisonAmount = comparisonAmount;
        this.selectedCurrency = selectedCurrency;
        this.selectedAmount = selectedAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getComparisonCurrency() {
        return comparisonCurrency;
    }

    public int getComparisonAmount() {
        return comparisonAmount;
    }

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public double getSelectedAmount() {
        return selectedAmount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
