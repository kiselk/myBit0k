package ru.bit.mybit.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.bit.mybit.client.model.coins.CoinInfoList;
import ru.bit.mybit.client.model.compare.CompareInfoList;
import ru.bit.mybit.client.model.exchanges.ExchangeInfo;
import ru.bit.mybit.client.model.exchanges.ExchangeInfoList;
import ru.bit.mybit.client.model.orders.OrderInfoList;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface MyBitService extends RemoteService {
    OrderInfoList getOrderInfoList() throws IllegalArgumentException;

    CoinInfoList getCoinInfoList() throws IllegalArgumentException;

    ExchangeInfoList getExchangeInfoList() throws IllegalArgumentException;

    CompareInfoList getCompareInfoList() throws IllegalArgumentException;

    String toggleBot() throws IllegalArgumentException;

    String setThreshold(Double threshold) throws IllegalArgumentException;

    String getThreshold() throws IllegalArgumentException;

    public static class App {
        private static MyBitServiceAsync ourInstance = GWT.create(MyBitService.class);

        public static synchronized MyBitServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
