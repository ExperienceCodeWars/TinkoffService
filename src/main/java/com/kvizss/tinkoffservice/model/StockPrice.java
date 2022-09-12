package com.kvizss.tinkoffservice.model;

import lombok.Value;

@Value
public class StockPrice {
    String figi;
    Double price;
}
