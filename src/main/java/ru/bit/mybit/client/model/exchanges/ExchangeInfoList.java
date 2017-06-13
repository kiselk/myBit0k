package ru.bit.mybit.client.model.exchanges;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExchangeInfoList implements IsSerializable {
    public ArrayList<ExchangeInfo> getExchangeInfos() {
        return exchangeInfos;
    }

    public void setExchangeInfos(ArrayList<ExchangeInfo> exchangeInfos) {
        this.exchangeInfos = exchangeInfos;
    }

    ArrayList<ExchangeInfo> exchangeInfos = new ArrayList<ExchangeInfo>();

    public ExchangeInfoList() {

    }

    public void add(ExchangeInfo exchangeInfo) {
        this.exchangeInfos.add(exchangeInfo);
    }

    public BigDecimal getPoloDiff(BigDecimal dec) {
        return new BigDecimal(0);
    }

    public ExchangeInfo getExchangeByName(String name) {
        for (ExchangeInfo exchangeInfo : this.getExchangeInfos()) {
            if (exchangeInfo.getName().equals(name)) {
                return exchangeInfo;
            }
        }
        return null;
    }


}
