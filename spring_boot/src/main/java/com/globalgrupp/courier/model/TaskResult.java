package com.globalgrupp.courier.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by Lenovo on 27.01.2016.
 */
@Entity
@Table(name="task_address_result")
public class TaskResult {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_adresses_id")
    @JsonBackReference
    private TaskAddressResultLink taskAddressResultLink;

    @Transient
    private long taskAddressResultLinkId;

    public long getTaskAddressResultLinkId() {
        return taskAddressResultLinkId;
    }

    public void setTaskAddressResultLinkId(long taskAddressResultLinkId) {
        this.taskAddressResultLinkId = taskAddressResultLinkId;
    }

    @Column(name="result")
    private Long result;

    @Column(name="comment")
    private String comment;

    @Column(name="porch")
    private String porch;

    public TaskResult(String comment, String porch, Long result, TaskAddressResultLink taskAddressResultLink) {
        this.comment = comment;
        this.porch = porch;
        this.result = result;
        this.taskAddressResultLink = taskAddressResultLink;
    }

    public TaskResult() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPorch() {
        return porch;
    }

    public void setPorch(String porch) {
        this.porch = porch;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public TaskAddressResultLink getTaskAddressResultLink() {
        return taskAddressResultLink;
    }

    public void setTaskAddressResultLink(TaskAddressResultLink taskAddressResultLink) {
        this.taskAddressResultLink = taskAddressResultLink;
    }
}
