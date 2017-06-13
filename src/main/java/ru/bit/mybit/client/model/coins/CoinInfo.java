package ru.bit.mybit.client.model.coins;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CoinInfo implements IsSerializable {
    private String name;

    private String askPrice;
    private String bidPrice;

    public CoinInfo() {
    }

    public CoinInfo(String name,String askPrice , String bidPrice) {

        this();
        this.name = name;
        this.askPrice = askPrice;
        this.bidPrice = bidPrice;
    }

    public String getName() {
        return name;
    }
    public String getAskPrice() {
        return askPrice;
    }
    public String getBidPrice() {
        return bidPrice;
    }

}
