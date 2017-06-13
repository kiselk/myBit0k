package ru.bit.mybit.client.model.compare;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class CompareInfoList implements IsSerializable {
    public ArrayList<CompareInfo> getCompareInfos() {
        return compareInfos;
    }

    public void setCompareInfos(ArrayList<CompareInfo> compareInfos) {
        this.compareInfos = compareInfos;
    }

    ArrayList<CompareInfo> compareInfos = new ArrayList<CompareInfo>();

    public CompareInfoList() {

    }

    BigDecimal max = new BigDecimal(0);
    BigDecimal min = new BigDecimal(9999);

    public int getMax() {
        return max.intValue();
    }

    public int getMin() {
        return min.intValue();
    }

    public Integer getCount() {
        return this.compareInfos.size();
    }

    public void add(CompareInfo compareInfo) {
      // System.out.println(compareInfo.getStamp().toString());
        if (max.compareTo(compareInfo.getBitFinexShortAsk()) < 0)
            max = compareInfo.getBitFinexShortAsk();
        if (max.compareTo(compareInfo.getBitstampLongBid()) < 0)
            max = compareInfo.getBitstampLongBid();
        if (max.compareTo(compareInfo.getPoloLongBid()) < 0)
            max = compareInfo.getPoloLongBid();
        if (max.compareTo(compareInfo.getBittrexLongBid()) < 0)
            max = compareInfo.getBittrexLongBid();
        if (max.compareTo(compareInfo.getKrakenLongBid()) < 0)
            max = compareInfo.getKrakenLongBid();


        if (min.compareTo(compareInfo.getBitFinexShortAsk()) > 0)
            min = compareInfo.getBitFinexShortAsk();
        if (min.compareTo(compareInfo.getBitstampLongBid()) > 0)
            min = compareInfo.getBitstampLongBid();
        if (min.compareTo(compareInfo.getPoloLongBid()) > 0)
            min = compareInfo.getPoloLongBid();
        if (min.compareTo(compareInfo.getBittrexLongBid()) > 0)
            min = compareInfo.getBittrexLongBid();
        if (min.compareTo(compareInfo.getKrakenLongBid()) > 0)
            min = compareInfo.getKrakenLongBid();


        this.compareInfos.add(compareInfo);
    }
}
