package com.serdarormanli.sepet.server.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistic {
    private String machineId;
    private BigDecimal minimum;
    private BigDecimal maximum;
    private BigDecimal average;
    private BigDecimal median;
}
