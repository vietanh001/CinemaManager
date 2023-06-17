package com.example.image.model;

public class Validate {
    private String tenPhim, gioChieu, gioHet;

    public Validate() {
    }

    public Validate(String tenPhim, String gioChieu, String gioHet) {
        this.tenPhim = tenPhim;
        this.gioChieu = gioChieu;
        this.gioHet = gioHet;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getGioChieu() {
        return gioChieu;
    }

    public void setGioChieu(String gioChieu) {
        this.gioChieu = gioChieu;
    }

    public String getGioHet() {
        return gioHet;
    }

    public void setGioHet(String gioHet) {
        this.gioHet = gioHet;
    }
}
