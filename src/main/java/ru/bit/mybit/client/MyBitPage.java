package ru.bit.mybit.client;

import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.RootPanel;
import ru.bit.mybit.client.pages.parts.ContentPagePart;
import ru.bit.mybit.client.pages.parts.ChartPagePart;
import ru.bit.mybit.client.pages.parts.MenuPagePart;
import ru.bit.mybit.client.pages.parts.TitlePagePart;
import ru.bit.mybit.client.pages.parts.LeftMenuPagePart;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.*;
import com.sencha.gxt.widget.core.client.event.HideEvent;

public class MyBitPage extends Composite {

    TitlePagePart title;
    MenuPagePart menu;
    ContentPagePart content;
    ChartPagePart chart;
    ContentPanel chartPanel;
    ContentPanel leftMenuPanel;
    Window chartWindow;
    VerticalLayoutContainer page = new VerticalLayoutContainer();
    ContentPanel pagePanel = new ContentPanel();
    int menuHeight = 40;
    int leftPanelWidth = 0;

    int width = 999;
    int footerHeight = 20;
    ResizeHandler handler = new ResizeHandler() {
        @Override
        public void onResize(com.google.gwt.event.logical.shared.ResizeEvent event) {
            resizeAllNow2();

        }
    };

    public void resizeAllNow() {
        int windowHeight = RootPanel.get().getOffsetHeight();
        int height = windowHeight - menuHeight - footerHeight;

        page.setHeight(windowHeight);
        pagePanel.setHeight(windowHeight);
        leftMenuPanel.setHeight(height);
        content.setHeight(height);

    }

    public void resizeAllNow2() {
        int windowHeight = com.google.gwt.user.client.Window.getClientHeight();
        int height = windowHeight - menuHeight - footerHeight;
        page.setHeight(windowHeight);
        pagePanel.setHeight(windowHeight);
        leftMenuPanel.setHeight(height);
        content.setHeight(height);

    }

    public MyBitPage() {
        com.google.gwt.user.client.Window.addResizeHandler(handler);
        int windowHeight = RootPanel.get().getOffsetHeight();
        int windowWidth = RootPanel.get().getOffsetWidth();
        int height = windowHeight - menuHeight - footerHeight;

        chart = new ChartPagePart(this);
        chartPanel = new ContentPanel();
        chartPanel.setVisible(false);
        chartPanel.setPixelSize(500, 600);
        for (ToolButton button : chart.getToolButtons()) {
            chartPanel.addTool(button);
        }
        chartPanel.setWidget(chart);
        chartWindow = new Window();
        chartWindow.add(chart);
        chartWindow.setPixelSize(400, 500);
        chartWindow.setResizable(true);
        for (ToolButton button : chart.getToolButtons()) {
            chartWindow.addTool(button);
        }
        chartWindow.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
            }

        });
        title = new TitlePagePart();
        menu = new MenuPagePart(this);
        content = new ContentPagePart();

        leftMenuPanel = new ContentPanel();
        leftMenuPanel.setVisible(true);
        leftMenuPanel.setWidth(leftPanelWidth);
        leftMenuPanel.setHeight(height);
        leftMenuPanel.setHeaderVisible(false);
        leftMenuPanel.add(new LeftMenuPagePart());
        content.setHeight(height);

        Viewport viewPort = new Viewport();
        CenterLayoutContainer invisibleHor = new CenterLayoutContainer();
        invisibleHor.setWidth("100%");
        invisibleHor.setHeight("100%");
        viewPort.setWidth(width);
        viewPort.setHeight("100%");

        HorizontalLayoutContainer leftMenuAndContent = new HorizontalLayoutContainer();
        leftMenuAndContent.setHeight(height);
        leftMenuAndContent.setWidth(width);
        leftMenuAndContent.add(leftMenuPanel, new HorizontalLayoutContainer.HorizontalLayoutData(-1, 1));
        leftMenuAndContent.add(content, new HorizontalLayoutContainer.HorizontalLayoutData(1, 1));
        leftMenuAndContent.setBorders(false);


        HorizontalLayoutContainer titleAndMenu = new HorizontalLayoutContainer();

        titleAndMenu.setHeight(menuHeight);
        titleAndMenu.setWidth(width);
        titleAndMenu.add(title, new HorizontalLayoutContainer.HorizontalLayoutData(1, 1));
        titleAndMenu.add(menu, new HorizontalLayoutContainer.HorizontalLayoutData(-1, 1));
        titleAndMenu.setBorders(false);
        page.setHeight(height);
        page.setWidth(width);
        page.add(titleAndMenu, new VerticalLayoutContainer.VerticalLayoutData(-1, -1));
        page.add(leftMenuAndContent, new VerticalLayoutContainer.VerticalLayoutData(-1, 1));

        pagePanel.setResize(true);
        pagePanel.setWidth(width);
        pagePanel.setHeight(height);
        pagePanel.setHeaderVisible(false);
        pagePanel.setBorders(false);

        pagePanel.add(page, new VerticalLayoutContainer.VerticalLayoutData(777, 1));

        invisibleHor.setWidget(pagePanel);
        invisibleHor.setResize(true);
        viewPort.add(invisibleHor);
        viewPort.setResize(true);
        initWidget(viewPort);
        resizeAllNow();
        this.menu.open(MenuPagePart.MenuItem.COMP);
    }

    public void toggleChartPanel() {

        if (this.chartPanel.isVisible()) {
            this.chartPanel.clear();
            this.chartWindow.add(chart);
            this.chartPanel.hide();
            this.chartWindow.show();


        } else {
            this.chartWindow.clear();
            this.chartPanel.add(chart);
            this.chartWindow.hide();
            this.chartPanel.show();
        }


    }

    public void toggleChart() {
        if (this.chartPanel.isVisible() || this.chartWindow.isVisible()) {
            this.chartWindow.hide();
            this.chartPanel.hide();
        } else {
            this.chartWindow.hide();
            this.chartPanel.hide();
            this.chartPanel.clear();
            this.chartWindow.add(chart);
            this.chartPanel.hide();
            this.chartWindow.show();
        }
    }

    public void refreshPanel() {
    }

    public ContentPagePart getContent() {
        return this.content;
    }

    public void setTitleText(String text) {
        this.title.setTitleLabel(text);
    }


}
