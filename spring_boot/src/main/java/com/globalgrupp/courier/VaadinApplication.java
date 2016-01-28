package com.globalgrupp.courier;

/**
 * Created by Lenovo on 27.01.2016.
 */

import com.globalgrupp.courier.model.Address;
import com.globalgrupp.courier.util.HibernateUtil;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.Session;
import org.vaadin.spring.annotation.VaadinUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

//@Theme("valo")
//@SpringUI
@VaadinUI
public class VaadinApplication extends  UI{
    private static final long serialVersionUID = 1L;

    final Embedded image = new Embedded("Uploaded Image");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        image.setVisible(false);
        ImageUploader receiver = new ImageUploader();
        Upload upload = new Upload("Upload Here", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);
        upload.setReceiver(receiver);

// Put the components in a panel
        Panel panel = new Panel();
        Layout panelContent = new VerticalLayout();
        panelContent.addComponents(upload, image);
        panel.setContent(panelContent);
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
                file = new File("/temp/uploads/" + filename);
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
            try{
                Workbook wb = WorkbookFactory.create(file);

                Session session= HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Sheet sheet=wb.getSheetAt(0);
                int i=1;
                Row row=sheet.getRow(i);

                while (row!=null){
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
                        if (row.getCell(11)!=null && getCellStringValueCusom(row.getCell(11))!=null)
                            address.setHouseYear(getCellStringValueCusom(row.getCell(11)));
                        if (row.getCell(12)!=null && getCellStringValueCusom(row.getCell(12))!=null)
                            address.setKey(getCellStringValueCusom(row.getCell(12)));
                        if (row.getCell(13)!=null && getCellStringValueCusom(row.getCell(13))!=null)
                            address.setComment(getCellStringValueCusom(row.getCell(13)));
                        if (row.getCell(14)!=null && getCellStringValueCusom(row.getCell(14))!=null)
                            address.setLastUpdate(getCellStringValueCusom(row.getCell(14)));
                        if (row.getCell(15)!=null && getCellStringValueCusom(row.getCell(15))!=null)
                            address.setNovostroyka(getCellStringValueCusom(row.getCell(15)));
                        session.save(address);
                        i++;
                        row=sheet.getRow(i);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                session.getTransaction().commit();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
