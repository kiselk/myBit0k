package ru.bit.mybit.client.pages.parts;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TitlePagePart extends Composite {

    private Label label1;
    private Label label2;

    public TitlePagePart() {

        ContentPanel contentPanel = new ContentPanel();
        HorizontalLayoutContainer horPanel = new HorizontalLayoutContainer();
        CenterLayoutContainer cent1 = new CenterLayoutContainer();
        CenterLayoutContainer cent2 = new CenterLayoutContainer();

        contentPanel.setHeaderVisible(false);
        contentPanel.setBodyStyle("background-color:#157FCC;");


        ContentPanel mybit0k = new ContentPanel();
        mybit0k.setHeaderVisible(false);
        mybit0k.setBodyStyle("background-color:white;");
        mybit0k.setSize("70px", "30px");
        ContentPanel server = new ContentPanel();
        server.setHeaderVisible(false);
        server.setHeight("30px");
        /*
        label1 = new Label("MyBit0k");
        label1.getElement().getStyle().setBackgroundColor("white");
        label1.getElement().getStyle().setColor("#157FCC");
        label1.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        label1.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        label1.setWidth("70px");
          */
        cent1.setSize("70px", "30px");

       // cent1.add(label1, new MarginData(0));

       // mybit0k.add(cent1);
      //  horPanel.add(mybit0k, new HorizontalLayoutContainer.HorizontalLayoutData(-1, 30));
        contentPanel.add(horPanel, new MarginData(5));

        initWidget(contentPanel);
    }

    public void setTitleLabel(String pageName) {
    }

}
