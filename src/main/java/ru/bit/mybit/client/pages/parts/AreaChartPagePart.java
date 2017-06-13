package ru.bit.mybit.client.pages.parts;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.axis.TimeAxis;
import com.sencha.gxt.chart.client.chart.series.AreaSeries;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import ru.bit.mybit.client.MyBitService;
import ru.bit.mybit.client.model.coins.CoinInfoList;
import ru.bit.mybit.client.model.compare.CompareInfo;
import ru.bit.mybit.client.model.compare.CompareInfoList;
import ru.bit.mybit.client.model.compare.CompareInfoProperties;

import java.util.Date;

public class AreaChartPagePart extends Widget {


    protected static final int MIN_HEIGHT = 480;
    protected static final int MIN_WIDTH = 640;

    private static final CompareInfoProperties props = GWT.create(CompareInfoProperties.class);

    final ListStore<CompareInfo> store = new ListStore<CompareInfo>(props.key());

    private ContentPanel panel;
    private Chart<CompareInfo> chart;

    public class MyAsyncCallback implements AsyncCallback {
        @Override
        public void onFailure(Throwable throwable) {


        }

        @Override
        public void onSuccess(Object result) {
            //new MyDialog("success").show();
            if (result instanceof CompareInfoList) {

                setCompares((CompareInfoList) result);
            }
            if (result instanceof String) {
                setTitle(((String) result).replace(",", "\n"));
            }
        }
    }


    public void refreshCompareInfo() {
        //   new MyDialog("eventok").show();
        // this.grid.setEnabled(false);

        MyBitService.App.getInstance().getCompareInfoList(new MyAsyncCallback());

    }

    public void setCompares(CompareInfoList coins) {
        store.clear();
        store.addAll(coins.getCompareInfos());
        //store.replaceAll(coins.getCompareInfos());
        chart.redrawChart();
        this.panel.forceLayout();

    }

    @Override
    public Widget asWidget() {
        if (panel == null) {
            // store.addAll(TestData.getData(8, 20, 100));

            TextSprite title = new TextSprite("BTC/USD");
            title.setFontSize(18);

            PathSprite odd = new PathSprite();
            odd.setOpacity(1);
            odd.setFill(new Color("#ddd"));
            odd.setStroke(new Color("#bbb"));
            odd.setStrokeWidth(0.5);

            NumericAxis<CompareInfo> axis = new NumericAxis<CompareInfo>();
            axis.setPosition(Position.LEFT);
            axis.addField(props.bitFinexShortAsk());
            axis.addField(props.bitstampLongBid());
            axis.setTitleConfig(title);
            axis.setMinorTickSteps(1);
            axis.setDisplayGrid(true);
            axis.setGridOddConfig(odd);
            axis.setMinimum(2810);
            axis.setMaximum(2840);

            title = new TextSprite("Date");
            title.setFontSize(18);
            TextSprite labelConfig = new TextSprite();
            //labelConfig.setRotation(270);


            CategoryAxis<CompareInfo, Long> catAxis = new CategoryAxis<CompareInfo, Long>();
            catAxis.setPosition(Position.BOTTOM);
            catAxis.setField(props.stamp());
            catAxis.setTitleConfig(title);
            catAxis.setLabelConfig(labelConfig);
            catAxis.setLabelOverlapHiding(true);
            catAxis.setLabelStepRatio(5);

            /*catAxis.setLabelProvider(new LabelProvider<String>() {
                @Override
                public String getLabel(String item) {
                    return item.substring(0, 3);
                }
            });
              */

            final LineSeries<CompareInfo> bitShort = new LineSeries<CompareInfo>();
            bitShort.setYAxisPosition(Position.LEFT);
            bitShort.setYField(props.bitFinexShortAsk());
            bitShort.setStroke(new RGB(186, 68, 32));
            bitShort.setShowMarkers(false);
            bitShort.setSmooth(true);
            bitShort.setFill(new RGB(186, 68, 32));
            bitShort.setHighlighting(true);
            final LineSeries<CompareInfo> poloLong = new LineSeries<CompareInfo>();
            poloLong.setYAxisPosition(Position.LEFT);
            poloLong.setYField(props.bitstampLongBid());
            poloLong.setStroke(new RGB(32, 68, 186));
            poloLong.setShowMarkers(false);
            poloLong.setSmooth(true);
            poloLong.setFill(new RGB(32, 68, 186));
            poloLong.setHighlighting(true);

            final Legend<CompareInfo> legend = new Legend<CompareInfo>();
            legend.setItemHighlighting(true);
            legend.setItemHiding(true);
            legend.getBorderConfig().setStrokeWidth(0);

            final Chart<CompareInfo> chart = new Chart<CompareInfo>();
            chart.setStore(store);
            chart.setShadowChart(false);
            chart.addAxis(axis);
            chart.addAxis(catAxis);
            // chart.addSeries(series);
            chart.addSeries(bitShort);
            chart.addSeries(poloLong);
            chart.setLegend(legend);
            chart.setDefaultInsets(30);


            ToggleButton animation = new ToggleButton("Animate");
            animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                @Override
                public void onValueChange(ValueChangeEvent<Boolean> event) {
                    chart.setAnimated(event.getValue());
                }
            });
            animation.setValue(true, true);

