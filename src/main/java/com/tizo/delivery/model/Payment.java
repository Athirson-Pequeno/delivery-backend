package com.tizo.delivery.model;

import com.tizo.delivery.model.enums.PaymentMethod;
import com.tizo.delivery.model.enums.PaymentStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

@Embeddable
public class Payment {
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal fee;
    private BigDecimal finalAmount;

    public Payment() {
    }

    public Payment(PaymentMethod method, PaymentStatus paymentStatus, BigDecimal totalAmount, BigDecimal discount, BigDecimal fee, BigDecimal finalAmount) {
        this.method = method;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.fee = fee;
        this.finalAmount = finalAmount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }
}
