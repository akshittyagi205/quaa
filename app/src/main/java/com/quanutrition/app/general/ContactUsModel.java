package com.quanutrition.app.general;

/**
 * Created by hp on 2/21/2018.
 */

public class ContactUsModel {
    private String title,address,phone,email,addressId;

    public ContactUsModel(String title, String address, String phone, String email, String addressId) {
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.addressId = addressId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
