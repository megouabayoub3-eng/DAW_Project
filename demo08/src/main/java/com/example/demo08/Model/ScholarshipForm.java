package com.example.demo08.Model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ScholarshipForm {

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank
    @Size(max = 1000)
    private String reason;

    public ScholarshipForm() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
