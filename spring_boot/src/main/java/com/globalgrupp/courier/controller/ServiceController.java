package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.*;
import com.globalgrupp.courier.util.HibernateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by п on 01.02.2016.
 */
@RestController
@RequestMapping("/service")

public class ServiceController  {


    @RequestMapping(value="/testMethod",method= RequestMethod.GET)
    Collection<String> getSmth(){
        Collection<String> res=new ArrayList<>(2);
        res.add("adsfadf");
        res.add("adsfadfasdfadf");

        return res;
    }

    @RequestMapping(value="/savePushAppId/{appId}", method= RequestMethod.POST)
    public void savePushAppId(@PathVariable("appId") String pushAppId){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Courier where app_push_id=:pushAppId");
        query.setParameter("pushAppId",pushAppId);
        List<Courier> users=query.list();
        Courier fUser;
        if (users.size()==1){
            fUser=users.get(0);
            //такой ключ уже есть
        } else {
            fUser=new Courier();
            fUser.setAppPushId(pushAppId);
            session.beginTransaction();
            session.save(fUser);
            session.getTransaction().commit();
        }
    }

    @RequestMapping(value="/getMyTasks/{appId}", method= RequestMethod.GET)
    public List<Task> getMyTasks(@PathVariable("appId") String pushAppId){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from Courier where app_push_id=:pushAppId");
        query.setParameter("pushAppId",pushAppId);
        List<Courier> users=query.list();
        Courier fUser;
        if (users.size()==1){
            fUser=users.get(0);
            query=session.createQuery("from Task where courier_id=:courierId");
            query.setParameter("courierId",fUser.getId());
            List<Task> tasks=query.list();
            return tasks;
            //такой ключ уже есть
        } else {
            return new ArrayList<Task>();
        }
    }

    @RequestMapping(value="/getTaskById/{appId}", method= RequestMethod.GET)
    public List<Task> getTaskById(@PathVariable("appId") String pushAppId){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query    query=session.createQuery("from Task where task_id=:taskId");
        query.setParameter("taskId",pushAppId);
        List<Task> tasks=query.list();
        return tasks;
    }

    @RequestMapping(value="/getTaskResult", method= RequestMethod.POST)
    public List<Task> getTaskById(@RequestBody ResultFilter filter){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query    query=session.createQuery("from TaskResult where task_adresses_id=:taskId");
        query.setParameter("taskId",filter.getTaskAddressResultLinkId());
        List<Task> tasks=query.list();
        return tasks;
    }

    @RequestMapping(value="/createResult", method= RequestMethod.POST)
    public void createResult(@RequestBody TaskResult result){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("from TaskAddressResultLink where id=:id ");
        query.setParameter("id",result.getTaskAddressResultLinkId());
        List<TaskAddressResultLink> taskAddressResultLink= query.list();
        result.setTaskAddressResultLink(taskAddressResultLink.get(0));
        session.beginTransaction();
        session.save(result);
        session.getTransaction().commit();

    }

    @RequestMapping(value="/uploadFile", method=RequestMethod.PUT)
    @ResponseBody
    public Long convert(InputStream file){
        try {
            byte[] bytes = IOUtils.toByteArray(file);//new byte[1024];
            LoadedFile lf =new LoadedFile();
            lf.setData(bytes);
            Session session= HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(lf);
            session.getTransaction().commit();
            return lf.getId();
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/getFile/{file_name}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("file_name") Long id,
            HttpServletResponse response) {
        try {
            // get your file as InputStream
            Session session= HibernateUtil.getSessionFactory().openSession();
            LoadedFile lf=(LoadedFile)session.get(LoadedFile.class,id);
            InputStream is = new ByteArrayInputStream(lf.getData());
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }


    @RequestMapping(value = "/getArchive/{result_id}", method = RequestMethod.GET)
    public void getArchive(
            @PathVariable("result_id") Long id,
            HttpServletResponse response) {
        try {
            // get your file as InputStream
            Session session= HibernateUtil.getSessionFactory().openSession();
//            TaskResult taskResult=(TaskResult)session.get(TaskResult.class,id);

            Task task=(Task)session.get(Task.class,id);
            List<TaskAddressResultLink> taskAddressResultLinks= new ArrayList<>(task.getTaskAddressResultLinks());
            taskAddressResultLinks.size();
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
            for (int i=0;i<taskAddressResultLinks.size();i++){
                if (taskAddressResultLinks.get(i).getResults()!=null &&taskAddressResultLinks.get(i).getResults().size()>0){
                    String street=taskAddressResultLinks.get(i).getAddress().getStreet()
                            +""+taskAddressResultLinks.get(i).getAddress().getHouseNumber();
                    String taskName=task.getDescription();
                    List<TaskResult> taskResultList=new ArrayList<>(taskAddressResultLinks.get(i).getResults());
                    int f=1;
                    for (int k=0;k<taskResultList.size();k++){
                        List<Long> photoIds=taskResultList.get(k).getPhotoIds();
                        for(int z=0;z<photoIds.size();z++){
                            LoadedFile lf=(LoadedFile)session.get(LoadedFile.class,photoIds.get(z));
                            ZipEntry zipEntry = new ZipEntry(taskName+"_"+street+"_"+(f)+".jpg");
                            zipOutputStream.putNextEntry(zipEntry);
                            zipOutputStream.write(lf.getData());
                            f++;
                        }
                    }
                }
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());


            String fileName =URLEncoder.encode(task.getDescription()+".zip", "UTF-8");
            // copy it to response's OutputStream
            response.setHeader("Content-disposition", "attachment; filename=\""+ fileName+"\"");

            org.apache.commons.io.IOUtils.copy(byteArrayInputStream,response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();

            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }



    @RequestMapping(value = "/getCourierInfo/{app_id}", method = RequestMethod.GET)
    public Courier getCourierInfo(
            @PathVariable("app_id") String id,
            HttpServletResponse response) {
        try {
            // get your file as InputStream
            Session session= HibernateUtil.getSessionFactory().openSession();
            Query query=session.createQuery("from Courier where app_push_id=:appId");
            query.setParameter("appId",id);
            List<Courier> courierList=query.list();
            if (courierList.size()==0){
                //// TODO: 04.02.2016  wtf wrong appId?
                return null;
            }else {
                return courierList.get(0);
            }
        } catch (Exception ex) {
            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            ex.printStackTrace();
            throw ex;
        }

    }



}
