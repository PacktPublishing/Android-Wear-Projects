package com.packt.upbeat.utils;

/**
 * Created by ashok.kumar on 28/05/17.
 */

public class HealthTipsItem {


    public String Title;
    public String MoreInfo;

    public HealthTipsItem(String title, String moreInfo) {
        Title = title;
        MoreInfo = moreInfo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMoreInfo() {
        return MoreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        MoreInfo = moreInfo;
    }
}
