package com.globalgrupp.courier;

/**
 * Created by Lenovo on 27.01.2016.
 */

import com.globalgrupp.courier.model.Address;
import com.globalgrupp.courier.util.HibernateUtil;
import com.myjeeva.poi.ExcelReader;
import com.myjeeva.poi.ExcelSheetCallback;
import com.myjeeva.poi.ExcelWorkSheetHandler;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Theme("valo")
//@SpringUI
@SpringUI(path = "/main")
public class VaadinApplication extends  UI{
    private static final long serialVersionUID = 1L;

    final Embedded image = new Embedded("Uploaded Image");


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setDefault();

    }

    private void setDefault(){
        image.setVisible(false);
        ImageUploader receiver = new ImageUploader();
        Upload upload = new Upload("Загрузить адреса", receiver);
        upload.setButtonCaption("Начать");
        upload.addSucceededListener(receiver);
        upload.setReceiver(receiver);

// Put the components in a panel
        Panel panel = new Panel();
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.addComponents(upload, image);

        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Address");
        List<Address> addresses=query.list();
        BeanItemContainer<Address> container =
                new BeanItemContainer<Address>(Address.class, addresses);

        Grid grid=new Grid(container);
        grid.setWidth("100%");
        grid.setColumnOrder("rayon","kv","street","houseNumber","apartmentCount","postboxType","postboxQuality","houseQuality",
                "levelCount","porchCount","cityRayon");


        grid.getColumn("rayon").setHeaderCaption("Район");
        grid.getColumn("kv").setHeaderCaption("Кв.");
        grid.getColumn("street").setHeaderCaption("Улица");
        grid.getColumn("houseNumber").setHeaderCaption("Дом №");
        grid.getColumn("apartmentCount").setHeaderCaption("Количество квартир");
        grid.getColumn("postboxType").setHeaderCaption("Ящики/Вахта");
        grid.getColumn("postboxQuality").setHeaderCaption("Состояние ящиков");
        grid.getColumn("houseQuality").setHeaderCaption("Уровень дома");
        grid.getColumn("levelCount").setHeaderCaption("Этажность");
        grid.getColumn("porchCount").setHeaderCaption("Количество подъездов");
        grid.getColumn("cityRayon").setHeaderCaption("Район города");

        grid.getColumn("id").setHidden(true);
        grid.setHeight("100%");

        Grid.HeaderRow filterRow = grid.appendHeaderRow();

// Set up a filter for all columns
        for (Object pid: grid.getContainerDataSource()
                .getContainerPropertyIds()) {
            Grid.HeaderCell cell = filterRow.getCell(pid);

            // Have an input field to use for filter
            TextField filterField = new TextField();
            filterField.setColumns(8);

            // Update filter When the filter input is changed
            filterField.addTextChangeListener(change -> {
                // Can't modify filters so need to replace
                container.removeContainerFilters(pid);

                // (Re)create the filter if necessary
                if (! change.getText().isEmpty())
                    container.addContainerFilter(
                            new SimpleStringFilter(pid,
                                    change.getText(), true, false));
            });
            cell.setComponent(filterField);
        }

        panelContent.addComponent(grid);
        panelContent.setHeight("100%");
        panel.setContent(panelContent);
        panel.setHeight("100%");
        panelContent.setExpandRatio(grid,1);
        setContent(panel);
    }


    private String getCellStringValueCusom(Cell cell){

        if (cell.getCellType()==Cell.CELL_TYPE_BLANK){
            return null;
        } else if (cell.getCellType()==Cell.CELL_TYPE_BOOLEAN){

        } else if (cell.getCellType()==Cell.CELL_TYPE_ERROR){

        } else if (cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
            return String.valueOf((long)cell.getNumericCellValue());
        } else if (cell.getCellType()==Cell.CELL_TYPE_STRING){
            return cell.getStringCellValue();
        } else if (cell.getCellType()==Cell.CELL_TYPE_FORMULA){

        }
        return null;
    }



    private void renderGrid(){
        try{
            Session session= HibernateUtil.getSessionFactory().openSession();
            Query query=session.createQuery("from Adresses");
            List<Address> addresses=query.list();
            BeanItemContainer<Address> container =
                    new BeanItemContainer<Address>(Address.class, addresses);

            Grid grid=new Grid(container);
            grid.setColumnOrder("rayon","kv");

            Panel panel= (Panel)getContent();
            Layout layout=(Layout)panel.getContent();
            layout.addComponent(grid);
            panel.setContent(layout);
            setContent(panel);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    // Implement both receiver that saves upload in a file and
// listener for successful upload
    private class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
        public File file;

        public OutputStream receiveUpload(String filename,
                                          String mimeType) {
            // Create upload stream
            FileOutputStream fos = null; // Stream to write to
            try {
                // Open the file for writing.
                file = new File("/tmp/" + filename);
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                new Notification("Could not open file<br/>",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            }
            return fos; // Return the output stream to write to
        }

        public void uploadSucceeded(Upload.SucceededEvent event) {


//            try {
//                Workbook wb=WorkbookFactory.create(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InvalidFormatException e) {
//                e.printStackTrace();
//            }
//            InputStream inputStream = null;
//            try {
//                inputStream = new FileInputStream(file);
//
//
//            // Excel Cell Mapping
//            Map<String, String> cellMapping = new HashMap<String, String>();
//            cellMapping.put("HEADER",
//                    "street");
////            cellMapping.put("A", "rayon");
////            cellMapping.put("B", "kv");
//            cellMapping.put("C", "street");
////            cellMapping.put("D", "emailId");
////            cellMapping.put("E", "dob");
////            cellMapping.put("F", "salary");
//
//            // The package open is instantaneous, as it should be.
//            OPCPackage pkg = null;
//            try {
//                ExcelWorkSheetHandler<Address> workSheetHandler = new ExcelWorkSheetHandler<Address>(Address.class, cellMapping);
////                  ExcelWorkSheetRowCallbackHandler sheetRowCallbackHandler = new ExcelWorkSheetRowCallbackHandler(
////                    new ExcelRowContentCallback() {
////                        @Override
////                        public void processRow(int rowNum,    Map<String, String> map) {
////                            // Do any custom row processing here, such as save
////                            // to database
////                            // Convert map values, as necessary, to dates or
////                            // parse as currency, etc
////                            System.out.println("rowNum=" + rowNum + ", map=" + map);
////                        }
////                    });
//
//                pkg = OPCPackage.open(inputStream);
//                ExcelSheetCallback sheetCallback = new ExcelSheetCallback() {
//                    private int sheetNumber = 0;
//
//                    @Override
//                    public void startSheet(int sheetNum) {
//                        this.sheetNumber = sheetNum;
//                        System.out.println("Started processing sheet number=" + sheetNumber);
//                    }
//
//                    @Override
//                    public void endSheet() {
//                        System.out.println("Processing completed for sheet number=" + sheetNumber);
//                    }
//                };
//
//                System.out.println("Constructor: pkg, workSheetHandler, sheetCallback");
//                ExcelReader example1 = new ExcelReader(pkg, workSheetHandler, sheetCallback);
//                example1.process();
//
//                if (workSheetHandler.getValueList().isEmpty()) {
//                    // No data present
////                    LOG.error("sHandler.getValueList() is empty");
//                } else {
////                    LOG.info(workSheetHandler.getValueList().size()
////                            + " no. of records read from given excel worksheet successfully.");
//                    // Displaying data ead from Excel file
//                    List<Address> addressList=workSheetHandler.getValueList();
//                }
//
//                System.out.println("nConstructor: filePath, workSheetHandler, sheetCallback");
////                ExcelReader example2 = new ExcelReader(SAMPLE_PERSON_DATA_FILE_PATH,
////                        workSheetHandler, sheetCallback);
////                example2.process();
//
//                System.out.println("nConstructor: file, workSheetHandler, sheetCallback");
////                ExcelReader example3 = new ExcelReader(file, workSheetHandler, null);
////                example3.process();
//            } catch (RuntimeException are) {
////                LOG.error(are.getMessage(), are.getCause());
//            } catch (InvalidFormatException ife) {
////                LOG.error(ife.getMessage(), ife.getCause());
//            } catch (IOException ioe) {
////                LOG.error(ioe.getMessage(), ioe.getCause());
//            } finally {
//                IOUtils.closeQuietly(inputStream);
//                try {
//                    if (null != pkg) {
//                        pkg.close();
//                    }
//                } catch (IOException e) {
//                    // just ignore IO exception
//                }
//            }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//















            try{
                Workbook wb =  WorkbookFactory.create(file);
//                Workbook workBook = new SXSSFWorkbook(200);

                Session session= HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Sheet sheet=wb.getSheetAt(0);
                int i=1;
                Row row=sheet.getRow(i);
                int o=0;
                while (row!=null){
//                    if (o==0){
//                        session.beginTransaction();
//                    }
                    Address address=new Address();
                    try{
                        if (row.getCell(0)!=null && getCellStringValueCusom(row.getCell(0))!=null)
                            address.setRayon(getCellStringValueCusom(row.getCell(0)));
                        if (row.getCell(1)!=null && getCellStringValueCusom(row.getCell(1))!=null)
                            address.setKv(getCellStringValueCusom(row.getCell(1)));
                        if (row.getCell(2)!=null && getCellStringValueCusom(row.getCell(2))!=null)
                            address.setStreet(getCellStringValueCusom(row.getCell(2)));
                        if (row.getCell(3)!=null && getCellStringValueCusom(row.getCell(3))!=null)
                            address.setHouseNumber(getCellStringValueCusom(row.getCell(3)));
                        if (row.getCell(4)!=null && getCellStringValueCusom(row.getCell(4))!=null)
                            address.setApartmentCount(new Long(getCellStringValueCusom(row.getCell(4))));
                        if (row.getCell(5)!=null && getCellStringValueCusom(row.getCell(5))!=null)
                            address.setPostboxType(getCellStringValueCusom(row.getCell(5)));
                        if (row.getCell(6)!=null && getCellStringValueCusom(row.getCell(6))!=null)
                            address.setPostboxQuality(getCellStringValueCusom(row.getCell(6)));
                        if (row.getCell(7)!=null && getCellStringValueCusom(row.getCell(7))!=null)
                            address.setHouseQuality(getCellStringValueCusom(row.getCell(7)));
                        if (row.getCell(8)!=null && getCellStringValueCusom(row.getCell(8))!=null)
                            address.setLevelCount(getCellStringValueCusom(row.getCell(8)));
                        if (row.getCell(9)!=null && getCellStringValueCusom(row.getCell(9))!=null)
                            address.setPorchCount(new Long(getCellStringValueCusom(row.getCell(9))));
                        if (row.getCell(10)!=null && getCellStringValueCusom(row.getCell(10))!=null)
                            address.setCityRayon(getCellStringValueCusom(row.getCell(10)));
//                        if (row.getCell(11)!=null && getCellStringValueCusom(row.getCell(11))!=null)
//                            address.setHouseYear(getCellStringValueCusom(row.getCell(11)));
//                        if (row.getCell(12)!=null && getCellStringValueCusom(row.getCell(12))!=null)
//                            address.setKey(getCellStringValueCusom(row.getCell(12)));
//                        if (row.getCell(13)!=null && getCellStringValueCusom(row.getCell(13))!=null)
//                            address.setComment(getCellStringValueCusom(row.getCell(13)));
//                        if (row.getCell(14)!=null && getCellStringValueCusom(row.getCell(14))!=null)
//                            address.setLastUpdate(getCellStringValueCusom(row.getCell(14)));
//                        if (row.getCell(15)!=null && getCellStringValueCusom(row.getCell(15))!=null)
//                            address.setNovostroyka(getCellStringValueCusom(row.getCell(15)));
                        session.save(address);
                        i++;
//                        if (o==100){
//                            session.getTransaction().commit();
//                            o=0;
//                        }
                        row=sheet.getRow(i);
                        address=null;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
//                if (o!=0){
                    session.getTransaction().commit();
//                }
                setDefault();
//                renderGrid();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



}
