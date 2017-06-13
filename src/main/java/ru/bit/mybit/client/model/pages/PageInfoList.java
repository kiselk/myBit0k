package ru.bit.mybit.client.model.pages;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;


/**
 * Created by user on 8/20/2015.
 */
public class PageInfoList implements IsSerializable {
    public ArrayList<PageInfo> getPageInfos() {
        return pageInfos;
    }

    public void setPageInfos(ArrayList<PageInfo> pageInfos) {
        this.pageInfos = pageInfos;
    }

    ArrayList<PageInfo> pageInfos = new ArrayList<PageInfo>();

    public PageInfoList() {

    }

    public Integer getCount() {
        return this.pageInfos.size();
    }

    public void add(PageInfo pageInfos) {
        this.pageInfos.add(pageInfos);
    }
}
