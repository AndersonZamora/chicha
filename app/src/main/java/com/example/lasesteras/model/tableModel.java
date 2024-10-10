package com.example.lasesteras.model;

import java.io.Serializable;
import java.util.List;

public class tableModel implements Serializable {

    String tableName;
    String tableStatus;
    String childId;
    String attended;
    List<dishesModel> order;

    public tableModel() {
    }

    public tableModel(String tableName, String tableStatus, List<dishesModel> order,String childId) {
        this.tableName = tableName;
        this.tableStatus = tableStatus;
        this.order = order;
        this.childId = childId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public List<dishesModel> getModels() {
        return order;
    }

    public void setModels(List<dishesModel> order) {
        this.order = order;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getAttended() {
        return attended;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }

    public List<dishesModel> getOrder() {
        return order;
    }

    public void setOrder(List<dishesModel> order) {
        this.order = order;
    }
}
