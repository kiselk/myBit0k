package ru.bit.mybit.client.pages;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import ru.bit.mybit.client.MyBitService;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class BotPage extends Composite {
    private ContentPanel panel;

    TextButton botButton;
    TextArea thresholdTextArea;
    TextButton thresholdButton;
    VerticalLayoutContainer con;

    public BotPage() {

        if (panel == null) {

            panel = new ContentPanel();
            panel.setHeight("100%");
            panel.setHeaderVisible(false);
            panel.setResize(true);
            con = new VerticalLayoutContainer();

            con.setHeight("100%");
            con.setBorders(true);

            botButton = new TextButton("Toggle Bot", new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    toggleBot();
                }
            });

            thresholdTextArea = new TextArea();
            thresholdTextArea.setText("1.0");
            thresholdButton = new TextButton("Set threshold", new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    setThreshold(Double.parseDouble(thresholdTextArea.getText()));
                }
            });

            con.add(botButton, new VerticalLayoutContainer.VerticalLayoutData(-1, -1));
            con.add(thresholdTextArea, new VerticalLayoutContainer.VerticalLayoutData(-1, -1));
            con.add(thresholdButton, new VerticalLayoutContainer.VerticalLayoutData(-1, -1));
            panel.setHeight("100%");
            panel.setWidget(con);
            panel.setResize(true);
        }
        initWidget(panel);
    }

    public class ToggleBotCallback implements AsyncCallback {
        @Override
        public void onFailure(Throwable throwable) {
        }

        @Override
        public void onSuccess(Object result) {
            if (result instanceof String) {
                botButton.setText((String) result);
            }
        }
    }

    public class SetThresholdCallback implements AsyncCallback {
        @Override
        public void onFailure(Throwable throwable) {
        }

        @Override
        public void onSuccess(Object result) {
            if (result instanceof String) {
                thresholdTextArea.setText((String) result);
            }
        }
    }

    public void toggleBot() {
        MyBitService.App.getInstance().toggleBot(new ToggleBotCallback());
    }

    public void setThreshold(Double threshold) {
        MyBitService.App.getInstance().setThreshold(threshold, new SetThresholdCallback());

    }
}
