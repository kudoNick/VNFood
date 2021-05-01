package vn.poly.jeanshop.src.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class changeprofile {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("username")
    @Expose
    private String username;

   public changeprofile(){}

    public changeprofile(String userId, String phone, String address, String username) {
        this.userId = userId;
        this.phone = phone;
        this.address = address;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


