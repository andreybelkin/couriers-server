package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.SimpleLoginView;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

/**
 * Created by Lenovo on 24.02.2016.
 */
@SpringUI(path="/login")
public class LoginController extends UI {

    @Override
    protected void init(VaadinRequest request) {
        setContent(new SimpleLoginView());
    }
}
