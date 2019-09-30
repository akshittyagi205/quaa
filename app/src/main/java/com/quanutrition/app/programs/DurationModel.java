package com.quanutrition.app.programs;

public class DurationModel {
    String duration,days,amount,discount;
    boolean has_discount;
    String name;

    public DurationModel(String duration, String days, String amount, String discount, boolean has_discount) {
        this.duration = duration;
        this.days = days;
        this.amount = amount;
        this.discount = discount;
        this.has_discount = has_discount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isHas_discount() {
        return has_discount;
    }

    public void setHas_discount(boolean has_discount) {
        this.has_discount = has_discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
