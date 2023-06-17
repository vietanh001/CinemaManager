package com.example.image.model;

public class Income {
    private String ngay, soVeBan, doanhThu;

    public Income() {
    }

    public Income(String ngay, String soVeBan, String doanhThu) {
        this.ngay = ngay;
        this.soVeBan = soVeBan;
        this.doanhThu = doanhThu;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getSoVeBan() {
        return soVeBan;
    }

    public void setSoVeBan(String soVeBan) {
        this.soVeBan = soVeBan;
    }

    public String getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(String doanhThu) {
        this.doanhThu = doanhThu;
    }
}
