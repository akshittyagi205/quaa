package com.quanutrition.app.payments;

import java.util.ArrayList;

public class PaymentHistoryModel {

    private String programName,programDuration,amount,discount,paymentMode,programId,purchaseDate;
    private boolean extension,renewal;

    private ArrayList<PaymentReceiptModel> receipts;

    public PaymentHistoryModel(String programName, String programDuration, String amount, String discount, String paymentMode, String programId, String purchaseDate, boolean extension, boolean renewal) {
        this.programName = programName;
        this.programDuration = programDuration;
        this.amount = amount;
        this.discount = discount;
        this.paymentMode = paymentMode;
        this.programId = programId;
        this.purchaseDate = purchaseDate;
        this.extension = extension;
        this.renewal = renewal;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramDuration() {
        return programDuration;
    }

    public void setProgramDuration(String programDuration) {
        this.programDuration = programDuration;
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public boolean isExtension() {
        return extension;
    }

    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    public boolean isRenewal() {
        return renewal;
    }

    public void setRenewal(boolean renewal) {
        this.renewal = renewal;
    }

    public ArrayList<PaymentReceiptModel> getReceipts() {
        return receipts;
    }

    public void setReceipts(ArrayList<PaymentReceiptModel> receipts) {
        this.receipts = receipts;
    }
}


