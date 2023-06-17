package com.example.image.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private String ma;
    private String tenP;
    private String theLoai;
    private String daoDien;
    private String dienVien;
    private String ngayPH;
    private int thoiLuong;
    private String noiDung;
    private String ngonNgu;
    private String kiemDuyet;
    private String anh;
    private String trailer;
    private String yeuThich = "7.0";

    public Movie() {
    }

    public Movie(String ma, String tenP, String theLoai, String daoDien, String dienVien, String ngayPH, int thoiLuong, String noiDung, String ngonNgu, String kiemDuyet, String anh, String trailer) {
        this.ma = ma;
        this.tenP = tenP;
        this.theLoai = theLoai;
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.ngayPH = ngayPH;
        this.thoiLuong = thoiLuong;
        this.noiDung = noiDung;
        this.ngonNgu = ngonNgu;
        this.kiemDuyet = kiemDuyet;
        this.anh = anh;
        this.trailer = trailer;
    }

    public Movie(String ma, String tenP, String theLoai, String daoDien, String dienVien, String ngayPH, int thoiLuong, String noiDung, String ngonNgu, String kiemDuyet, String anh, String trailer, String yeuThich) {
        this.ma = ma;
        this.tenP = tenP;
        this.theLoai = theLoai;
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.ngayPH = ngayPH;
        this.thoiLuong = thoiLuong;
        this.noiDung = noiDung;
        this.ngonNgu = ngonNgu;
        this.kiemDuyet = kiemDuyet;
        this.anh = anh;
        this.trailer = trailer;
        this.yeuThich = yeuThich;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenP() {
        return tenP;
    }

    public void setTenP(String tenP) {
        this.tenP = tenP;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public String getDienVien() {
        return dienVien;
    }

    public void setDienVien(String dienVien) {
        this.dienVien = dienVien;
    }

    public String getNgayPH() {
        return ngayPH;
    }

    public void setNgayPH(String ngayPH) {
        this.ngayPH = ngayPH;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgonNgu() {
        return ngonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        this.ngonNgu = ngonNgu;
    }

    public String getKiemDuyet() {
        return kiemDuyet;
    }

    public void setKiemDuyet(String kiemDuyet) {
        this.kiemDuyet = kiemDuyet;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getYeuThich() {
        return yeuThich;
    }

    public void setYeuThich(String yeuThich) {
        this.yeuThich = yeuThich;
    }
}
