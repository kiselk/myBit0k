package ru.bit.mybit.client.model.coins;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface CoinInfoProperties extends PropertyAccess<CoinInfo> {


    @Path("name")
    ModelKeyProvider<CoinInfo> key();

    LabelProvider<CoinInfo> nameLabel();

    ValueProvider<CoinInfo, String> name();

    @Path("askPrice")
    LabelProvider<CoinInfo> askPriceLabel();

    ValueProvider<CoinInfo, String> askPrice();

    @Path("bidPrice")
    LabelProvider<CoinInfo> bidPriceLabel();

    ValueProvider<CoinInfo, String> bidPrice();

}
