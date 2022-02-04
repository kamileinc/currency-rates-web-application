package com.example.application.views.currency;

import com.example.application.data.CurrencyEndpoint;
import com.example.application.data.entity.Currency;
import com.example.application.data.entity.CurrencyRate;
import com.example.application.data.service.CurrencyService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Currency rates")
@Route(value = "", layout = MainLayout.class)
public class CurrencyListView extends VerticalLayout {

    private final CurrencyService service;
    Grid<Currency> grid = new Grid<>(Currency.class);
    RatesListView ratesListView;

    public CurrencyListView(CurrencyService service) {
        this.service = service;
        addCurrenciesToDB();
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureRatesListView();
        add(getContent());
        updateList();
        closeRatesListView();
    }

    private void closeRatesListView() {
        ratesListView.setCurrencyRates(null);
        ratesListView.setVisible(false);
        removeClassName("editing");
        updateList();
    }

    private void updateList() {
        grid.setItems(service.findAllCurrencies());
    }

    private void configureGrid() {
        grid.addClassName("currency-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(Currency::getAbbreviation).setHeader("Currency code");
        grid.addColumn(Currency::getNameEn).setHeader("Full name");
        grid.addColumn(Currency::getNumber).setHeader("Currency number (CcyNbr)");
        grid.addColumn(Currency::getUnits).setHeader("Currency units (CcyMnrUnts)");
        grid.addColumn(Currency::getCurrencyRate).setHeader("Current exchange rate");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> updateRatesList(e.getValue()));
    }

    private void updateRatesList(Currency currency) {
        if (currency == null) {
            closeRatesListView();
        }
        else {
            addCurrencyRatesToDB(currency);
            Currency updatedCurrency = service.findCurrency(currency.getId()).get();
            ratesListView.setCurrencyRates(updatedCurrency);
            ratesListView.setVisible(true);
            addClassName("editing");
        }
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, ratesListView);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, ratesListView);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureRatesListView() {
        List<CurrencyRate> currencyRates = service.findAllCurrencyRates();;
        ratesListView = new RatesListView(currencyRates);
        ratesListView.setWidth("50em");
    }

    public void addCurrenciesToDB() {
        List<Currency> currencies = CurrencyEndpoint.getCurrencies();
        List<CurrencyRate> currencyRates = CurrencyEndpoint.getCurrencyRates();
        service.saveCurrencies(currencies);
        currencyRates = CurrencyEndpoint.mapCurrencyRatesWithCurrencies(currencies, currencyRates);
        service.saveCurrencyRates(currencyRates);
    }

    public void addCurrencyRatesToDB(Currency currency) {
        List<CurrencyRate> currencyRates = CurrencyEndpoint.getCurrencyRatesForTimePeriod(currency.getAbbreviation());
        currencyRates = CurrencyEndpoint.mapCurrencyRatesWithCurrency(currency, currencyRates);
        service.saveCurrencyRates(currencyRates);
    }
}
