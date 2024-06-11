package com.example.IoTDemo.Lamp;

public class LampDTO {
    private String lamp;
    private String status;

    // Constructors
    public LampDTO() {
    }

    public LampDTO(String lamp, String status) {
        this.lamp = lamp;
        this.status = status;
    }

    // Getter and Setter methods
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

    // toString method (optional)
    @Override
    public String toString() {
        return "LampDTO{" +
                "lamp='" + lamp + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

