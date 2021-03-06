package com.globalgrupp.courier.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lenovo on 27.01.2016.
 */
@Entity
@Table(name = "couriers")
public class Courier {

    @Id
    @Column(name = "courier_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="app_push_id")
    private String appPushId;

    @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL,mappedBy = "courier")
    private Set<Task> tasks=new HashSet<Task>(0);

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Courier() {
    }

    public String getAppPushId() {
        return appPushId;
    }

    public void setAppPushId(String appPushId) {
        this.appPushId = appPushId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if (getName()!=null)
            return "Курьер "+getId().toString()+"("+getName()+")";
        else{
            return "Курьер "+getId().toString();
        }
    }
}
