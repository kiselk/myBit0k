package ru.bit.mybit.client.model.coins;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class CoinInfoList implements IsSerializable {
    ArrayList<CoinInfo> coinInfos = new ArrayList<CoinInfo>();

    public CoinInfoList() {

    }

    public ArrayList<CoinInfo> getCoinInfos() {
        return coinInfos;
    }

    public void setCoinInfos(ArrayList<CoinInfo> coinInfos) {
        this.coinInfos = coinInfos;
    }

    public Integer getCount() {
        return this.coinInfos.size();
    }

    public void add(CoinInfo coinInfo) {
        this.coinInfos.add(coinInfo);
    }
}
