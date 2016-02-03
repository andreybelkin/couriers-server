package com.globalgrupp.courier.model;

/**
 * Created by п on 03.02.2016.
 */
public class TotalReport {

    private Courier courier;

    private Task task;

    private String taskStartDate;

    private String taskEndDate;

    private String addressCount;

    private String porchCount;

    private String courierAddressCount;

    private String porchAddressCount;

    public TotalReport() {
    }

    public String getAddressCount() {
        return addressCount;
    }

    public void setAddressCount(String addressCount) {
        this.addressCount = addressCount;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public String getCourierAddressCount() {
        return courierAddressCount;
    }

    public void setCourierAddressCount(String courierAddressCount) {
        this.courierAddressCount = courierAddressCount;
    }

    public String getPorchAddressCount() {
        return porchAddressCount;
    }

    public void setPorchAddressCount(String porchAddressCount) {
        this.porchAddressCount = porchAddressCount;
    }

    public String getPorchCount() {
        return porchCount;
    }

    public void setPorchCount(String porchCount) {
        this.porchCount = porchCount;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(String taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public String getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(String taskStartDate) {
        this.taskStartDate = taskStartDate;
    }
}
