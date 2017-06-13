package ru.bit.mybit.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

public class MyBit implements EntryPoint {

    private MyBitPage mainPage;

    @Override
    public void onModuleLoad() {
        RootPanel rootPanel = RootPanel.get();
        mainPage = new MyBitPage();
        rootPanel.add(mainPage);
        mainPage.resizeAllNow();
    }

    public MyBitPage getMainPage() {
        return this.mainPage;
    }


}
