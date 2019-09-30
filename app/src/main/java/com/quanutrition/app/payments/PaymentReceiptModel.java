package com.quanutrition.app.payments;

public class PaymentReceiptModel {
    String amount,mode,date;

    public PaymentReceiptModel(String amount, String mode, String date) {
        this.amount = amount;
        this.mode = mode;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
