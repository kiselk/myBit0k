package ru.bit.mybit.client.model.orders;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class OrderInfoList implements IsSerializable {
    public ArrayList<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(ArrayList<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }

    ArrayList<OrderInfo> orderInfos = new ArrayList<OrderInfo>();

    public OrderInfoList() {

    }


    public Integer getCount() {
        return this.orderInfos.size();
    }

    public void add(OrderInfo orderInfos) {
        this.orderInfos.add(orderInfos);
    }
}
