package ru.bit.mybit.client.model.exchanges;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import java.math.BigDecimal;

public interface ExchangeInfoProperties extends PropertyAccess<ExchangeInfo> {


    @Path("name")
    ModelKeyProvider<ExchangeInfo> key();

    LabelProvider<ExchangeInfo> nameLabel();

    ValueProvider<ExchangeInfo, String> name();

    @Path("ask")
    LabelProvider<ExchangeInfo> askLabel();

    ValueProvider<ExchangeInfo, BigDecimal> ask();

    @Path("bid")
    LabelProvider<ExchangeInfo> bidLabel();

    ValueProvider<ExchangeInfo, BigDecimal> bid();

    @Path("diff")
    LabelProvider<ExchangeInfo> diffLabel();

    ValueProvider<ExchangeInfo, BigDecimal> diff();

}
