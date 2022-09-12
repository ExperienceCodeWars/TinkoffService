package com.kvizss.tinkoffservice.model;

import lombok.Value;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;

import java.math.BigDecimal;
@Value
public class MarketInstrument {
    String figi;
    String ticker;
    String isin;
    BigDecimal minPriceIncrement;
    Integer lot;
    Integer minQuantity;
    Currency currency;
    String name;
    InstrumentType type;
}
