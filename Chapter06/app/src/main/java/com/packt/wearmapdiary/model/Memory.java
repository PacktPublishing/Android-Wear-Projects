package com.packt.wearmapdiary.model;

import java.io.Serializable;

/**
 * Created by ashok.kumar on 12/03/17.
 */

public class Memory implements Serializable {
    public long id;
    public double latitude;
    public double longitude;
    public String city;
    public String country;
    public String notes;
}
