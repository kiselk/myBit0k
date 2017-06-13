package ru.bit.mybit.client.pages.parts;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.markview.automation.JAEServletPageManager;
//import com.markview.automation.pages.BasePage;
import ru.bit.mybit.client.MyBitPage;
import ru.bit.mybit.client.model.pages.PageInfo;
import ru.bit.mybit.client.model.pages.PageInfoList;
import ru.bit.mybit.client.model.pages.PageInfoProperties;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.ArrayList;
import java.util.List;

public class LeftMenuPagePart extends Composite {

    private ContentPanel panel;
    ListStore<PageInfo> store;
    Grid<PageInfo> grid;
    ColumnModel<PageInfo> cm;

    final int refreshInterval = 10000;

    private static final PageInfoProperties props = GWT.create(PageInfoProperties.class);

    MyBitPage mainPage;

    public LeftMenuPagePart() {

        if (panel == null) {

            panel = new ContentPanel();
            panel.setWidth("100%");
            panel.setHeight("100%");
            panel.setHeaderVisible(false);


            ColumnConfig<PageInfo, String> nameCol = new ColumnConfig<PageInfo, String>(props.title(), 50, SafeHtmlUtils.fromTrustedString("<b>Page</b>"));

            List<ColumnConfig<PageInfo, ?>> columns = new ArrayList<ColumnConfig<PageInfo, ?>>();
            AbstractCell<String> c2 = new AbstractCell<String>() {

                @Override
                public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
                    value = "<div  style=\"font-color:#157FCC; cursor: pointer; cursor: hand; font-size:11px; font-family: tahoma, arial, verdana, sans-serif, Lucida Sans; line-height : 4px; height=4px\" >" + value + "</div>";
                    sb.appendHtmlConstant(value);
                }
            };

            nameCol.setCell(c2);
            columns.add(nameCol);
            cm = new ColumnModel<PageInfo>(columns);

            store = new ListStore<PageInfo>(props.key());

            grid = new Grid<PageInfo>(store, cm);
            grid.setAllowTextSelection(true);
            grid.setHideHeaders(true);
            grid.getView().setStripeRows(false);
            grid.getView().setAutoFill(true);
            grid.getView().setForceFit(true);
            grid.getElement().getStyle().setCursor(Style.Cursor.POINTER);

            VerticalLayoutContainer con = new VerticalLayoutContainer();
            con.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
            panel.setWidget(con);
        }
        initWidget(panel);

        refreshCoins();
    }


    public void refreshCoins() {
        this.grid.setEnabled(false);
    }

    public void setPages(PageInfoList pages) {
        this.grid.getStore().clear();
        this.grid.getStore().addAll(pages.getPageInfos());
        this.grid.getView().refresh(true);
        this.grid.setEnabled(true);
        this.mainPage.refreshPanel();
    }
}


