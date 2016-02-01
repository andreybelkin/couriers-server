package com.globalgrupp.courier.model;

/**
 * Created by п on 01.02.2016.
 */
public class ResultFilter {
    private String appPushId;

    private Long taskAddressResultLinkId;

    public ResultFilter() {
    }

    public String getAppPushId() {
        return appPushId;
    }

    public void setAppPushId(String appPushId) {
        this.appPushId = appPushId;
    }

    public Long getTaskAddressResultLinkId() {
        return taskAddressResultLinkId;
    }

    public void setTaskAddressResultLinkId(Long taskAddressResultLinkId) {
        this.taskAddressResultLinkId = taskAddressResultLinkId;
    }
}
