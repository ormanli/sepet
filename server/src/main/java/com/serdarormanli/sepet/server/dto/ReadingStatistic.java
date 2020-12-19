package com.serdarormanli.sepet.server.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ReadingStatistic {
    String machineId;
    BigDecimal minimum;
    BigDecimal maximum;
    BigDecimal average;
    BigDecimal median;
}
