package com.example.image.model;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {
    private String tenPhong;
    private List<List<String>> list;

    public Room() {
    }

    public Room(String tenP, List<List<String>> list) {
        this.tenPhong = tenP;
        this.list = list;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenP(String tenP) {
        this.tenPhong = tenP;
    }

    public List<List<String>> getList() {
        return list;
    }

    public void setList(List<List<String>> list) {
        this.list = list;
    }

}
