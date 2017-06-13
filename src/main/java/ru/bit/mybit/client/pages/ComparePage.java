package ru.bit.mybit.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import ru.bit.mybit.client.MyBitService;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import ru.bit.mybit.client.model.compare.CompareInfo;
import ru.bit.mybit.client.model.compare.CompareInfoList;
import ru.bit.mybit.client.model.compare.CompareInfoProperties;
import ru.bit.mybit.client.pages.parts.AreaChartPagePart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ComparePage extends Composite {
    private ContentPanel panel;

    Label label;
    ListStore<CompareInfo> store;
    Grid<CompareInfo> grid;
    ColumnModel<CompareInfo> cm;
    VerticalLayoutContainer con;
    Label text;
    AreaChartPagePart areaChartPagePart;
    private Chart<CompareInfo> chart;
    NumericAxis<CompareInfo> axis;

    ContentPanel panel2;
    private static final CompareInfoProperties props = GWT.create(CompareInfoProperties.class);
    final int refreshInterval = 1000;

    public ComparePage() {
        final Timer timer = new Timer() {
            public void run() {
                refreshCompares();
                schedule(refreshInterval);
            }
        };
        timer.schedule(2000);

        if (panel == null) {
            panel = new ContentPanel();
            panel.setWidth("100%");
            panel.setHeight("100%");
            panel.setHeaderVisible(false);
            panel.setResize(true);
            con = new VerticalLayoutContainer();

            ColumnConfig<CompareInfo, Long> stamp = new ColumnConfig<CompareInfo, Long>(props.stamp(), 50, SafeHtmlUtils.fromTrustedString("<b>stamp</b>"));
            ColumnConfig<CompareInfo, BigDecimal> bitFinexShortAsk = new ColumnConfig<CompareInfo, BigDecimal>(props.bitFinexShortAsk(), 80, SafeHtmlUtils.fromTrustedString("<b>bitFinexShortAsk</b>"));
            ColumnConfig<CompareInfo, BigDecimal> bitstampLongBid = new ColumnConfig<CompareInfo, BigDecimal>(props.bitstampLongBid(), 80, SafeHtmlUtils.fromTrustedString("<b>bitStampLongBid</b>"));
            ColumnConfig<CompareInfo, BigDecimal> bittrexLongBid = new ColumnConfig<CompareInfo, BigDecimal>(props.bittrexLongBid(), 80, SafeHtmlUtils.fromTrustedString("<b>bittrexLongBid</b>"));
            ColumnConfig<CompareInfo, BigDecimal> poloLongBid = new ColumnConfig<CompareInfo, BigDecimal>(props.poloLongBid(), 80, SafeHtmlUtils.fromTrustedString("<b>poloLongBid</b>"));
            ColumnConfig<CompareInfo, BigDecimal> krakenLongBid = new ColumnConfig<CompareInfo, BigDecimal>(props.krakenLongBid(), 80, SafeHtmlUtils.fromTrustedString("<b>krakenLongBid</b>"));

            List<ColumnConfig<CompareInfo, ?>> columns = new ArrayList<ColumnConfig<CompareInfo, ?>>();
            columns.add(stamp);
            columns.add(bitFinexShortAsk);
            columns.add(bitstampLongBid);
            columns.add(poloLongBid);
            columns.add(bittrexLongBid);
            columns.add(krakenLongBid);

            cm = new ColumnModel<CompareInfo>(columns);
            store = new ListStore<CompareInfo>(props.key());
            grid = new Grid<CompareInfo>(store, cm);
            grid.setAllowTextSelection(true);
            grid.getView().setStripeRows(true);
            grid.getView().setAutoFill(true);
            grid.getView().setForceFit(true);

            VerticalLayoutContainer con = new VerticalLayoutContainer();
           // con.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 100));

            TextSprite title = new TextSprite("BTC/USD");
            title.setFontSize(18);

            PathSprite odd = new PathSprite();
            odd.setOpacity(100);
            odd.setFill(new Color("#eee"));
            odd.setStroke(new Color("#bbb"));
            odd.setStrokeWidth(0.5);

            axis = new NumericAxis<CompareInfo>();
            axis.setPosition(Chart.Position.LEFT);
            axis.addField(props.bitFinexShortAsk());
            axis.addField(props.bitstampLongBid());
            axis.addField(props.bittrexLongBid());
            axis.addField(props.poloLongBid());
            axis.addField(props.krakenLongBid());
            axis.setMinorTickSteps(1);
            axis.setDisplayGrid(true);
            axis.setMinimum(2810);
            axis.setMaximum(2840);

            title = new TextSprite("Date");
            title.setFontSize(18);
            TextSprite labelConfig = new TextSprite();
            labelConfig.setRotation(90);

            CategoryAxis<CompareInfo, String> catAxis = new CategoryAxis<CompareInfo, String>();
            catAxis.setPosition(Chart.Position.TOP);
            catAxis.setField(props.time());
            catAxis.setLabelConfig(labelConfig);
            catAxis.setLabelOverlapHiding(true);
            catAxis.setLabelStepRatio(5);

            final LineSeries<CompareInfo> bitShort = new LineSeries<CompareInfo>();
            bitShort.setYAxisPosition(Chart.Position.LEFT);
            bitShort.setYField(props.bitFinexShortAsk());
            bitShort.setStroke(new RGB(255, 0, 0));
            bitShort.setShowMarkers(false);
            bitShort.setSmooth(true);
            bitShort.setHighlighting(true);

            final LineSeries<CompareInfo> bitStampLong = new LineSeries<CompareInfo>();
            bitStampLong.setYAxisPosition(Chart.Position.LEFT);
            bitStampLong.setYField(props.bitstampLongBid());
            bitStampLong.setStroke(new RGB(0, 255, 0));
            bitStampLong.setShowMarkers(false);
            bitStampLong.setSmooth(true);
            bitStampLong.setHighlighting(true);
            bitStampLong.setHighlighting(true);

            final LineSeries<CompareInfo> poloLong = new LineSeries<CompareInfo>();
            poloLong.setYAxisPosition(Chart.Position.LEFT);
            poloLong.setYField(props.poloLongBid());
            poloLong.setStroke(new RGB(0, 0, 255));
            poloLong.setShowMarkers(false);
            poloLong.setSmooth(true);
            poloLong.setHighlighting(true);

            final LineSeries<CompareInfo> bittrexLong = new LineSeries<CompareInfo>();
            bittrexLong.setYAxisPosition(Chart.Position.LEFT);
            bittrexLong.setYField(props.bittrexLongBid());
            bittrexLong.setStroke(new RGB(255, 255, 0));
            bittrexLong.setShowMarkers(false);
            bittrexLong.setSmooth(true);
            bittrexLong.setHighlighting(true);

            final LineSeries<CompareInfo> krakenLong = new LineSeries<CompareInfo>();
            krakenLong.setYAxisPosition(Chart.Position.LEFT);
            krakenLong.setYField(props.krakenLongBid());
            krakenLong.setStroke(new RGB(0, 255, 255));
            krakenLong.setShowMarkers(false);
            krakenLong.setSmooth(true);
            krakenLong.setHighlighting(true);
            krakenLong.setHighlighting(true);

            final Legend<CompareInfo> legend = new Legend<CompareInfo>();
            legend.setItemHighlighting(true);
            legend.setItemHiding(true);
            legend.getBorderConfig().setStrokeWidth(0);
            legend.setPosition(Chart.Position.TOP);

            chart = new Chart<CompareInfo>();
            chart.setStore(store);
            chart.setShadowChart(true);
            chart.addAxis(axis);
            chart.addAxis(catAxis);
            chart.addSeries(bitShort);
            chart.addSeries(bitStampLong);
            chart.addSeries(bittrexLong);
            chart.addSeries(poloLong);
            chart.addSeries(krakenLong);
            chart.setAnimated(false);
            chart.setLegend(legend);

            VerticalLayoutContainer layout = new VerticalLayoutContainer();
            layout.add(chart, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

            panel2 = new ContentPanel();
            panel2.setHeading("");
            panel2.add(layout);
            con.add(panel2, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

            panel.setHeight("99%");
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

    public class CompareCallback implements AsyncCallback {
        @Override
        public void onFailure(Throwable throwable) {
            new MyDialog(throwable.getMessage()).show();
        }

        @Override
        public void onSuccess(Object result) {
            if (result instanceof CompareInfoList) {
                setCompares((CompareInfoList) result);
            }
        }
    }

    public void refreshCompares() {
        MyBitService.App.getInstance().getCompareInfoList(new CompareCallback());
    }

    public void setCompares(CompareInfoList compares) {
        ArrayList<CompareInfo> infos = compares.getCompareInfos();
        this.grid.getStore().replaceAll(infos);

        shortBF = infos.get(infos.size() - 1).getBitFinexShortAsk().setScale(2,BigDecimal.ROUND_HALF_UP);
        bitstamp = infos.get(infos.size() - 1).getBitstampLongBid().setScale(2, BigDecimal.ROUND_HALF_UP);
        polo = infos.get(infos.size() - 1).getPoloLongBid().setScale(2, BigDecimal.ROUND_HALF_UP);
        bittrex = infos.get(infos.size() - 1).getBittrexLongBid().setScale(2, BigDecimal.ROUND_HALF_UP);
        kraken = infos.get(infos.size() - 1).getKrakenLongBid().setScale(2, BigDecimal.ROUND_HALF_UP);
        diff = shortBF.subtract(polo);
        spread = shortBF.subtract(polo).divide(polo, BigDecimal.ROUND_CEILING);

        panel2.setHeading("" +
                        "[ASK:BitFinex:" + shortBF.toPlainString() + "] " +
                        "[Bitstamp:" + bitstamp.toPlainString() + "] " +
                        "[Poloniex:" + polo.toPlainString() + "] " +
                        "[Bittrex:" + bittrex.toPlainString() + "] " +
                        "[Kraken:" + kraken.toPlainString() + "] " +
                        "[DIFF:" + diff.toPlainString() + "]" +
                        "[SPREAD:" + spread.toPlainString() + "]"
        );

        this.chart.getStore().replaceAll(infos);
        this.axis.setMaximum(compares.getMax() + 1);
        this.axis.setMinimum(compares.getMin() - 1);
        this.chart.redrawChart();

        this.con.setWidth("100%");
        this.con.setHeight("100%");
        this.con.clearSizeCache();
        this.con.setLayoutData(new VerticalLayoutContainer.VerticalLayoutData());
        this.con.forceLayout();
        this.panel.clearSizeCache();
        this.panel.forceLayout();
        this.panel.setHeight("100%");

    }

    BigDecimal shortBF;
    BigDecimal bitstamp;
    BigDecimal polo;
    BigDecimal bittrex;
    BigDecimal kraken;
    BigDecimal diff;
    BigDecimal spread;

}
