package com.serdarormanli.sepet.server.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Data
@Embeddable
public class ReadingKey implements Serializable {
    @NotNull
    private Instant time;
    @NotEmpty
    private String machineId;
}
