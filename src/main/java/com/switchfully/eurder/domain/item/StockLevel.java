package com.switchfully.eurder.domain.item;

public enum StockLevel {
    STOCK_LOW,
    STOCK_MEDIUM,
    STOCK_HIGH;

    public static final int START_STOCK_MEDIUM = 5;
    public static final int START_STOCK_HIGH = 10;

    public static StockLevel getStockLevelForAmount(int amount) {
        if (amount < START_STOCK_MEDIUM) return STOCK_LOW;
        if (amount < START_STOCK_HIGH) return STOCK_MEDIUM;
        return STOCK_HIGH;
    }
}
