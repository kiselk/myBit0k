package ru.bit.mybit.client.model.pages;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PageInfo implements IsSerializable {
    private String title;

    private String url;

    private static int COUNTER = 0;

    public PageInfo() {
    }

    public PageInfo(String title, String url) {
        this();
        this.title = title;

        this.url = url;

    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }


}
