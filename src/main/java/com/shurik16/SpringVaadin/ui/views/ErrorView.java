package com.shurik16.SpringVaadin.ui.views;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;


@SpringUI(path = "/sb/error")
@Title("Ошибка")
@Theme("valo")
public class ErrorView extends UI {

    @Override
    protected void init(VaadinRequest request) {
        setContent(new Label("Ошибка"));
    }
}
