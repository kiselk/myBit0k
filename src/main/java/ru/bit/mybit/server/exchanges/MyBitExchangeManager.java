package ru.bit.mybit.server.exchanges;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyBitExchangeManager {


    public static MyBitExchangeManager instance;
    private ArrayList<Exchange> exchanges;

    private MyBitExchangeManager() {
    }

    public static MyBitExchangeManager getInstance() {
        if (instance == null) instance = new MyBitExchangeManager();
        return instance;
    }

    public ArrayList<Exchange> getAllExchanges() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (exchanges == null) {
            exchanges = new ArrayList<Exchange>();
            try {
                MyBitExchangeFinder finder = new MyBitExchangeFinder();
                List<Class<? extends Exchange>> list = MyBitExchangeFinder.findNew("org.knowm.xchange");
                for (Class<?> someClass : list) {
                    Method[] methodList = someClass.getMethods();
                    for (Method method : methodList) {
                        if (method.getName().equals("getMarketDataService")) {
                            try {
                                Class<?> c = Class.forName(someClass.getName());
                                Exchange ex = ExchangeFactory.INSTANCE.createExchange(someClass.getName());
                                ex.remoteInit();
                                exchanges.add(ex);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return exchanges;
    }
}
