package vn.poly.jeanshop.src.model;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("orderId")
    @Expose
    private String idOrder;

    @SerializedName("totalAmount")
    @Expose
    private int totalAmount;

    @SerializedName("totalPrice")
    @Expose
    private double totalPrice;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("day")
    @Expose
    private String day;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("update_at")
    @Expose
    private String update_at;

    private List<OrderDetails> orderDetails;


    public Order(String idOrder, int totalAmount, double totalPrice, String status) {
        this.idOrder = idOrder;
        this.totalAmount = totalAmount;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Order() {
    }

    public String getOrderId() {
        return idOrder;
    }

    public void setOrderId(String orderId) {
        this.idOrder = orderId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }
}


