package ru.bit.mybit.client.model.compare;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

public class CompareInfo implements IsSerializable {
    private Long stamp;

    private String time;

    private BigDecimal bitFinexShortAsk;
    private BigDecimal bitstampLongBid;
    private BigDecimal poloLongBid;
    private BigDecimal bittrexLongBid;
    private BigDecimal krakenLongBid;

    public CompareInfo() {
    }

    public CompareInfo(Long stamp,
                       String time,
                       BigDecimal bitFinexShortAsk,
                       BigDecimal bitstampLongBid,
                       BigDecimal poloLongBid,
                       BigDecimal bittrexLongBid,
                       BigDecimal krakenLongBid) {
        this();
        this.stamp = stamp;
        this.time = time;
        this.bitFinexShortAsk = bitFinexShortAsk;
        this.bitstampLongBid = bitstampLongBid;
        this.poloLongBid = poloLongBid;
        this.bittrexLongBid = bittrexLongBid;
        this.krakenLongBid = krakenLongBid;


    }

    public Long getStamp() {
        return stamp;
    }

    public String getTime() {
        return time;
    }

    public BigDecimal getBitFinexShortAsk() {
        return bitFinexShortAsk;
    }

    public BigDecimal getBitstampLongBid() {
        return bitstampLongBid;
    }

    public BigDecimal getPoloLongBid() {
        return poloLongBid;
    }

    public BigDecimal getBittrexLongBid() {
        return bittrexLongBid;
    }

    public BigDecimal getKrakenLongBid() {
        return krakenLongBid;
    }



    public String bitFinexShortAskLabel() {
        return "BITFINEX";
    }



}
