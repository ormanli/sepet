package com.serdarormanli.sepet.server.controller;

import com.serdarormanli.sepet.server.dto.Query;
import com.serdarormanli.sepet.server.dto.ReadingStatistic;
import com.serdarormanli.sepet.server.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/query", produces = APPLICATION_JSON_VALUE)
public class QueryController {

    @Autowired
    private ReadingService readingService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = OK)
    public Map<String, ReadingStatistic> queryStatistics(@Valid @RequestBody Query query) {
        return readingService.queryStatistics(query);
    }

    @GetMapping(path = "machine-id")
    @ResponseStatus(value = OK)
    public List<String> distinctMachineIds() {
        return readingService.distinctMachineIds();
    }
}
