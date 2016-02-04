package com.globalgrupp.courier.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lenovo on 27.01.2016.
 */

@Entity
@Table(name="task")
public class Task {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="description")
    private String description;

    @Column(name="period_begin")
    private Date periodBegin;

    @Column(name="period_end")
    private Date periodEnd;

    @ManyToOne
    @JoinColumn(name="courier_id")
    @JsonBackReference
    private Courier courier;


    @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL,mappedBy = "task")
    private Set<TaskAddressResultLink> taskAddressResultLinks = new HashSet<TaskAddressResultLink>(0);


    public Task() {
    }

    public Set<TaskAddressResultLink> getTaskAddressResultLinks() {
        return taskAddressResultLinks;
    }

    public void setTaskAddressResultLinks(Set<TaskAddressResultLink> taskAddressResultLinks) {
        this.taskAddressResultLinks = taskAddressResultLinks;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
