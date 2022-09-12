package com.kvizss.tinkoffservice.dto;

import com.kvizss.tinkoffservice.model.StockPrice;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@AllArgsConstructor
@Value
public class StocksPricesDto {
    private List<StockPrice> prices;
}
