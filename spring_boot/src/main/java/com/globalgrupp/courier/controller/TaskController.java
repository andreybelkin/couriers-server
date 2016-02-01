package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.Address;
import com.globalgrupp.courier.model.Courier;
import com.globalgrupp.courier.model.Task;
import com.globalgrupp.courier.model.TaskAddressResultLink;
import com.globalgrupp.courier.util.HibernateUtil;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by п on 01.02.2016.
 */


@SpringUI(path="/task")
public class TaskController extends UI {


    Collection<Object> selectedAddresses=null;
    TextField tfName=null;
    ComboBox cbCourier=null;
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        setBaseContent();
    }

    private void setBaseContent(){
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
        panelContent.addComponent(button);
        panelContent.addComponent(grid);
        panel.setContent(panelContent);
        setContent(panel);
    }

    private void createTask(){
        Panel panel = new Panel();
        Layout panelContent = new VerticalLayout();
        Session session= HibernateUtil.getSessionFactory().openSession();
         tfName = new TextField("");
        cbCourier=new ComboBox();
        cbCourier.setFilteringMode(FilteringMode.CONTAINS);
        Query queryCouriers=session.createQuery("from Courier");
        List<Courier> courierList=queryCouriers.list();
        for(int i=0;i<courierList.size();i++){
            cbCourier.addItem(courierList.get(i));
        }

        cbCourier.setNullSelectionAllowed(false);

        panelContent.addComponent(tfName);
        panelContent.addComponent(cbCourier);


        Query query=session.createQuery("from Address ");
        List<Address> tasks=query.list();
        BeanItemContainer<Address> container =
                new BeanItemContainer<Address>(Address.class, tasks);

        Grid grid=new Grid(container);

        Button buttonAdd=new Button();
        buttonAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                saveTask();
            }
        });
        grid.setSelectionMode(Grid.SelectionMode.MULTI);


        grid.addSelectionListener(selectionEvent -> { // Java 8
            // Get selection from the selection model
            Collection<Object> selected = ((Grid.MultiSelectionModel)
                    grid.getSelectionModel()).getSelectedRows();
            selectedAddresses=selected;

        });
        panelContent.addComponent(grid);
        panelContent.addComponent(buttonAdd);
        panel.setContent(panelContent);
        setContent(panel);
    }
    private void saveTask(){
        try{
            String name=tfName.getValue();
            if (selectedAddresses==null){
                return;
            }
            List<Object> lAddress=new ArrayList<>(selectedAddresses);

            Session session= HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Task task=new Task();
            task.setDescription(name);
            List<TaskAddressResultLink> taskAddressResultLinkList=new ArrayList<>();

            Courier courier=(Courier)cbCourier.getValue();
            task.setCourier(courier);
            session.save(task);
            for (int i=0;i<lAddress.size();i++){
                TaskAddressResultLink taskAddressResultLink=new TaskAddressResultLink();
                taskAddressResultLink.setAddress((Address) lAddress.get(i));
                taskAddressResultLink.setTask(task);
                session.save(taskAddressResultLink);
                taskAddressResultLinkList.add(taskAddressResultLink);
            }
            task.setTaskAddressResultLinks(new HashSet<>(taskAddressResultLinkList));
            //task.setTaskAddressResultLinks();
            session.save(task);
            session.getTransaction().commit();
            setBaseContent();

            Sender sender = new Sender("AIzaSyAlV0Ldil5wHjxlKZgHE_8ErzV-5d_HIUg");
            Message message=new Message.Builder().addData("message",task.getDescription())
                    .addData("taskId",task.getId().toString()).build();
            sender.send(message,courier.getAppPushId(),5);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
