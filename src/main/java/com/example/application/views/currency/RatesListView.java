package com.example.application.views.currency;

import com.example.application.data.entity.Currency;
import com.example.application.data.entity.CurrencyRate;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class RatesListView extends VerticalLayout {
    Grid<CurrencyRate> grid = new Grid<>(CurrencyRate.class, false);
    Text tableTitle = new Text("");
    private Currency currency;

    public RatesListView(List<CurrencyRate> currencyRates) {
        addClassName("currency-grid");
        setSizeFull();
        configureGrid();
        add(tableTitle, getContent());
        updateList(currencyRates);
    }

    private void updateList(List<CurrencyRate> currencyRates) {
        grid.setItems(currencyRates);
    }

    private void configureGrid() {
        grid.addClassName("currency-grid");
        grid.setSizeFull();
        grid.setColumns("date");
        grid.addColumn(CurrencyRate::getSelectedAmount).setHeader("Exchange Rate");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    public void setCurrencyRates(Currency currency) {
        this.currency = currency;
        if (currency != null) {
            List<CurrencyRate> currencyRates = currency.getCurrencyRates();
            if (currencyRates.isEmpty()) {
                tableTitle.setText("No Currency rates were found for " + currency.getAbbreviation());
                grid.setVisible(false);
            }
            else {
                String abbreviation = currency.getAbbreviation();
                int comparisonAmount = currency.getCurrencyRates().get(0).getComparisonAmount();
                String comparisonCurrency = currency.getCurrencyRates().get(0).getComparisonCurrency();
                tableTitle.setText("Exchange rates for " + abbreviation + " (in comparison with " +
                        comparisonAmount + " " + comparisonCurrency + ")");
                updateList(currencyRates);
                grid.setVisible(true);
            }
        }
    }
}
