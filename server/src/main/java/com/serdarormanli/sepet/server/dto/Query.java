package com.serdarormanli.sepet.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

@Data
public class Query {
    @NotEmpty
    private Set<String> machineIds;
    @NotNull
    private Instant startDate;
    @NotNull
    private Instant endDate;
}
