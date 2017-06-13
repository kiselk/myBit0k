package ru.bit.mybit.client.model.orders;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrderInfo implements IsSerializable {

    private Integer id;
    private String type;
    private String rate;
    private String amount;
    private String total;

    public OrderInfo() {
    }

    public OrderInfo(int id, String type, String rate, String amount, String total) {
        this();
        this.id = id;
        this.type = type;
        this.rate = rate;
        this.amount = amount;
        this.total = total;

    }

    public Integer getId() {
        return id;
    }

    public String getRate() {
        return rate;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }

    public String getTotal() {
        return total;
    }


}
