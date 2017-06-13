package ru.bit.mybit.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

import ru.bit.mybit.client.MyBitService;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import ru.bit.mybit.client.model.exchanges.ExchangeInfo;
import ru.bit.mybit.client.model.exchanges.ExchangeInfoList;
import ru.bit.mybit.client.model.exchanges.ExchangeInfoProperties;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OverPage extends Composite {
    private ContentPanel panel;

    ListStore<ExchangeInfo> store;
    Grid<ExchangeInfo> grid;
    ColumnModel<ExchangeInfo> cm;
    VerticalLayoutContainer con;

    private static final ExchangeInfoProperties props = GWT.create(ExchangeInfoProperties.class);
    final int refreshInterval = 100000;

    public OverPage() {
        final Timer timer = new Timer() {
            public void run() {
                refreshExchanges();
                //schedule(refreshInterval);
            }
        };
        timer.schedule(1000);

        if (panel == null) {

            panel = new ContentPanel();
            panel.setWidth("100%");
            panel.setHeight("100%");
            panel.setHeaderVisible(false);
            panel.setResize(true);

            con = new VerticalLayoutContainer();

            ColumnConfig<ExchangeInfo, String> nameCol = new ColumnConfig<ExchangeInfo, String>(props.name(), 50, SafeHtmlUtils.fromTrustedString("<b>EX</b>"));
            ColumnConfig<ExchangeInfo, BigDecimal> bidCol = new ColumnConfig<ExchangeInfo, BigDecimal>(props.bid(), 80, SafeHtmlUtils.fromTrustedString("<b>Bid</b>"));
            ColumnConfig<ExchangeInfo, BigDecimal> askCol = new ColumnConfig<ExchangeInfo, BigDecimal>(props.ask(), 80, SafeHtmlUtils.fromTrustedString("<b>Ask</b>"));
            ColumnConfig<ExchangeInfo, BigDecimal> diffCol = new ColumnConfig<ExchangeInfo, BigDecimal>(props.diff(), 80, SafeHtmlUtils.fromTrustedString("<b>Diff</b>"));

            List<ColumnConfig<ExchangeInfo, ?>> columns = new ArrayList<ColumnConfig<ExchangeInfo, ?>>();
            columns.add(nameCol);
            columns.add(bidCol);
            columns.add(askCol);
            columns.add(diffCol);

            cm = new ColumnModel<ExchangeInfo>(columns);

            store = new ListStore<ExchangeInfo>(props.key());

            grid = new Grid<ExchangeInfo>(store, cm);
            grid.setAllowTextSelection(true);
            grid.getView().setStripeRows(true);
            grid.getView().setAutoFill(true);
            grid.getView().setForceFit(true);

            VerticalLayoutContainer con = new VerticalLayoutContainer();
            con.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

            panel.setHeight("100%");
            panel.setWidget(con);
            panel.setResize(true);

        }
        initWidget(panel);
    }

    private static class MyDialog extends AlertMessageBox {
        public MyDialog(String text) {
            super("", text);
        }
    }

    public class OverviewCallback implements AsyncCallback {
        @Override
        public void onFailure(Throwable throwable) {
            new MyDialog(throwable.getMessage()).show();
        }

        @Override
        public void onSuccess(Object result) {
            if (result instanceof ExchangeInfoList) {
                setExchanges((ExchangeInfoList) result);
            }
        }
    }

    public void refreshExchanges() {
        MyBitService.App.getInstance().getExchangeInfoList(new OverviewCallback());
    }

    public void setExchanges(ExchangeInfoList configs) {

        this.grid.getStore().replaceAll(configs.getExchangeInfos());
        this.grid.getView().refresh(true);

        this.con.setWidth("100%");
        this.con.setHeight("100%");
        this.con.clearSizeCache();
        this.con.setLayoutData(new VerticalLayoutContainer.VerticalLayoutData());
        this.con.forceLayout();
        this.panel.clearSizeCache();
        this.panel.forceLayout();
        this.panel.setHeight("100%");

    }

}
