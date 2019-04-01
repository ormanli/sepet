package com.serdarormanli.sepet.server.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ReadingStatistic {
    private BigDecimal minimum;
    private BigDecimal maximum;
    private BigDecimal average;
    private BigDecimal median;
}
