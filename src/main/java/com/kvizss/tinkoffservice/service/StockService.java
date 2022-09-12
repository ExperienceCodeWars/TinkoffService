package com.kvizss.tinkoffservice.service;

import com.kvizss.tinkoffservice.dto.*;
import com.kvizss.tinkoffservice.dto.AllStockInfo;
import com.kvizss.tinkoffservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksDto getStocksByTickers(TickersDto tickers);

    StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto);

    AllStockInfo getAllInfoStockByTicker(String ticker);

    MarketInstrumentDto getMarketStocks();
}
