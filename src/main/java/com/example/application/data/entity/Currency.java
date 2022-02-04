//@XmlSchema(namespace = "http://www.lb.lt/WebServices/FxRates", elementFormDefault = XmlNsForm.QUALIFIED)
package com.example.application.data.entity;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "currency")
public class Currency extends AbstractEntity {

    @NotNull
    private String abbreviation;

    @NotNull
    private String nameLt;

    @NotNull
    private String nameEn;

    @NotNull
    private String number;

    @NotNull
    private int units;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "currency")
    @OrderBy("date DESC")
    @Nullable
    private List<CurrencyRate> currencyRates;

    public Currency() {
    }

    public Currency(String abbreviation, String nameLt, String nameEn, String number, int units) {
        this.abbreviation = abbreviation;
        this.nameLt = nameLt;
        this.nameEn = nameEn;
        this.number = number;
        this.units = units;
    }

    public String getCurrencyRate() {
        if (currencyRates.isEmpty()) {
            return "-";
        }
        else {
            return Double.toString(currencyRates.get(0).getSelectedAmount());
        }
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getNameLt() {
        return nameLt;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNumber() {
        return number;
    }

    public int getUnits() {
        return units;
    }

    public List<CurrencyRate> getCurrencyRates() {
        return currencyRates;
    }

    public void setCurrencyRate(List<CurrencyRate> currencyRates) {
        this.currencyRates = currencyRates;
    }
}
