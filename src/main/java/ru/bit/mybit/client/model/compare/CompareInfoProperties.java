package ru.bit.mybit.client.model.compare;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import java.math.BigDecimal;
import java.util.Date;

public interface CompareInfoProperties extends PropertyAccess<CompareInfo> {

    @Path("stamp")
    ModelKeyProvider<CompareInfo> key();
    LabelProvider<CompareInfo> stampLabel();
    ValueProvider<CompareInfo, Long> stamp();

    @Path("time")
    LabelProvider<CompareInfo> TimeLabel();
    ValueProvider<CompareInfo, String> time();

    @Path("bitFinexShortAsk")
    LabelProvider<CompareInfo> bitFinexShortAskLabel();
    ValueProvider<CompareInfo, BigDecimal> bitFinexShortAsk();

    @Path("bitstampLongBid")
    LabelProvider<CompareInfo> bitstampLongBidLabel();
    ValueProvider<CompareInfo, BigDecimal> bitstampLongBid();

    @Path("poloLongBid")
    LabelProvider<CompareInfo> poloLongBidLabel();
    ValueProvider<CompareInfo, BigDecimal> poloLongBid();

    @Path("bittrexLongBid")
    LabelProvider<CompareInfo> bittrexLongBidLabel();
    ValueProvider<CompareInfo, BigDecimal> bittrexLongBid();

    @Path("krakenLongBid")
    LabelProvider<CompareInfo> krakenLongBidLabel();
    ValueProvider<CompareInfo, BigDecimal> krakenLongBid();

}
