package com.mjy.econometrics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PceData {
    private LocalDate date;
    private BigDecimal value;
    private BigDecimal percentage;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "PceData{" +
                "date=" + date +
                ", value=" + value +
                ", percentage=" + percentage +
                '}';
    }
}
