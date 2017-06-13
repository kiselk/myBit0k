package ru.bit.mybit.server;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTicker;
import org.knowm.xchange.bitfinex.v1.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.bitstamp.dto.marketdata.BitstampTicker;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataServiceRaw;
import org.knowm.xchange.bittrex.v1.dto.marketdata.BittrexTicker;
import org.knowm.xchange.bittrex.v1.service.BittrexMarketDataServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.kraken.dto.marketdata.KrakenTicker;
import org.knowm.xchange.kraken.service.KrakenMarketDataServiceRaw;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinTicker;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinTickerResponse;
import org.knowm.xchange.okcoin.service.OkCoinMarketDataServiceRaw;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexPublicTrade;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexTicker;
import org.knowm.xchange.poloniex.dto.trade.PoloniexLimitOrder;
import org.knowm.xchange.poloniex.service.PoloniexMarketDataServiceRaw;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import ru.bit.mybit.client.MyBitService;
import ru.bit.mybit.client.model.coins.CoinInfo;
import ru.bit.mybit.client.model.coins.CoinInfoList;
import ru.bit.mybit.client.model.compare.CompareInfo;
import ru.bit.mybit.client.model.compare.CompareInfoList;
import ru.bit.mybit.client.model.exchanges.ExchangeInfo;
import ru.bit.mybit.client.model.exchanges.ExchangeInfoList;
import ru.bit.mybit.client.model.orders.OrderInfo;
import ru.bit.mybit.client.model.orders.OrderInfoList;
import ru.bit.mybit.server.exchanges.MyBitExchangeManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.bit.mybit.server.pollers.CompareInfoListPoller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MyBitServiceImpl extends RemoteServiceServlet implements
        MyBitService {
    private static CurrencyPair currencyPair;
    private static BigDecimal xmrBuyRate;

    public CoinInfoList getCoinInfoList() {
        CoinInfoList coins = new CoinInfoList();
        ArrayList<Currency> list = new ArrayList<Currency>();
        list.add(Currency.BTC);
        list.add(Currency.ETH);
        list.add(Currency.XRP);
        list.add(Currency.ETC);
        list.add(Currency.LTC);
        list.add(Currency.STR);
        list.add(Currency.ZEC);
        list.add(Currency.XMR);
        list.add(Currency.DASH);
        list.add(Currency.REP);
        list.add(Currency.NXT);


        for (Currency currency : list)
            coins.add(getCoinInfo(new CurrencyPair(currency, Currency.USDT)));
        //coins.add(getCoinInfo(new CurrencyPair(Currency.XRP, Currency.USDT)));
        //coins.add(getCoinInfo(new CurrencyPair(Currency.ETH, Currency.USDT)));
        return coins;

    }

    private CoinInfo getCoinInfo(CurrencyPair currencyPair) {
        BigDecimal askPrice = new BigDecimal(0);
        BigDecimal bidPrice = new BigDecimal(0);
        MarketDataService marketDataService = MyBitServer.getInstance().getPolo().getMarketDataService();
        try {
            int asks = 0;
            int bids = 0;
            BigDecimal totalPrice = new BigDecimal(0);
            PoloniexPublicTrade[] trades = ((PoloniexMarketDataServiceRaw) marketDataService).getPoloniexPublicTrades(currencyPair);
            String trace = "";
            for (PoloniexPublicTrade trade : trades) {
                trace += trade.toString();
                if (trade.getType().equals("sell")) {
                    askPrice = askPrice.add(trade.getRate());
                    asks++;
                }
                if (trade.getType().equals("buy")) {
                    bidPrice = bidPrice.add(trade.getRate());
                    bids++;
                }
            }
            askPrice = askPrice.divide(BigDecimal.valueOf(asks), BigDecimal.ROUND_CEILING);
            bidPrice = bidPrice.divide(new BigDecimal(bids), BigDecimal.ROUND_CEILING);

            System.out.println("ASK: " + askPrice + " BID: " + bidPrice);
        } catch (Exception e) {
            System.out.println("ex:" + e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                System.out.println(el);
            }
        }
        return new CoinInfo(currencyPair.toString(), askPrice.toString(), bidPrice.toString());
    }

    ExchangeInfoList list = null;

    public ExchangeInfoList getExchangeInfoList() {
        if (list == null) list = generateExchangeInfoList();
        return list;

    }

    public CompareInfoList getCompareInfoList() {
        return CompareInfoListPoller.getInstance().getCompareInfoList();

    }

    public String toggleBot() {
        MyBitServer.getInstance().getBot().toggleBot();

        return MyBitServer.getInstance().getBot().isEnabled();
    }

    public String setThreshold(Double threshold) {
        MyBitServer.getInstance().getBot().setThreshold(threshold);
        return getThreshold();

    }

    public String getThreshold() {
        return MyBitServer.getInstance().getBot().getThreshold().toString();
    }

    public ExchangeInfoList generateExchangeInfoList() {
        ExchangeInfoList output = new ExchangeInfoList();
        ArrayList<Currency> list = new ArrayList<Currency>();
        list.add(Currency.BTC);

        try {
            ArrayList<Exchange> exchanges = MyBitExchangeManager.getInstance().getAllExchanges();
            for (Exchange exchange : exchanges)
                try {
                    MarketDataService service = exchange.getMarketDataService();
                    CurrencyPair pair1 = new CurrencyPair(Currency.BTC, Currency.USDT);
                    CurrencyPair pair2 = new CurrencyPair(Currency.BTC, Currency.USD);

                    BigDecimal bid = new BigDecimal(0);
                    BigDecimal ask = new BigDecimal(0);
                    BigDecimal diff = new BigDecimal(0);

                    Ticker ticker = null;

                    String name = exchange.getDefaultExchangeSpecification().getExchangeName();
                    System.out.println(name);
                    ArrayList<String> exs = new ArrayList<String>();
                    Collections.addAll(exs, new String[]{"BitFinex", "Bitstamp"});
                    if (exs.contains(name))

                        for (CurrencyPair pair : exchange.getExchangeSymbols()) {
                            try {
                                if (pair.equals(pair1)) {
                                    ticker = service.getTicker(pair1);
                                    bid = ticker.getBid();
                                    ask = ticker.getAsk();
                                    diff = bid.subtract(ask);
                                }
                                if (pair.equals(pair2)) {
                                    ticker = service.getTicker(pair2);
                                    bid = ticker.getBid();
                                    ask = ticker.getAsk();
                                    diff = bid.subtract(ask);
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }

                    if (!bid.equals(new BigDecimal(0))) {
                        ExchangeInfo exchangeInfo = new ExchangeInfo(name, bid, ask, diff);
                        output.add(exchangeInfo);
                        System.out.println(exchangeInfo.toString());
                    }
                } catch (Exception e) {
                    System.out.println("ex:" + e.getMessage());
                }
        } catch (Exception e) {
        }
        return output;
    }


    public OrderInfoList getOrderInfoList() {
        CurrencyPair currencyPair = new CurrencyPair(Currency.BTC, Currency.USDT);
        OrderInfoList output = new OrderInfoList();

        MarketDataService marketDataService = MyBitServer.getInstance().getPolo().getMarketDataService();
        try {
            PoloniexPublicTrade[] trades = ((PoloniexMarketDataServiceRaw) marketDataService).getPoloniexPublicTrades(currencyPair);
            for (PoloniexPublicTrade trade : trades) {
                output.add(new OrderInfo(Integer.valueOf(trade.getTradeID()), trade.getType(), trade.getRate().toString(), trade.getAmount().toString(), trade.getTotal().toString()));
            }
        } catch (Exception e) {
            System.out.println("ex:" + e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                System.out.println(el);
            }
        }
        return output;
    }

    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
                ">", "&gt;");
    }
}
