package com.serdarormanli.sepet.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table("readings")
public class Reading implements Persistable<String> {
    @Id
    private String id;
    @NotNull
    private BigDecimal temperature;
    @NotNull
    private Instant time;
    @NotEmpty
    private String machineId;
    @Transient
    private boolean save = true;

    @Override
    public boolean isNew() {
        return this.save;
    }
}
