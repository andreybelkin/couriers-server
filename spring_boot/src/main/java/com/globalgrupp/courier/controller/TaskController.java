package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.*;
import com.globalgrupp.courier.util.HibernateUtil;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.ExternalResource;
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
        VerticalLayout panelContent = new VerticalLayout();

        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Task");
        List<Task> tasks=query.list();
        BeanItemContainer<Task> container =
                new BeanItemContainer<Task>(Task.class, tasks);

        Grid grid=new Grid(container);
        grid.setWidth("100%");
        grid.setHeight("100%");
        grid.getColumn("courier").setHeaderCaption("Курьер");
        grid.getColumn("description").setHeaderCaption("Описание");
        grid.getColumn("periodBegin").setHidden(true);
        grid.getColumn("periodEnd").setHidden(true);
        grid.getColumn("taskAddressResultLinks").setHeaderCaption("Список адресов");
        grid.getColumn("id").setHidden(true);


        grid.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                if (itemClickEvent.isDoubleClick()){
                    renderTaskDetail(itemClickEvent.getItem().getItemProperty("id").getValue().toString());
                }
            }
        });
        Button button=new Button("Создать задачу");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                createTask();
            }
        });

        Button btnTotalReport=new Button("Сводный отчет");
        btnTotalReport.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                totalGrid();
            }
        });

        panelContent.addComponent(button);
        panelContent.addComponent(btnTotalReport);
        panelContent.addComponent(grid);
        panelContent.setHeight("100%");
        panel.setHeight("100%");
        panel.setContent(panelContent);
        panelContent.setExpandRatio(grid,1);

        setContent(panel);
    }

    private void renderTaskDetail(String taskIdStr){
        Panel panel = new Panel();
        VerticalLayout panelContent = new VerticalLayout();

        Long taskId=new Long(taskIdStr);
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from TaskAddressResultLink where task_id=:taskId");
        query.setParameter("taskId",taskId);
        List<TaskAddressResultLink> taskAddressResultLinkListquery =query.list();

        List<TaskResult> taskResults=new ArrayList<TaskResult>();
        for (int i=0;i<taskAddressResultLinkListquery.size();i++){
            TaskAddressResultLink q=taskAddressResultLinkListquery.get(i);
            if (q.getResults()!=null && q.getResults().size()>0){
                taskResults.addAll(new ArrayList<TaskResult>(q.getResults()));
            }
        }

        BeanItemContainer<TaskResult> container =
                new BeanItemContainer<TaskResult>(TaskResult.class, taskResults);

        Table grid=new Table();
        grid.setContainerDataSource(container);
        grid.setWidth("100%");
        grid.addGeneratedColumn("photoIds", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                Property prop =
                        table.getItem(itemId).getItemProperty(columnId);
                if (prop.getType().equals(List.class)) {
                    HorizontalLayout hbox = new HorizontalLayout();
                    List<Long>qwe=(List<Long>)prop.getValue();
                    for (int i=0;i<qwe.size();i++){
                        Link res=new Link("Вложение;",new ExternalResource("http://46.146.171.6:8080/service/getFile/"+qwe.get(i).toString()));
                        res.setTargetName("_blank");
                        hbox.addComponent(res);
                    }

                    return hbox;
                }
                return null;
            }
        });

        Button backButton=new Button("К списку задач");
        backButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setBaseContent();
            }
        });

        Button btnTotalReport=new Button("К сводному реестру");
        btnTotalReport.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                totalGrid();
            }
        });
        grid.setVisibleColumns("comment","correctPlace","photoIds","porch","location","taskAddressResultLink");
        grid.setColumnHeader("comment","Комментарий");
        grid.setColumnHeader("correctPlace","Совпадает местоположение");
        grid.setColumnHeader("location","Адрес курьера");
        grid.setColumnHeader("photoIds","Вложение");
        grid.setColumnHeader("porch","Подъезд");
        grid.setColumnHeader("taskAddressResultLink","Адрес задачи");

        grid.setHeight("100%");
        panelContent.addComponent(backButton);
        panelContent.addComponent(btnTotalReport);
        panelContent.addComponent(grid);
        panelContent.setHeight("100%");
        panel.setHeight("100%");
        panel.setContent(panelContent);
        panelContent.setExpandRatio(grid,1);
        setContent(panel);

    }


    private void totalGrid(){
        Panel panel = new Panel();
        VerticalLayout panelContent = new VerticalLayout();


        List<TotalReport> totalReportList=new ArrayList<>();

        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Courier");
        List<Courier> courierList=query.list();

        for (int i=0;i<courierList.size();i++){
            Courier courier=courierList.get(i);

            Query query1=session.createQuery("from Task where courier_id=:courierId");
            query1.setParameter("courierId",courier.getId());
            List<Task> taskList=query1.list();
            for (int k=0;k<taskList.size();k++){
                List<TaskAddressResultLink> taskAddressResultLinkList=new ArrayList<TaskAddressResultLink>(taskList.get(k).getTaskAddressResultLinks());
                int addressesCount=0;
                int porchCount=0;
                int courierAddressesCount=0;
                int courierPorchCount=0;
                for (int z=0;z<taskAddressResultLinkList.size();z++){
                    addressesCount++;
                    if (taskAddressResultLinkList.get(z).getAddress().getPorchCount()!=null){
                        porchCount+=taskAddressResultLinkList.get(z).getAddress().getPorchCount().intValue();
                    }

                    ArrayList<TaskResult> results=new ArrayList<TaskResult>(taskAddressResultLinkList.get(z).getResults());
                    if (results.size()>0){
                        courierAddressesCount++;
                    }
                    for (int z1=0;z1<results.size();z1++){
                        courierPorchCount++;
                    }
                }
                TotalReport totalReport=new TotalReport();
                totalReport.setTask(taskList.get(k));
                totalReport.setCourier(courier);
                totalReport.setAddressCount(String.valueOf(addressesCount));
                totalReport.setPorchCount(String.valueOf(porchCount));
                totalReport.setCourierAddressCount(String.valueOf(courierAddressesCount));
                totalReport.setPorchAddressCount(String.valueOf(courierPorchCount));
                totalReportList.add(totalReport);
            }
        }

        BeanItemContainer<TotalReport> container =
                new BeanItemContainer<TotalReport>(TotalReport.class, totalReportList);

        Table table=new Table();
        table.setContainerDataSource(container);
        table.setWidth("100%");
        //table.setHeight("100%");

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                if (itemClickEvent.isDoubleClick()){
                    renderTaskDetail(((Task)(itemClickEvent.getItem().getItemProperty("task").getValue())).getId().toString());
                }
            }
        });


        //table.setVisibleColumns();
        table.setVisibleColumns("courier","task","taskStartDate","taskEndDate","addressCount","courierAddressCount","porchCount","porchAddressCount");
        table.setColumnHeader("courier","Курьер");
        table.setColumnHeader("task","Задача");
        table.setColumnHeader("taskStartDate","Начало задачи");
        table.setColumnHeader("taskEndDate","Конец задачи");
        table.setColumnHeader("addressCount","Количество адресов (Всего)");
        table.setColumnHeader("courierAddressCount","Количество адресов (Отмечены)");
        table.setColumnHeader("porchCount","Количество подъездов (Всего)");
        table.setColumnHeader("porchAddressCount","Количество подъездов (Отмечены)");

        Button btnBack=new Button("К списку задач");
        btnBack.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setBaseContent();
            }
        });
        table.setHeight("100%");
        panelContent.addComponent(btnBack);
        panelContent.addComponent(table);
        panelContent.setHeight("100%");
        panel.setHeight("100%");
        panel.setContent(panelContent);
        panelContent.setExpandRatio(table,1);
        setContent(panel);
    }



    private void createTask(){
        Panel panel = new Panel();
        VerticalLayout panelContent = new VerticalLayout();
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
        panelContent.addComponent(new Label("Описание"));
        panelContent.addComponent(tfName);
        panelContent.addComponent(new Label("Курьер"));
        panelContent.addComponent(cbCourier);


        Query query=session.createQuery("from Address ");
        List<Address> tasks=query.list();
        BeanItemContainer<Address> container =
                new BeanItemContainer<Address>(Address.class, tasks);

        Grid grid=new Grid(container);




        Button buttonAdd=new Button("Сохранить");
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
        grid.setWidth("100%");
        grid.setColumnOrder("street","houseNumber");

        grid.getColumn("street").setHeaderCaption("Улица");
        grid.getColumn("houseNumber").setHeaderCaption("Дом №");
        grid.getColumn("apartmentCount").setHeaderCaption("Количество квартир");
        grid.getColumn("cityRayon").setHeaderCaption("Район");
        grid.getColumn("comment").setHeaderCaption("Коментарий");
        grid.getColumn("houseQuality").setHeaderCaption("Уровень дома");
        grid.getColumn("houseYear").setHeaderCaption("Год постройки(сдачи)");
        grid.getColumn("key").setHeaderCaption("Ключ");
        grid.getColumn("kv").setHeaderCaption("Кв.");
        grid.getColumn("lastUpdate").setHeaderCaption("Последнее обновление");
        grid.getColumn("levelCount").setHeaderCaption("Этажность");
        grid.getColumn("novostroyka").setHeaderCaption("Новостройка");
        grid.getColumn("porchCount").setHeaderCaption("Количество подъездов");
        grid.getColumn("postboxQuality").setHeaderCaption("Состояние ящиков");
        grid.getColumn("rayon").setHeaderCaption("Район");
        grid.getColumn("id").setHidden(true);
        grid.setHeight("100%");




        Grid.HeaderRow filterRow = grid.appendHeaderRow();

        for (Object pid: grid.getContainerDataSource()
                .getContainerPropertyIds()) {
            Grid.HeaderCell cell = filterRow.getCell(pid);

            TextField filterField = new TextField();
            filterField.setColumns(8);

            filterField.addTextChangeListener(change -> {
                container.removeContainerFilters(pid);
                if (! change.getText().isEmpty())
                    container.addContainerFilter(
                            new SimpleStringFilter(pid,
                                    change.getText(), true, false));
            });
            cell.setComponent(filterField);
        }

        panelContent.addComponent(new Label("Адреса"));
        panelContent.addComponent(grid);
        panelContent.addComponent(buttonAdd);
        //panelContent.setHeight("100%");
        panel.setContent(panelContent);
        //panel.setHeight("100%");
        //panelContent.setExpandRatio();
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
