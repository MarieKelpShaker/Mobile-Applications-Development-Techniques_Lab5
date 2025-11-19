package com.example.lab5_rimantek;

import java.util.Date;
import java.util.Locale;


public class CurrencyRate {
    private final String code; // e.g. "EUR"
    private final String name; // e.g. "Euro"
    private final double rate; // e.g. 0.859574 (1 USD -> EUR)
    private final Date publishedAt; // publication date for this item, if present


    public CurrencyRate(String code, String name, double rate, Date publishedAt) {
        this.code = code;
        this.name = name;
        this.rate = rate;
        this.publishedAt = publishedAt;
    }


    public String getCode() { return code; }
    public String getName() { return name; }
    public double getRate() { return rate; }
    public Date getPublishedAt() { return publishedAt; }


    /** Used by default list filtering when needed. */
    @Override
    public String toString() {
        return String.format(Locale.US, "%s – %.6f (%s)", code, rate, name);
    }
}