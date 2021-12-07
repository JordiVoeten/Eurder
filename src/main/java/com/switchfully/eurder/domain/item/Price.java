package com.switchfully.eurder.domain.item;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Price {
    private BigDecimal value;
    private Currency currency;

    public Price(double value, Currency currency) {
        this.value = new BigDecimal(value);
        this.currency = currency;
    }

    public static Price calculatePrice(Price price, int amount) {
        BigDecimal count = new BigDecimal(amount);
        return new Price(count.multiply(price.getValue()).doubleValue(), price.getCurrency());
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
}
