package com.example.image.model;

public class User {
    private String ho, ten, sDT, diaChi, username;

    public User() {
    }

    public User(String ho, String ten, String sDT, String diaChi, String username) {
        this.ho = ho;
        this.ten = ten;
        this.sDT = sDT;
        this.diaChi = diaChi;
        this.username = username;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getsDT() {
        return sDT;
    }

    public void setsDT(String sDT) {
        this.sDT = sDT;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
