package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.*;
import com.globalgrupp.courier.util.HibernateUtil;
import org.apache.commons.io.IOUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public void getTaskById(@RequestBody TaskResult result){
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
