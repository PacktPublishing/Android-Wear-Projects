package com.packt.upbeat.models;

import io.realm.RealmObject;

/**
 * Created by ashok.kumar on 26/05/17.
 */

public class StepCounts extends RealmObject {

    private String ReceivedDateTime;
    private String Data;

    public String getReceivedDateTime() {
        return ReceivedDateTime;
    }

    public void setReceivedDateTime(String receivedDateTime) {
        ReceivedDateTime = receivedDateTime;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
