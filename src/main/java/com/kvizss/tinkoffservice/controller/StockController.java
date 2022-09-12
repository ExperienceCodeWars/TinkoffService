package com.kvizss.tinkoffservice.controller;


import com.kvizss.tinkoffservice.dto.*;
import com.kvizss.tinkoffservice.dto.AllStockInfo;
import com.kvizss.tinkoffservice.model.Stock;
import com.kvizss.tinkoffservice.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

@RequiredArgsConstructor
@RestController
@Slf4j
public class StockController {

    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks/getStocksByTickers")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickersDto) {
        return stockService.getStocksByTickers(tickersDto);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPrices(@RequestBody FigiesDto figiesDto) {
        return stockService.getPricesStocksByFigies(figiesDto);
    }

    @GetMapping("/stock/all-info/{ticker}")
    public AllStockInfo getAllInfoStockByTicker(@PathVariable String ticker) {
        return stockService.getAllInfoStockByTicker(ticker);
    }

    @GetMapping("/stock/getInstrument")
    public MarketInstrumentDto getInstrument() {
        return stockService.getMarketStocks();
    }
}
