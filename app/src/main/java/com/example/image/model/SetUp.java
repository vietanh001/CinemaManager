package com.example.image.model;

import java.io.Serializable;
import java.util.List;

public class SetUp implements Serializable {
    private String maPhim, diaDiem,soPhong, ngayChieu, gioChieu, hinhThuc;
    private List<List<String>> list;

    public SetUp() {
    }

    public SetUp(String maPhim, String diaDiem, String soPhong, String ngayChieu, String gioChieu, String hinhThuc, List<List<String>> list) {
        this.maPhim = maPhim;
        this.diaDiem = diaDiem;
        this.soPhong = soPhong;
        this.ngayChieu = ngayChieu;
        this.gioChieu = gioChieu;
        this.hinhThuc = hinhThuc;
        this.list = list;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public String getGioChieu() {
        return gioChieu;
    }

    public void setGioChieu(String gioChieu) {
        this.gioChieu = gioChieu;
    }

    public String getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(String maPhim) {
        this.maPhim = maPhim;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(String ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public List<List<String>> getList() {
        return list;
    }

    public void setList(List<List<String>> list) {
        this.list = list;
    }
}
