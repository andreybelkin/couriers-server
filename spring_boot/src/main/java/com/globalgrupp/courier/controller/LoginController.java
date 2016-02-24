package com.globalgrupp.courier.controller;

import com.ejt.vaadin.loginform.DefaultVerticalLoginForm;
import com.ejt.vaadin.loginform.LoginForm;
import com.globalgrupp.courier.VaadinApplication;
import com.globalgrupp.courier.model.SimpleLoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
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

//        //
//        // Create a new instance of the navigator. The navigator will attach
//        // itself automatically to this view.
//        //
//        new Navigator(this, this);
//
//        //
//        // The initial log view where the user can login to the application
//        //
//        getNavigator().addView(SimpleLoginView.NAME, SimpleLoginView.class);//
//
//        //
//        // Add the main view of the application
//        //
//
//
//        //
//        // We use a view change handler to ensure the user is always redirected
//        // to the login view if the user is not logged in.
//        //
//        getNavigator().addViewChangeListener(new ViewChangeListener() {
//
//            @Override
//            public boolean beforeViewChange(ViewChangeEvent event) {
//
//                // Check if a user has logged in
//                boolean isLoggedIn = getSession().getAttribute("isAuthorized")!=null?(boolean)getSession().getAttribute("isAuthorized"):false;
//                boolean isLoginView = event.getNewView() instanceof SimpleLoginView;
//
//                if (!isLoggedIn && !isLoginView) {
//                    // Redirect to login view always if a user has not yet
//                    // logged in
//                    //getNavigator().navigateTo(SimpleLoginView.NAME);
//
////                    getUI().getPage().setLocation("/login");
//                    return false;
//
//                } else if (isLoggedIn && isLoginView) {
//                    // If someone tries to access to login view while logged in,
//                    // then cancel
//                    return false;
//                }
//
//                return true;
//            }
//
//            @Override
//            public void afterViewChange(ViewChangeEvent event) {
//
//            }
//        });
    }
}
