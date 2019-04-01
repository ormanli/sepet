package com.serdarormanli.sepet.server.model;

import java.math.BigDecimal;

public interface Statistic {
    String getMachineId();

    BigDecimal getMinimum();

    BigDecimal getMaximum();

    BigDecimal getAverage();

    BigDecimal getMedian();
}
