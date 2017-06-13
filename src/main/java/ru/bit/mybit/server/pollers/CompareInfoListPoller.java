package ru.bit.mybit.server.pollers;

import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTicker;
import org.knowm.xchange.bitfinex.v1.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.kraken.dto.marketdata.KrakenTicker;
import org.knowm.xchange.kraken.service.KrakenMarketDataServiceRaw;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexTicker;
import org.knowm.xchange.poloniex.service.PoloniexMarketDataServiceRaw;
import org.knowm.xchange.service.marketdata.MarketDataService;
import ru.bit.mybit.client.model.compare.CompareInfo;
import ru.bit.mybit.client.model.compare.CompareInfoList;
import ru.bit.mybit.server.MyBitServer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CompareInfoListPoller {

    private static CompareInfoListPoller ourInstance;
    private boolean running = false;

    CompareInfoListPoller() {
        TimerTask pollTask = new TimerTask() {
            @Override
            public void run() {
                if (!running) {
                    running = true;
                    generateCompareInfoList();
                    running = false;
                }
            }
        };
        Timer pollTimer = new Timer();
        pollTimer.scheduleAtFixedRate(pollTask, 1000, 1000);

    }

    public static synchronized CompareInfoListPoller getInstance() {
        if (ourInstance == null)
            ourInstance = new CompareInfoListPoller();
        return ourInstance;
    }

    CompareInfoList compareList = null;

    public CompareInfoList getCompareInfoList() {
        if (compareList == null) compareList = generateCompareInfoList();
        return compareList;
    }

    public CompareInfoList generateCompareInfoList() {
        //  System.out.println("Polling exchanges");
        if (compareList == null) {
            //       System.out.println("new compare list");
            compareList = new CompareInfoList();
        }
        if (compareList.getCount() >= 100) {
            //       System.out.println("removing leading position");
            compareList.getCompareInfos().remove(0);
        }
        long yourmilliseconds = System.currentTimeMillis();
        Date resultdate = new Date(yourmilliseconds);
        //   System.out.println(resultdate.toString());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(resultdate);
        CurrencyPair pair1 = new CurrencyPair(Currency.BTC, Currency.USDT);
        CurrencyPair pair2 = new CurrencyPair(Currency.BTC, Currency.USD);
        // Exchange ex = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.g());
        try {
            MarketDataService bitFinexService = MyBitServer.getInstance().getBitfinex().getMarketDataService();
            MarketDataService poloService = MyBitServer.getInstance().getPolo().getMarketDataService();
            MarketDataService bitStampService = MyBitServer.getInstance().getBitstamp().getMarketDataService();
            MarketDataService okcoinService = MyBitServer.getInstance().getOkcoin().getMarketDataService();
            MarketDataService krakenService = MyBitServer.getInstance().getKraken().getMarketDataService();
            MarketDataService bittrexService = MyBitServer.getInstance().getBittrex().getMarketDataService();
              /*  BigDecimal okcoinLongBid = null;
        try {
            OkCoinTickerResponse response =((OkCoinMarketDataServiceRaw)okcoinService).getTicker(CurrencyPair.BTC_CNY);
            OkCoinTicker okcoinTicker = response.getTicker();
            System.out.println(okcoinTicker.toString());
            okcoinLongBid = okcoinTicker.getHigh();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
            BigDecimal bitstampLongBid = new BigDecimal(2800);
          /*  try {
                Ticker bitstampTicker = bitStampService.getTicker(CurrencyPair.BTC_USD);
                bitstampLongBid = bitstampTicker.getBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            */
            BigDecimal krakenLongBid = null;
            try {

                KrakenTicker krakenTicker = ((KrakenMarketDataServiceRaw) krakenService).getKrakenTicker(pair2);
                //System.out.println(krakenTicker.toString());
                krakenLongBid = krakenTicker.getBid().getPrice();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            BigDecimal bitFinexShortAsk = null;
            try {
           /*Collection<String> pairs =  ((BitfinexMarketDataServiceRaw)bitFinexService).getBitfinexSymbols();
            for(String pair:pairs) {
                System.out.println(pair);
            }*/
                BitfinexTicker bfTicker = ((BitfinexMarketDataServiceRaw) bitFinexService).getBitfinexTicker("btcusd");
                bitFinexShortAsk = bfTicker.getAsk();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            BigDecimal poloLongBid = null;
            try {
                PoloniexTicker poloTicker = ((PoloniexMarketDataServiceRaw) poloService).getPoloniexTicker(pair1);
                poloLongBid = poloTicker.getPoloniexMarketData().getHighestBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            BigDecimal bittrexLongBid = null;
            try {
                //BittrexTicker bittrexTicker =((BittrexMarketDataServiceRaw)bittrexService).getBittrexTicker("btcusd");
                Ticker bittrexTicker = bittrexService.getTicker(pair1);
                bittrexLongBid = bittrexTicker.getBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                // System.out.println("adding to list");

                compareList.add(new CompareInfo(yourmilliseconds, time, bitFinexShortAsk, bitstampLongBid, poloLongBid, bittrexLongBid, krakenLongBid));
                // System.out.println("added to list");
                //  BigDecimal bitstamppread = bitFinexShortAsk.subtract(bitstampLongBid).divide(bitstampLongBid, BigDecimal.ROUND_CEILING);
                Double poloSpread = bitFinexShortAsk.subtract(poloLongBid).divide(poloLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double bittrexSpread = bitFinexShortAsk.subtract(bittrexLongBid).divide(bittrexLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double krakenSpread = bitFinexShortAsk.subtract(krakenLongBid).divide(krakenLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double bitstampSpread = bitFinexShortAsk.subtract(bitstampLongBid).divide(bitstampLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double thresh = MyBitServer.getInstance().getBot().getThreshold();
                if (poloSpread > thresh)
                    MyBitServer.getInstance().getBot().raisePoloAlert(poloSpread, poloLongBid, bitFinexShortAsk);
                if (poloSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownPoloAlert(poloSpread, poloLongBid, bitFinexShortAsk);

                if (bittrexSpread > thresh)
                    MyBitServer.getInstance().getBot().raiseBittrexAlert(bittrexSpread, bittrexLongBid, bitFinexShortAsk);
                if (bittrexSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownBittrexAlert(bittrexSpread, bittrexLongBid, bitFinexShortAsk);

                if (krakenSpread > thresh)
                    MyBitServer.getInstance().getBot().raiseKrakenAlert(krakenSpread, krakenLongBid, bitFinexShortAsk);
                if (krakenSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownKrakenAlert(krakenSpread, krakenLongBid, bitFinexShortAsk);

                if (bitstampSpread > thresh)
                    MyBitServer.getInstance().getBot().raiseBitstampAlert(bitstampSpread, krakenLongBid, bitFinexShortAsk);
                if (bitstampSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownBitstampAlert(bitstampSpread, bitstampLongBid, bitFinexShortAsk);

                if (bitstampSpread > thresh)
                    MyBitServer.getInstance().getBot().raiseBitstampAlert(bitstampSpread, krakenLongBid, bitFinexShortAsk);
                if (bitstampSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownBitstampAlert(bitstampSpread, bitstampLongBid, bitFinexShortAsk);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return compareList;


    }


    public CompareInfoList generateCompareInfoList2() {
        //  System.out.println("Polling exchanges");
        if (compareList == null) {
            //       System.out.println("new compare list");
            compareList = new CompareInfoList();
        }
        if (compareList.getCount() >= 500) {
            //       System.out.println("removing leading position");
            compareList.getCompareInfos().remove(0);
        }
        long yourmilliseconds = System.currentTimeMillis();
        Date resultdate = new Date(yourmilliseconds);
        //   System.out.println(resultdate.toString());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(resultdate);
        CurrencyPair pair1 = new CurrencyPair(Currency.BTC, Currency.USDT);
        CurrencyPair pair2 = new CurrencyPair(Currency.BTC, Currency.USD);
        // Exchange ex = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.g());
        try {
            MarketDataService bitFinexService = MyBitServer.getInstance().getBitfinex().getMarketDataService();
            MarketDataService poloService = MyBitServer.getInstance().getPolo().getMarketDataService();
            MarketDataService bitStampService = MyBitServer.getInstance().getBitstamp().getMarketDataService();
            MarketDataService okcoinService = MyBitServer.getInstance().getOkcoin().getMarketDataService();
            MarketDataService krakenService = MyBitServer.getInstance().getKraken().getMarketDataService();
            MarketDataService bittrexService = MyBitServer.getInstance().getBittrex().getMarketDataService();
            BigDecimal okcoinLongBid = null;
            BigDecimal krakenLongBid = null;
            BigDecimal bitFinexShortAsk = null;
            BigDecimal poloLongBid = null;
            BigDecimal bittrexLongBid = null;

            try {
                Ticker okcoinTicker = okcoinService.getTicker(pair2);
                okcoinLongBid = okcoinTicker.getBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            BigDecimal bitstampLongBid = new BigDecimal(2800);
            try {
                Ticker bitstampTicker = bitStampService.getTicker(CurrencyPair.BTC_USD);
                bitstampLongBid = bitstampTicker.getBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            try {
                Ticker krakenTicker = krakenService.getTicker(pair2);
                krakenLongBid = krakenTicker.getBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                Ticker bfTicker = bitFinexService.getTicker(pair2);
                bitFinexShortAsk = bfTicker.getAsk();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                Ticker poloTicker = poloService.getTicker(pair1);
                poloLongBid = poloTicker.getBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                Ticker bittrexTicker = bittrexService.getTicker(pair1);
                bittrexLongBid = bittrexTicker.getBid();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {

                compareList.add(new CompareInfo(yourmilliseconds, time, bitFinexShortAsk, bitstampLongBid, poloLongBid, bittrexLongBid, krakenLongBid));

                Double poloSpread = bitFinexShortAsk.subtract(poloLongBid).divide(poloLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double bittrexSpread = bitFinexShortAsk.subtract(bittrexLongBid).divide(bittrexLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double krakenSpread = bitFinexShortAsk.subtract(krakenLongBid).divide(krakenLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double bitstampSpread = bitFinexShortAsk.subtract(bitstampLongBid).divide(bitstampLongBid, BigDecimal.ROUND_CEILING).doubleValue() * 100;
                Double thresh = MyBitServer.getInstance().getBot().getThreshold();
                if (poloSpread > thresh)
                    MyBitServer.getInstance().getBot().raisePoloAlert(poloSpread, poloLongBid, bitFinexShortAsk);
                if (poloSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownPoloAlert(poloSpread, poloLongBid, bitFinexShortAsk);

                if (bittrexSpread > thresh)
                    MyBitServer.getInstance().getBot().raiseBittrexAlert(bittrexSpread, bittrexLongBid, bitFinexShortAsk);
                if (bittrexSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownBittrexAlert(bittrexSpread, bittrexLongBid, bitFinexShortAsk);

                if (krakenSpread > thresh)
                    MyBitServer.getInstance().getBot().raiseKrakenAlert(krakenSpread, krakenLongBid, bitFinexShortAsk);
                if (krakenSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownKrakenAlert(krakenSpread, krakenLongBid, bitFinexShortAsk);

                if (bitstampSpread > thresh)
                    MyBitServer.getInstance().getBot().raiseBitstampAlert(bitstampSpread, krakenLongBid, bitFinexShortAsk);
                if (bitstampSpread < thresh)
                    MyBitServer.getInstance().getBot().shutdownBitstampAlert(bitstampSpread, bitstampLongBid, bitFinexShortAsk);


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return compareList;


    }


}
