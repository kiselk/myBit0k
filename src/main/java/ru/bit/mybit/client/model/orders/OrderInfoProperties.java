package ru.bit.mybit.client.model.orders;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface OrderInfoProperties extends PropertyAccess<OrderInfo> {

    @Path("id")
    ModelKeyProvider<OrderInfo> key();

    LabelProvider<OrderInfo> idLabel();

    ValueProvider<OrderInfo, Integer> id();

    @Path("type")
    LabelProvider<OrderInfo> typeLabel();

    ValueProvider<OrderInfo, String> type();

    @Path("rate")
    LabelProvider<OrderInfo> rateLabel();

    ValueProvider<OrderInfo, String> rate();

    @Path("amount")
    LabelProvider<OrderInfo> amountLabel();

    ValueProvider<OrderInfo, String> amount();

    @Path("total")
    LabelProvider<OrderInfo> totalLabel();

    ValueProvider<OrderInfo, String> total();

}
