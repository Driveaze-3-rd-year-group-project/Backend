package com.driveaze.driveaze.entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @Column(name = "booking_id", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookingId;

    @Column(name = "vehicle_no", length =100, nullable = false)
    private String vehicleNo;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "status", length = 100)
    private String status = "waiting";;

    @Column(name = "preferred_date")
    private Date preferredDate;

    @Column(name = "preferred_time")
    private Time preferredTime;

    public Booking() {
    }

    public Booking(int bookingId, String vehicleNo, String brand, String model, String status, Date preferredDate, Time preferredTime) {
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
        return "Booking{" +
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
