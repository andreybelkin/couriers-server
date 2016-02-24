package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.Courier;
import com.globalgrupp.courier.util.HibernateUtil;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by п on 04.02.2016.
 */

@SpringUI(path="/courier")
public class CourierController extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        boolean isAuthorized=getSession().getAttribute("isAuthorized")==null?false: (boolean)getSession().getAttribute("isAuthorized");
        if (!isAuthorized){
            getUI().getPage().setLocation("/login");
        }
        setbaseContent();
    }

    private void setbaseContent(){
        Panel panel = new Panel();
        VerticalLayout panelContent = new VerticalLayout();

        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Courier");
        List<Courier> courierList=query.list();

        BeanItemContainer<Courier> container =
                new BeanItemContainer<Courier>(Courier.class, courierList);

        Table table=new Table();
        table.setContainerDataSource(container);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                setEditContent((Long)itemClickEvent.getItem().getItemProperty("id").getValue());
            }
        });
        table.setWidth("50%");
        table.setHeight("100%");

        table.setVisibleColumns("id","name","description");
        table.setColumnHeader("id","Номер");
        table.setColumnHeader("name","ФИО");
        table.setColumnHeader("descripion","Описание/комментарий");

        Label label=new Label("Список курьеров");

        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.addComponent(label);
        horizontalLayout.setExpandRatio(label,1.0f);
        Button btnLogout=new Button("Выход");
        btnLogout.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getSession().setAttribute("isAuthorized",false);
                getUI().getPage().setLocation("/login");
            }
        });
        horizontalLayout.addComponent(btnLogout);


        panelContent.addComponent(horizontalLayout);
        panelContent.setHeight("100%");
        panelContent.addComponent(table);
        panel.setContent(panelContent);
        panel.setHeight("100%");
        panelContent.setExpandRatio(table,1);
        setContent(panel);
    }


    private void setEditContent(Long id){
        Panel panel = new Panel();
        Layout panelContent = new VerticalLayout();

        Button backButton=new Button("К списку курьеров");
        backButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setbaseContent();
            }
        });

        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.addComponent(backButton);
        horizontalLayout.setExpandRatio(backButton,1.0f);
        Button btnLogout=new Button("Выход");
        btnLogout.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getSession().setAttribute("isAuthorized",false);
                getUI().getPage().setLocation("/login");
            }
        });
        horizontalLayout.addComponent(btnLogout);


        Session session= HibernateUtil.getSessionFactory().openSession();
        Courier courier=(Courier)session.get(Courier.class,id);

        Label label=new Label("Курьер "+id.toString());

        Label label1=new Label("ФИО:");
        TextField tfName=new TextField();
        if (courier.getName()!=null)
        tfName.setValue(courier.getName());

        Label label2=new Label("Описание/комментарий:");
        TextField tfDescription=new TextField();
        if (courier.getDescription()!=null)
        tfDescription.setValue(courier.getDescription());
        panelContent.addComponent(horizontalLayout);
        panelContent.addComponent(label);
        panelContent.addComponent(label1);
        panelContent.addComponent(tfName);
        panelContent.addComponent(label2);
        panelContent.addComponent(tfDescription);

        Button btnSave=new Button("сохранить");
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                courier.setName(tfName.getValue());
                courier.setDescription(tfDescription.getValue());
                session.beginTransaction();
                session.save(courier);
                session.getTransaction().commit();
                setbaseContent();
            }
        });
        panelContent.addComponent(btnSave);
        panel.setContent(panelContent);
        setContent(panel);
    }
}
