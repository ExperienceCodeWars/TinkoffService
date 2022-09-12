package com.kvizss.tinkoffservice.dto;

import com.kvizss.tinkoffservice.model.MarketInstrument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class MarketInstrumentDto {
    private List<MarketInstrument> marketInstruments;
}