            ToggleButton shadow = new ToggleButton("Shadow");
            shadow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                @Override
                public void onValueChange(ValueChangeEvent<Boolean> event) {
                    chart.setShadowChart(event.getValue());
                    chart.redrawChart();
                }
            });
            shadow.setValue(false);

            ToolBar toolBar = new ToolBar();
            // toolBar.add(regenerate);
            toolBar.add(animation);
            toolBar.add(shadow);

            VerticalLayoutContainer layout = new VerticalLayoutContainer();
            layout.add(toolBar, new VerticalLayoutData(1, -1));
            layout.add(chart, new VerticalLayoutData(1, 1));

            panel = new ContentPanel();
            panel.setHeading("BitFinex:Short:Ask:Red, Poloniex:Long:Bid:Blue");
            panel.add(layout);
        }

        return panel;
    }

      /*  @Override
        public Widget asWidget2() {
            if (panel == null) {

               // store.addAll(TestData.getData(12, 20, 100));

                TextSprite titleHits = new TextSprite("Number of Hits");
                titleHits.setFontSize(18);

                TextSprite titleMonthYear = new TextSprite("Month of the Year");
                titleMonthYear.setFontSize(18);

                PathSprite gridConfig = new PathSprite();
                gridConfig.setStroke(new RGB("#bbb"));
                gridConfig.setFill(new RGB("#ddd"));
                gridConfig.setZIndex(1);
                gridConfig.setStrokeWidth(1);

                NumericAxis<CompareInfo> axis = new NumericAxis<CompareInfo>();
                axis.setPosition(Position.LEFT);
                axis.addField(props.bitFinexShortAsk());
                axis.addField(props.bitstampLongBid());

                axis.setGridOddConfig(gridConfig);
                axis.setDisplayGrid(true);
                axis.setTitleConfig(titleHits);
                axis.setMinorTickSteps(2);

                TextSprite labelConfig = new TextSprite();
                labelConfig.setRotation(315);

                CategoryAxis<CompareInfo, Date> catAxis = new CategoryAxis<CompareInfo, Date>();
                catAxis.setPosition(Position.BOTTOM);
                catAxis.setField(props.stamp());
                catAxis.setTitleConfig(titleMonthYear);
                catAxis.setDisplayGrid(true);
                catAxis.setLabelConfig(labelConfig);
                catAxis.setLabelPadding(-10);
                catAxis.setMinorTickSteps(5);
                catAxis.setLabelTolerance(20);

                PathSprite highlightLine = new PathSprite();
                highlightLine.setHidden(true);
                highlightLine.addCommand(new MoveTo(0, 0));
                highlightLine.setZIndex(1000);
                highlightLine.setStrokeWidth(5);
                highlightLine.setStroke(new RGB("#444"));
                highlightLine.setOpacity(0.3);

                AreaSeries<CompareInfo> series = new AreaSeries<CompareInfo>();
                series.setHighlighting(true);
                series.setYAxisPosition(Position.LEFT);
                series.addYField(props.bitFinexShortAsk());
                series.addYField(props.bitstampLongBid());

                series.addColor(new RGB(148, 174, 10));
                series.addColor(new RGB(17, 95, 166));
                series.addColor(new RGB(166, 17, 32)); /*
                series.setHighlightLineConfig(highlightLine);
                series.setRenderer(new SeriesRenderer<CompareInfo>() {
                    @Override
                    public void spriteRenderer(Sprite sprite, int index, ListStore<CompareInfo> store) {
                        sprite.setOpacity(0.93);
                        sprite.redraw();
                    }
                });                                      */
                 /*
                Legend<CompareInfo> legend = new Legend<CompareInfo>();
                legend.setItemHighlighting(true);
                legend.setItemHiding(true);
                legend.getBorderConfig().setStrokeWidth(0);

                chart = new Chart<CompareInfo>();
                chart.setStore(store);
                // Allow room for rotated labels
                chart.setDefaultInsets(30);
                chart.addAxis(axis);
                chart.addAxis(catAxis);
                chart.addSeries(series);
                chart.setLegend(legend);

                TextButton regenerate = new TextButton("Reload Data");
                regenerate.addSelectHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        store.clear();
                  //      store.addAll(TestData.getData(12, 20, 100));
                        chart.redrawChart();
                    }
                });

                ToggleButton animation = new ToggleButton("Animate");
                animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> event) {
                        chart.setAnimated(event.getValue());
                    }
                });
                animation.setValue(true, true);

                ToolBar toolBar = new ToolBar();
                toolBar.add(regenerate);
                toolBar.add(animation);
                toolBar.setLayoutData(new VerticalLayoutData(1, -1));

                VerticalLayoutContainer layout = new VerticalLayoutContainer();
                layout.add(toolBar, new VerticalLayoutData(1, -1));
                layout.add(chart, new VerticalLayoutData(1, 1));

                panel = new ContentPanel();
                panel.setHeading("BitFinex:Short, Bitstamp:Long");
                panel.add(layout);
            }

            return panel;
        }

        */
}
