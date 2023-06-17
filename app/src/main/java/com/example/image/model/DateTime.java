package com.example.image.model;

import java.io.Serializable;

public class DateTime implements Serializable {
    private String maP;
    private String hinhThuc;
    private String gioChieu;

    public DateTime() {
    }

    public DateTime(String maP, String hinhThuc, String gioChieu) {
        this.maP = maP;
        this.hinhThuc = hinhThuc;
        this.gioChieu = gioChieu;
    }

    public String getMaP() {
        return maP;
    }

    public void setMaP(String maP) {
        this.maP = maP;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public String getGioChieu() {
        return gioChieu;
    }

    public void setGioChieu(String gioChieu) {
        this.gioChieu = gioChieu;
    }
}
