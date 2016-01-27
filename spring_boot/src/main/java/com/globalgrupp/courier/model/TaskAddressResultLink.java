package com.globalgrupp.courier.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lenovo on 27.01.2016.
 */
@Entity
@Table(name = "task_addresses")
public class TaskAddressResultLink {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    private Task task;

    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL,mappedBy = "taskaddressresultlink")
    private Set<TaskResult> results=new HashSet<TaskResult>(0);

    public TaskAddressResultLink() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<TaskResult> getResults() {
        return results;
    }

    public void setResults(Set<TaskResult> results) {
        this.results = results;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
