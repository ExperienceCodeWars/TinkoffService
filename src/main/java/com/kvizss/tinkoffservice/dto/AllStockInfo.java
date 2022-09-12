package com.kvizss.tinkoffservice.dto;

import com.kvizss.tinkoffservice.model.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AllStockInfo {
    String ticker;
    String figi;
    String name;
    String type;
    Currency currency;
    String source;
    Double price;
    Integer lot;
}
