package com.globalgrupp.courier.model;

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
    private Courier courier;

    @ManyToMany(fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name="street_channel",joinColumns = {
            @JoinColumn(name = "channel_id",nullable = false,updatable = false)},
            inverseJoinColumns = {@JoinColumn(name="street_id",nullable = false,updatable = false)}
    )
    private Set<Address> addresses = new HashSet<Address>(0);


    public Task() {
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
}
