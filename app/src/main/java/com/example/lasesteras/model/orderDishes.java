package com.example.lasesteras.model;

import java.util.List;

public class orderDishes {

    String tableName;
    String attended;
    String child;
    String hour;
    List<orderModel> order;

    public orderDishes() {
    }

    public orderDishes(String tableName, List<orderModel> order) {
        this.tableName = tableName;
        this.order = order;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<orderModel> getOrder() {
        return order;
    }

    public void setOrder(List<orderModel> order) {
        this.order = order;
    }

    public String getAttended() {
        return attended;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
