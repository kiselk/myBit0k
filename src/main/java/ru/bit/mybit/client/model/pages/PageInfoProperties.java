package ru.bit.mybit.client.model.pages;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface PageInfoProperties extends PropertyAccess<PageInfo> {


    @Path("title")
    LabelProvider<PageInfo> titleLabel();

    ValueProvider<PageInfo, String> title();


    @Path("url")
    ModelKeyProvider<PageInfo> key();

    LabelProvider<PageInfo> urlLabel();

    ValueProvider<PageInfo, String> url();

}
