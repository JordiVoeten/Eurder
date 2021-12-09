package com.switchfully.eurder.domain.item;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Price {
    private BigDecimal value;
    private Currency currency;

    public Price() {
    }

    public Price(double value, Currency currency) {
        this.value = new BigDecimal(value);
        this.currency = currency;
    }

    public static Price calculatePrice(Price price, double amount) {
        BigDecimal convertedAmount = new BigDecimal(amount).multiply(price.getValue());
        return new Price(convertedAmount.doubleValue(), price.getCurrency());
    }

    public static Price add(Price valueOne, BigDecimal valueTwo) {
        return new Price(valueOne.getValue().add(valueTwo).doubleValue(), valueOne.getCurrency());
    }

    public void setValue(double value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
