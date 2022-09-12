package com.kvizss.tinkoffservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TickersDto {
    private List<String> tickers;
}
