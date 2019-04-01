package com.serdarormanli.sepet.server.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "readings")
public class Reading {
    @EmbeddedId
    private ReadingKey readingKey;
    @NotNull
    private BigDecimal temperature;
}
