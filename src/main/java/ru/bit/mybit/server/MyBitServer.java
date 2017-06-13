package ru.bit.mybit.server;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bittrex.v1.BittrexExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.okcoin.OkCoinExchange;
import org.knowm.xchange.poloniex.Poloniex;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MyBitServer {

    private static MyBitServer ourInstance;
    String apiKey = "ETGFGL49-SQWO5LYG-6OAL2V2G-KWNVU8GZ";
    String apiSecret = "92441ba24f472dfbafac27ce249691e7adebc25eff30d5719237c6ba5501031991164d6c1d26c3510c2b11a0e97b7d191623b95f7be72ac3e3c28cd547b8c87e";

    Exchange polo;
    Exchange bitFinex;
    Exchange bittrex;
    Exchange bitstamp;
    Exchange okcoin;
    Exchange kraken;
    MyBitBot bot;

    MyBitServer() {
        ApiContextInitializer.init();
        bot = new MyBitBot();

        ExchangeSpecification poloSpec = new ExchangeSpecification(PoloniexExchange.class);
        ExchangeSpecification bitFinexSpec = new ExchangeSpecification(BitfinexExchange.class);
        ExchangeSpecification bitstampSpec = new ExchangeSpecification(BitstampExchange.class);
        ExchangeSpecification okcoinSpec = new ExchangeSpecification(OkCoinExchange.class);
        ExchangeSpecification krakenSpec = new ExchangeSpecification(KrakenExchange.class);
        ExchangeSpecification bittrexSprec = new ExchangeSpecification(BittrexExchange.class);

        okcoinSpec.setExchangeSpecificParametersItem("Use_Intl", true);
        poloSpec.setApiKey(apiKey);
        poloSpec.setSecretKey(apiSecret);

        bittrex = ExchangeFactory.INSTANCE.createExchange(bittrexSprec);
        kraken = ExchangeFactory.INSTANCE.createExchange(krakenSpec);
        bitstamp = ExchangeFactory.INSTANCE.createExchange(bitstampSpec);
        bitFinex = ExchangeFactory.INSTANCE.createExchange(bitFinexSpec);
        okcoin = ExchangeFactory.INSTANCE.createExchange(okcoinSpec);
        polo = ExchangeFactory.INSTANCE.createExchange(poloSpec);


    }

    public static synchronized MyBitServer getInstance() {
        if (ourInstance == null)
            ourInstance = new MyBitServer();
        return ourInstance;
    }

    public Exchange getPolo() {
        return this.polo;
    }

    public Exchange getBitfinex() {
        return this.bitFinex;
    }

    public Exchange getBitstamp() {
        return this.bitstamp;
    }

    public Exchange getOkcoin() {
        return this.okcoin;
    }

    public Exchange getKraken() {
        return this.kraken;
    }

    public Exchange getBittrex() {
        return this.bittrex;
    }

    public MyBitBot getBot() {
        return this.bot;
    }


}
