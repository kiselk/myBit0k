package ru.bit.mybit.client.pages.parts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.ui.Label;
import ru.bit.mybit.client.MyBitPage;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import ru.bit.mybit.client.MyBitService;
import ru.bit.mybit.client.model.coins.CoinInfo;
import ru.bit.mybit.client.model.coins.CoinInfoList;
import ru.bit.mybit.client.model.coins.CoinInfoProperties;

import java.util.ArrayList;
import java.util.List;

public class ChartPagePart extends Composite {
    private ContentPanel panel;
    ListStore<CoinInfo> store;
    Grid<CoinInfo> grid;
    Label label;
    ColumnModel<CoinInfo> cm;

    final int refreshInterval  = 10000;

    private static final CoinInfoProperties props = GWT.create(CoinInfoProperties.class);

    MyBitPage mainPage;
    public ChartPagePart(MyBitPage mainPage) {
        this.mainPage = mainPage;
        final Timer timer = new Timer() {
            public void run() {
                refreshCoins();
               // schedule(refreshInterval);
            }
        };
        timer.schedule(1000);


        if (panel == null) {

            panel = new ContentPanel();
            panel.setWidth("100%");
            panel.setHeight("100%");
            panel.setHeaderVisible(false);

            ColumnConfig<CoinInfo, String> nameCol = new ColumnConfig<CoinInfo, String>(props.name(), 50, SafeHtmlUtils.fromTrustedString("<b>Coin</b>"));
            ColumnConfig<CoinInfo, String> askCol = new ColumnConfig<CoinInfo, String>(props.askPrice(), 80, SafeHtmlUtils.fromTrustedString("<b>Ask</b>"));
            ColumnConfig<CoinInfo, String> bidCol = new ColumnConfig<CoinInfo, String>(props.bidPrice(), 80, SafeHtmlUtils.fromTrustedString("<b>Bid</b>"));

            List<ColumnConfig<CoinInfo, ?>> columns = new ArrayList<ColumnConfig<CoinInfo, ?>>();
            columns.add(nameCol);
            columns.add(askCol);
            columns.add(bidCol);

            cm = new ColumnModel<CoinInfo>(columns);
            store = new ListStore<CoinInfo>(props.key());
            grid = new Grid<CoinInfo>(store, cm);

            grid.setAllowTextSelection(true);
            grid.getView().setStripeRows(true);
            grid.getView().setAutoFill(true);
            grid.getView().setForceFit(true);

            VerticalLayoutContainer con = new VerticalLayoutContainer();
            con.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 500));
            panel.setWidget(con);
        }
        initWidget(panel);

    }


    public ArrayList<ToolButton> getToolButtons() {
        ArrayList<ToolButton> buttons = new ArrayList<ToolButton>();
        ToolButton toggle;
        ToolButton refresh;
        toggle = new ToolButton(ToolButton.RESTORE);
        toggle.setToolTip("Toggle");
        toggle.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                toggle();
            }
        });
        refresh= new ToolButton(ToolButton.REFRESH);
        refresh.setToolTip("Refresh");
        refresh.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                refreshCoins();
            }
        });

        buttons.add(refresh);
        buttons.add(toggle);

        return buttons;
    }

    public void toggle() {mainPage.toggleChartPanel();}

    private static class MyDialog extends AlertMessageBox {
        public MyDialog(String text) {
            super("", text);
        }
    }

    public class ChartCallback implements AsyncCallback {
        @Override
        public void onFailure(Throwable throwable) {
            new MyDialog(throwable.getMessage()).show();
        }

        @Override
        public void onSuccess(Object result) {
                if (result instanceof CoinInfoList) {
                    setCoins((CoinInfoList) result);
            }
            if (result instanceof String) {
                setTitle(((String) result).replace(",", "\n"));
            }
        }
    }

    public void refreshCoins() {
        MyBitService.App.getInstance().getCoinInfoList(new ChartCallback());
    }

    public void setCoins(CoinInfoList coins) {
        this.grid.getStore().replaceAll(coins.getCoinInfos());
        this.grid.getView().refresh(true);
        this.mainPage.refreshPanel();
    }

    public void setTitle(String ticker) {

    }
}
