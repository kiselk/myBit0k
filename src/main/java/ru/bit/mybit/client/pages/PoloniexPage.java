package ru.bit.mybit.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.bit.mybit.client.MyBitService;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.state.client.GridFilterStateHandler;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import ru.bit.mybit.client.model.orders.OrderInfo;
import ru.bit.mybit.client.model.orders.OrderInfoList;
import ru.bit.mybit.client.model.orders.OrderInfoProperties;

import java.util.ArrayList;
import java.util.List;

public class PoloniexPage extends Composite {
    private ContentPanel panel;

    ListStore<OrderInfo> store;
    Grid<OrderInfo> grid;
    ColumnModel<OrderInfo> cm;
    VerticalLayoutContainer con;

    private static final OrderInfoProperties props = GWT.create(OrderInfoProperties.class);
    final int refreshInterval = 10000;

    public PoloniexPage() {
        final Timer timer = new Timer() {
            public void run() {
                refreshOrderInfos();
                //    schedule(refreshInterval);
            }
        };
        timer.schedule(1000);

        if (panel == null) {
            panel = new ContentPanel();
            panel.setHeight("100%");
            panel.setHeaderVisible(false);
            panel.setResize(true);

            con = new VerticalLayoutContainer();

            ColumnConfig<OrderInfo, Integer> idCol = new ColumnConfig<OrderInfo, Integer>(props.id(), 100, SafeHtmlUtils.fromTrustedString("<b>Id</b>"));
            ColumnConfig<OrderInfo, String> typeCol = new ColumnConfig<OrderInfo, String>(props.type(), 50, SafeHtmlUtils.fromTrustedString("<b>Type</b>"));
            ColumnConfig<OrderInfo, String> amountCol = new ColumnConfig<OrderInfo, String>(props.amount(), 150, SafeHtmlUtils.fromTrustedString("<b>Amount</b>"));
            ColumnConfig<OrderInfo, String> rateCol = new ColumnConfig<OrderInfo, String>(props.rate(), 150, SafeHtmlUtils.fromTrustedString("<b>Rate</b>"));
            ColumnConfig<OrderInfo, String> totalCol = new ColumnConfig<OrderInfo, String>(props.total(), 150, SafeHtmlUtils.fromTrustedString("<b>Total</b>"));
            List<ColumnConfig<OrderInfo, ?>> columns = new ArrayList<ColumnConfig<OrderInfo, ?>>();

            columns.add(idCol);
            columns.add(typeCol);
            columns.add(amountCol);
            columns.add(rateCol);
            columns.add(totalCol);

            cm = new ColumnModel<OrderInfo>(columns);
            store = new ListStore<OrderInfo>(props.key());
            grid = new Grid<OrderInfo>(store, cm);

            grid.setAllowTextSelection(true);
            grid.getView().setStripeRows(true);
            grid.getView().setAutoFill(true);
            grid.getView().setForceFit(true);
            grid.setStateful(true);
            grid.setBorders(false);

            NumericFilter<OrderInfo, Integer> lastFilter = new NumericFilter<OrderInfo, Integer>(props.id(), new NumberPropertyEditor.IntegerPropertyEditor());
            lastFilter.setLessThanValue(10000);
            lastFilter.setActive(true, false);
            StringFilter<OrderInfo> typeFilter = new StringFilter<OrderInfo>(props.type());
            StringFilter<OrderInfo> amountFilter = new StringFilter<OrderInfo>(props.amount());
            StringFilter<OrderInfo> rateFilter = new StringFilter<OrderInfo>(props.rate());
            StringFilter<OrderInfo> totalFilter = new StringFilter<OrderInfo>(props.total());
            GridFilters<OrderInfo> filters = new GridFilters<OrderInfo>();
            filters.initPlugin(grid);
            filters.setLocal(true);
            filters.addFilter(lastFilter);
            filters.addFilter(typeFilter);
            filters.addFilter(amountFilter);
            filters.addFilter(rateFilter);
            filters.addFilter(totalFilter);

            GridFilterStateHandler<OrderInfo> handler = new GridFilterStateHandler<OrderInfo>(grid, filters);
            handler.loadState();

            con.setHeight("100%");
            con.setBorders(true);
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

    public class PoloniexCallback implements AsyncCallback {
        @Override
        public void onFailure(Throwable throwable) {
            new MyDialog(throwable.getMessage()).show();
        }

        @Override
        public void onSuccess(Object result) {
            if (result instanceof OrderInfoList) {
                setOrderInfos((OrderInfoList) result);
            }
        }
    }

    public void refreshOrderInfos() {
        MyBitService.App.getInstance().getOrderInfoList(new PoloniexCallback());

    }

    public void setOrderInfos(OrderInfoList configs) {
        this.grid.getStore().replaceAll(configs.getOrderInfos());
    }
}
