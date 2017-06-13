package ru.bit.mybit.client.model.exchanges;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class ExchangeInfo implements IsSerializable {
    private static int COUNTER = 0;
    private String name;
    private String last;
    private String high;
    private BigDecimal ask;
    private BigDecimal bid;
    private BigDecimal diff;

    public ExchangeInfo() {
    }

    public ExchangeInfo(String name, String last, String high, String bid, String buy) {
        this();
        this.name = name;
    }

    public ExchangeInfo(String name, BigDecimal bid, BigDecimal ask, BigDecimal diff) {
        this();
        this.name = name;
        this.bid = bid;
        this.ask = ask;
        this.diff = diff;
    }

    public String getName() {
        return name;
    }

    public String getLast() {
        return last;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getDiff() {
        return diff;
    }


}
