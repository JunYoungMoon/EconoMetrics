package com.mjy.econometrics.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cpi_data")
public class CpiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "cpi_value")
    private BigDecimal cpiValue;

    public CpiData() {
    }

    public CpiData(LocalDate date, BigDecimal cpiValue) {
        this.date = date;
        this.cpiValue = cpiValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getCpiValue() {
        return cpiValue;
    }

    public void setCpiValue(BigDecimal cpiValue) {
        this.cpiValue = cpiValue;
    }

    @Override
    public String toString() {
        return "CpiData{" +
                "id=" + id +
                ", date=" + date +
                ", cpiValue=" + cpiValue +
                '}';
    }
}
