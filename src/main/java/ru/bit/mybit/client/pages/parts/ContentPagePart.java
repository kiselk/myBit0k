package ru.bit.mybit.client.pages.parts;

import ru.bit.mybit.client.pages.*;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class ContentPagePart extends Composite {

    private ContentPanel content = new ContentPanel();

    public ContentPagePart() {

        content.setWidth("100%");
        content.setHeight("100%");
        content.setHeaderVisible(false);
        content.setResize(true);
        initWidget(content);
    }

    public void open(Composite page) {
        this.content.clear();
        this.content.add(page);
        this.content.forceLayout();

    }

    public void openLm() {
        this.content.clear();
        this.content.add(new PoloniexPage());
    }


}
