package com.globalgrupp.courier;

/**
 * Created by Lenovo on 27.01.2016.
 */

import com.globalgrupp.courier.model.Address;
import com.globalgrupp.courier.util.HibernateUtil;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

// Put the components in a panel
        Panel panel = new Panel();
        Layout panelContent = new VerticalLayout();
        panelContent.addComponents(upload, image);
        panel.setContent(panelContent);
        setContent(panel);
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
                Row row=sheet.getRow(1);
                while (row!=null){
                    Address address=new Address();
                    address.setRayon(row.getCell(0).getStringCellValue());
                    address.setKv(row.getCell(1).getStringCellValue());
                    address.setStreet(row.getCell(2).getStringCellValue());
                    address.setHouseNumber(row.getCell(3).getStringCellValue());
                    address.setApartmentCount(new Long(row.getCell(4).getStringCellValue()));
                    address.setPostboxType(row.getCell(5).getStringCellValue());
                    address.setPostboxQuality(row.getCell(6).getStringCellValue());
                    address.setHouseQuality(row.getCell(7).getStringCellValue());
                    address.setLevelCount(row.getCell(8).getStringCellValue());
                    address.setPorchCount(new Long(row.getCell(9).getStringCellValue()));
                    address.setCityRayon(row.getCell(10).getStringCellValue());
                    address.setHouseYear(new Long(row.getCell(11).getStringCellValue()));
                    address.setKey(row.getCell(12).getStringCellValue());
                    address.setComment(row.getCell(13).getStringCellValue());
                    address.setLastUpdate(row.getCell(14).getStringCellValue());
                    address.setNovostroyka(row.getCell(15).getStringCellValue());
                    session.save(address);
                }
                session.getTransaction().commit();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
