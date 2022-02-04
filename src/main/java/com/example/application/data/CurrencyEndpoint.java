package com.example.application.data;

import com.example.application.data.entity.Currency;
import com.example.application.data.entity.CurrencyRate;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.fusion.Endpoint;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
@AnonymousAllowed
public class CurrencyEndpoint {

    public static WebClient client;

    public CurrencyEndpoint(WebClient.Builder builder) {
        this.client = builder.baseUrl("https://www.lb.lt").build();
    }

    public static ArrayList<Currency> getCurrencies() {
        String response = (client.get().uri(uri ->
                        uri.path("/webservices/FxRates/FxRates.asmx/getCurrencyList")
                                .build())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .toEntity(String.class)
                .block())
                .getBody();

        ArrayList<Currency> currencies = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            db = dbf.newDocumentBuilder();
            Document dom = db.parse(is);
            Element docEle = dom.getDocumentElement();
            NodeList nl = docEle.getChildNodes();
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    if (el.getNodeName().contains("CcyNtry")) {
                        String abbreviation = el.getElementsByTagName("Ccy").item(0).getTextContent();
                        String nameLt = el.getElementsByTagName("CcyNm").item(0).getTextContent();
                        String nameEn = el.getElementsByTagName("CcyNm").item(1).getTextContent();
                        String number = el.getElementsByTagName("CcyNbr").item(0).getTextContent();
                        int units = Integer.parseInt(el.getElementsByTagName("CcyMnrUnts").item(0).getTextContent());
                        currencies.add(new Currency(abbreviation, nameLt, nameEn, number, units));
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return currencies;
    }

    public static ArrayList<CurrencyRate> getCurrencyRates() {
        String response = (client.get().uri(uri ->
                        uri.path("/webservices/FxRates/FxRates.asmx/getCurrentFxRates")
                                .queryParam("tp", "EU")
                                .build())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .toEntity(String.class)
                .block())
                .getBody();
        ArrayList<CurrencyRate> currencyRates = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(is);
            Element docEle = dom.getDocumentElement();
            NodeList nl = docEle.getChildNodes();
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    if (el.getNodeName().contains("FxRate")) {

                        String type = el.getElementsByTagName("Tp").item(0).getTextContent();
                        String date = el.getElementsByTagName("Dt").item(0).getTextContent();
                        String comparisonCurrency = el.getElementsByTagName("Ccy").item(0).getTextContent();
                        int comparisonAmount = Integer.parseInt(el.getElementsByTagName("Amt").item(0).getTextContent());
                        String selectedCurrency = el.getElementsByTagName("Ccy").item(1).getTextContent();
                        double selectedAmount = Double.parseDouble(el.getElementsByTagName("Amt").item(1).getTextContent());
                        currencyRates.add(new CurrencyRate(type, date, comparisonCurrency, comparisonAmount, selectedCurrency, selectedAmount));
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return currencyRates;
    }

    public static ArrayList<CurrencyRate> getCurrencyRatesForTimePeriod(String selectedAbbreviation) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        LocalDate fromDate = LocalDate.now().minusYears(1);

        String response = (client.get().uri(uri ->
                        uri.path("/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency")
                                .queryParam("tp", "EU")
                                .queryParam("ccy", selectedAbbreviation)
                                .queryParam("dtFrom", fromDate)
                                .queryParam("dtTo", currentDate)
                                .build())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .toEntity(String.class)
                .block())
                .getBody();

        ArrayList<CurrencyRate> currencyRates = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(is);
            Element docEle = dom.getDocumentElement();
            NodeList nl = docEle.getChildNodes();
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    if (el.getNodeName().contains("FxRate")) {

                        String type = el.getElementsByTagName("Tp").item(0).getTextContent();
                        String date = el.getElementsByTagName("Dt").item(0).getTextContent();
                        String comparisonCurrency = el.getElementsByTagName("Ccy").item(0).getTextContent();
                        int comparisonAmount = Integer.parseInt(el.getElementsByTagName("Amt").item(0).getTextContent());
                        String selectedCurrency = el.getElementsByTagName("Ccy").item(1).getTextContent();
                        double selectedAmount = Double.parseDouble(el.getElementsByTagName("Amt").item(1).getTextContent());

                        currencyRates.add(new CurrencyRate(type, date, comparisonCurrency, comparisonAmount, selectedCurrency, selectedAmount));
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return currencyRates;
    }
    public static List<CurrencyRate> mapCurrencyRatesWithCurrencies(
            List<Currency> currencies,
            List<CurrencyRate> currencyRates
    ) {
        for (Currency currency : currencies) {
            currencyRates.stream().map(rate -> {
                if (rate.getSelectedCurrency().equals(currency.getAbbreviation())) {
                    rate.setCurrency(currency);
                }
                return rate;
            }).collect(Collectors.toList());
        }
        return currencyRates;
    }

    public static List<CurrencyRate> mapCurrencyRatesWithCurrency(
            Currency currency,
            List<CurrencyRate> currencyRates
    ) {
            try {
                currencyRates.stream().map(rate -> {
                    rate.setCurrency(currency);
                    return rate;
                }).collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return currencyRates;
    }
}
