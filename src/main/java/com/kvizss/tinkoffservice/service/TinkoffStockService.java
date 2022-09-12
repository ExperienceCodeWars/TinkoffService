package com.kvizss.tinkoffservice.service;

import com.kvizss.tinkoffservice.dto.*;
import com.kvizss.tinkoffservice.exception.StockNotFoundException;
import com.kvizss.tinkoffservice.dto.AllStockInfo;
import com.kvizss.tinkoffservice.model.Currency;
import com.kvizss.tinkoffservice.model.MarketInstrument;
import com.kvizss.tinkoffservice.model.Stock;
import com.kvizss.tinkoffservice.model.StockPrice;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TinkoffStockService implements StockService {
    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentTicker(String ticker) {
        var context = openApi.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        var cf = getMarketInstrumentTicker(ticker);
        var list = cf.join().getInstruments();
        if (list.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %S not found", ticker));
        }

        var item = list.get(0);
        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                "TINKOFF");
    }

    @Override
    public StocksDto getStocksByTickers(TickersDto tickers) {
        List<CompletableFuture<MarketInstrumentList>> marketInstrument = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> marketInstrument.add(getMarketInstrumentTicker(ticker)));
        List<Stock> stocks = marketInstrument.stream().
                map(CompletableFuture::join).
                map(mi -> {
                    if (!mi.getInstruments().isEmpty()) {
                        return mi.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()), "TINKOFF")
                )
                .collect(Collectors.toList());
        return new StocksDto(stocks);
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> geOrderBookByFigi(String figi) {
        var orderBook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("Getting price {} from Tinkoff", figi);
        return orderBook;
    }

    @Override
    public StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto) {
        long start = System.currentTimeMillis();
        List<CompletableFuture<Optional<Orderbook>>> ordersBooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> ordersBooks.add(geOrderBookByFigi(figi)));
        var listPrices = ordersBooks.stream()
                .map(CompletableFuture::join)
                .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock not found")))
                .map(orderbook -> new StockPrice(orderbook.getFigi(), orderbook.getLastPrice().doubleValue()))
                .collect(Collectors.toList());
        log.info("Time - {}", System.currentTimeMillis() - start);
        return new StocksPricesDto(listPrices);
    }

    @Override
    public AllStockInfo getAllInfoStockByTicker(String ticker) {
        var cf = getMarketInstrumentTicker(ticker);
        var list = cf.join().getInstruments();
        if (list.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %S not found", ticker));
        }

        var item = list.get(0);
        return new AllStockInfo()
                .setTicker(item.getTicker())
                .setFigi(item.getFigi())
                .setName(item.getName())
                .setType(item.getType().getValue())
                .setCurrency(Currency.valueOf(item.getCurrency().getValue()))
                .setPrice(getPricesStocksByFigies(new FigiesDto()
                        .setFigies(Collections.singletonList(item.getFigi()))).getPrices().get(0).getPrice())
                .setSource("TINKOFF")
                .setLot(item.getLot());
    }

    @SneakyThrows
    @Override
    public MarketInstrumentDto getMarketStocks() {
        var marketInstrumentListCompletableFuture = openApi.getMarketContext().getMarketStocks();
        log.info("Getting market instrument list from Tinkoff");

        var marketInstruments = marketInstrumentListCompletableFuture.get().getInstruments()
                .stream()
                .map(marketInstrument -> new MarketInstrument(marketInstrument.getFigi(),
                        marketInstrument.getTicker(),
                        marketInstrument.getIsin(),
                        marketInstrument.getMinPriceIncrement(),
                        marketInstrument.getLot(),
                        marketInstrument.getMinQuantity(),
                        marketInstrument.getCurrency(),
                        marketInstrument.getName(),
                        marketInstrument.getType()))
                .collect(Collectors.toList());

        return new MarketInstrumentDto().setMarketInstruments(marketInstruments);
    }


}
