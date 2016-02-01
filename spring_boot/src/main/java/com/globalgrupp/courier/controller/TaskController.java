package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.Address;
import com.globalgrupp.courier.model.Task;
import com.globalgrupp.courier.util.HibernateUtil;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.vaadin.spring.annotation.VaadinUI;

import java.util.List;

/**
 * Created by п on 01.02.2016.
 */


@VaadinUI(path="task")
public class TaskController extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Panel panel = new Panel();
        Layout panelContent = new VerticalLayout();

        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Task");
        List<Task> tasks=query.list();
        BeanItemContainer<Task> container =
                new BeanItemContainer<Task>(Task.class, tasks);

        Grid grid=new Grid(container);
        Button button=new Button();
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                createTask();
            }
        });
        panelContent.addComponent(grid);
        panel.setContent(panelContent);
        setContent(panel);
    }

    private void createTask(){
        Panel panel = new Panel();
        Layout panelContent = new VerticalLayout();

        TextField tf = new TextField("");
        ComboBox cb=new ComboBox();
        panelContent.addComponent(tf);
        panelContent.addComponent(cb);

        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Task");
        List<Task> tasks=query.list();
        BeanItemContainer<Task> container =
                new BeanItemContainer<Task>(Task.class, tasks);

        Grid grid=new Grid(container);
        Button buttonAdd=new Button();
        buttonAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addAddress();
            }
        });
        Button buttonRemove=new Button();
        buttonRemove.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                removeAddress();
            }
        });

        panel.setContent(panelContent);
        setContent(panel);
    }
}
