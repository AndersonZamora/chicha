package com.example.lasesteras.model;

import java.util.List;

public class reports {

    String date;
    List<String> total;

    public reports() {

    }

    public reports(String date, List<String> total) {
        this.date = date;
        this.total = total;
    }

    public List<String> getTotal() {
        return total;
    }

    public void setTotal(List<String> total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
