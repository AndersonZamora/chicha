package com.example.lasesteras.model;

import java.io.Serializable;

public class dishesModel implements Serializable {

    String namePlate;
    String pricePlate;
    String quantity;
    String totalQuantity;
    String total;
    String available;
    String childId;
    String plateState;

    public dishesModel() {
    }

    public dishesModel(String namePlate, String namePrice, String quantity, String totalQuantity, String total, String available, String plateState) {
        this.namePlate = namePlate;
        this.pricePlate = namePrice;
        this.quantity = quantity;
        this.totalQuantity = totalQuantity;
        this.total = total;
        this.available = available;
        this.plateState = plateState;
    }

    public String getNamePlate() {
        return namePlate;
    }

    public void setNamePlate(String namePlate) {
        this.namePlate = namePlate;
    }

    public String getPricePlate() {
        return pricePlate;
    }

    public void setPricePlate(String pricePlate) {
        this.pricePlate = pricePlate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getPlateState() {
        return plateState;
    }

    public void setPlateState(String plateState) {
        this.plateState = plateState;
    }
}
