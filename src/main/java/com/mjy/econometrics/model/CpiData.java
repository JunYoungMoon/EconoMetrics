package com.mjy.econometrics.model;

import javax.persistence.*;

@Entity
@Table(name = "cpi_data")
public class CpiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private java.sql.Date date;

    @Column(name = "cpi_value")
    private Double cpiValue;

    // Getter, Setter, 생성자
}
