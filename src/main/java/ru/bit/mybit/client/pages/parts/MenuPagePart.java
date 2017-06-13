package ru.bit.mybit.client.pages.parts;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import ru.bit.mybit.client.MyBitPage;
import ru.bit.mybit.client.pages.BotPage;
import ru.bit.mybit.client.pages.ComparePage;
import ru.bit.mybit.client.pages.OverPage;
import ru.bit.mybit.client.pages.PoloniexPage;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.HashMap;
import java.util.Map;

public class MenuPagePart extends Composite {
    private Map<MenuItem, Composite> created = new HashMap<MenuItem, Composite>();

    public enum MenuItem {
        POLO("Poloniex BTC", "POLO"),
        OVER("Overview", "OVER"),
        COMP("Compare", "COMP"),
        COINS("Coins", "COINS"),
        BOT("Settings", "BOT");

        private String name;
        private String shortName;

        MenuItem(String name, String shortName) {
            this.name = name;
            this.shortName = shortName;
        }

        String getText() {
            return name;
        }
    }

    HorizontalPanel horPanel = new HorizontalPanel();
    ContentPanel contentPanel = new ContentPanel();
    MyBitPage main;

    public MenuPagePart(MyBitPage main) {
        this.main = main;
        contentPanel.setHeaderVisible(false);
        contentPanel.setBodyStyle("background-color:#157FCC;");
        initWidget(contentPanel);

        for (MenuItem item : MenuItem.values()) {
            final TextButton button = new TextButton(item.name);
            button.setData("page", item);
            button.setHeight("40px");
            button.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {

                    onClick((MenuItem) button.getData("page"));

                }
            });

            horPanel.add(button);
        }
        contentPanel.add(horPanel);
    }

    private void onClick(MenuItem item) {
        try {
            if (item.equals(MenuItem.COINS)) {
                main.toggleChart();
            } else {
                Composite page = created.get(item);
                if (page == null) {
                    page = createPage(item);
                    created.put(item, page);
                }
                main.setTitleText(item.getText());
                main.getContent().open(page);
            }
        } catch (Exception e) {
            new MessageBox("", e.getMessage()).show();
        }
    }

    public void open(MenuItem item) {
        onClick(item);
    }

    public void show(Composite page) {
        main.getContent().open(page);
    }

    public Composite createPage(MenuItem item) {
        Composite page = null;
        switch (item) {
            case OVER:
                page = new OverPage();
                break;
            case COMP:
                page = new ComparePage();
                break;
            case POLO:
                page = new PoloniexPage();
                break;
            case BOT:
                page = new BotPage();
                break;
            default:
                page = new Composite();
                break;
        }

        return page;
    }
}
