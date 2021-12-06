package com.switchfully.eurder.domain.item;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Price {
    private BigDecimal value;
    private Currency currency;

    public Price(double value, Currency currency) {
        this.value = new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN);
        this.currency = currency;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }
}
