package com.driveaze.driveaze.dto;

import java.sql.Time;
import java.util.Date;

public class BookingDTO {
    private int bookingId;
    private String vehicleNo;
    private String brand;
    private String model;
    private String status = "waiting";;
    private Date preferredDate;
    private Time preferredTime;

    public BookingDTO() {
    }

    public BookingDTO(int bookingId, String vehicleNo, String brand, String model, String status, Date preferredDate, Time preferredTime) {
        this.bookingId = bookingId;
        this.vehicleNo = vehicleNo;
        this.brand = brand;
        this.model = model;
        this.status = status;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(Date preferredDate) {
        this.preferredDate = preferredDate;
    }

    public Time getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(Time preferredTime) {
        this.preferredTime = preferredTime;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "bookingId=" + bookingId +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", status='" + status + '\'' +
                ", preferredDate=" + preferredDate +
                ", preferredTime=" + preferredTime +
                '}';
    }
}
