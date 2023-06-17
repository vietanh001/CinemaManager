package com.example.image.model;

public class Ticket {
    private String username;
    private String hoTen;
    private String tenPhim;
    private String dinhDang;
    private String ngayDat;
    private String gioDat;
    private String diaDiem;
    private String ngayChieu;
    private String phongChieu;
    private String gioChieu;
    private String ghe;
    private String tongTien;

    public Ticket() {
    }

    public Ticket(String username, String hoTen, String tenPhim, String dinhDang, String ngayDat, String gioDat, String diaDiem, String ngayChieu, String phongChieu, String gioChieu, String ghe, String tongTien) {
        this.username = username;
        this.hoTen = hoTen;
        this.tenPhim = tenPhim;
        this.dinhDang = dinhDang;
        this.ngayDat = ngayDat;
        this.gioDat = gioDat;
        this.diaDiem = diaDiem;
        this.ngayChieu = ngayChieu;
        this.phongChieu = phongChieu;
        this.gioChieu = gioChieu;
        this.ghe = ghe;
        this.tongTien = tongTien;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getDinhDang() {
        return dinhDang;
    }

    public void setDinhDang(String dinhDang) {
        this.dinhDang = dinhDang;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getGioDat() {
        return gioDat;
    }

    public void setGioDat(String gioDat) {
        this.gioDat = gioDat;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public String getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(String ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public String getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(String phongChieu) {
        this.phongChieu = phongChieu;
    }

    public String getGioChieu() {
        return gioChieu;
    }

    public void setGioChieu(String gioChieu) {
        this.gioChieu = gioChieu;
    }

    public String getGhe() {
        return ghe;
    }

    public void setGhe(String ghe) {
        this.ghe = ghe;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }
}
