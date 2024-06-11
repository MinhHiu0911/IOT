package com.example.IoTDemo.Lamp;

public class LampStatus {
    private String lamp; // Tên của đèn (ví dụ: "lamp1", "lamp2")
    private String status;

    public String getLamp() {
        return lamp;
    }

    public void setLamp(String lamp) {
        this.lamp = lamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

