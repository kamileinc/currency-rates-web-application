package com.example.application.views.calculator;

import com.example.application.data.entity.Currency;
import com.example.application.data.service.CurrencyService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.DecimalFormat;
import java.util.List;

@Route(value = "calculator", layout = MainLayout.class)
@PageTitle("Calculator")
public class CalculatorView extends VerticalLayout {

    private final CurrencyService service;
    NumberField amount = new NumberField("Amount");
    ComboBox<Currency> currency = new ComboBox<>("Currency");
    Button calculate = new Button("Calculate");
    Text result = new Text("");

    public CalculatorView(CurrencyService service) {
        this.service = service;
        List<Currency> currencies = service.searchCurrencyForCalculator();
        addClassName("calculator-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        currency.setItems(currencies);
        currency.setItemLabelGenerator(Currency::getAbbreviation);
        currency.setWidth("200px");
        amount.setWidth("200px");

        add(amount,
                currency,
                createButtonsLayout(),
                result
        );
    }

    private HorizontalLayout createButtonsLayout() {
        calculate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        calculate.addClickListener(event -> calculateAmount());

        calculate.addClickShortcut(Key.ENTER);

        return new HorizontalLayout(calculate);
    }

    private void calculateAmount() {
        try {
            Double amountDouble = amount.getValue();

            if (amountDouble<0) {
                throw new RuntimeException();
            }

            Double currencyRateDouble = Double.parseDouble((currency.getValue().getCurrencyRate()));
            Double productOfMultiplication = amountDouble * currencyRateDouble;
            result.setText(prepareResultText(amountDouble, productOfMultiplication));
        }
        catch (Exception e) {
            result.setText("Theres something wrong with the values. Please try again with different ones.");
        }
    }
    private String prepareResultText(Double amount, Double productOfMultiplication) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(5);
        String formattedAmount = df.format(amount);
        String abbreviation = currency.getValue().getAbbreviation();
        String formattedProduct = df.format(productOfMultiplication);
        String formattedComparisonCurrency = currency.getValue().getCurrencyRates().get(0).getComparisonCurrency();
        String formattedCurrencyRate = df.format(Double.parseDouble(currency.getValue().getCurrencyRate()));

        String resultText = formattedAmount + " " + abbreviation + " = " +  formattedProduct + " " +
                formattedComparisonCurrency + " with " + formattedCurrencyRate + " rate.";

        return resultText;
    }
}
