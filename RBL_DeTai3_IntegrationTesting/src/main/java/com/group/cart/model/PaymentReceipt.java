package com.group.cart.model;

import java.util.Objects;

public class PaymentReceipt {
    private String status;
    private double originalAmount;
    private double discountAmount;
    private double netAmount;
    private double fee;
    private double totalAmount;
    private String paymentMethod;
    private String message;

    public PaymentReceipt() {
    }

    public PaymentReceipt(String status, double totalAmount) {
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public PaymentReceipt(String status, double originalAmount, double discountAmount,
            double netAmount, double fee, double totalAmount,
            String paymentMethod, String message) {
        this.status = status;
        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.netAmount = netAmount;
        this.fee = fee;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.message = message;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public double getFee() {
        return fee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTotalPayable() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalPayable(double totalPayable) {
        this.totalAmount = totalPayable;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PaymentReceipt{" +
                "status='" + status + '\'' +
                ", originalAmount=" + originalAmount +
                ", discountAmount=" + discountAmount +
                ", netAmount=" + netAmount +
                ", fee=" + fee +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PaymentReceipt that = (PaymentReceipt) o;
        return Double.compare(that.originalAmount, originalAmount) == 0 &&
                Double.compare(that.discountAmount, discountAmount) == 0 &&
                Double.compare(that.netAmount, netAmount) == 0 &&
                Double.compare(that.fee, fee) == 0 &&
                Double.compare(that.totalAmount, totalAmount) == 0 &&
                Objects.equals(status, that.status) &&
                Objects.equals(paymentMethod, that.paymentMethod) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, originalAmount, discountAmount, netAmount, fee, totalAmount, paymentMethod,
                message);
    }
}
