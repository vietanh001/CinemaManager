package com.example.image.model;

import java.io.Serializable;

public class Date implements Serializable {
    private String thu, ngay;

    public Date() {
    }

    public Date(String thu, String ngay) {
        this.thu = thu;
        this.ngay = ngay;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
}
