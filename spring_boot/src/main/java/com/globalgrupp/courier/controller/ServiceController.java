package com.globalgrupp.courier.controller;

import com.globalgrupp.courier.model.*;
import com.globalgrupp.courier.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

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
}
