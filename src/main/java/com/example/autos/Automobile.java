package com.example.autos;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "automobiles")
public class Automobile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vin;
    private String year;
    private String make;
    private String model;
    private String color;
    private String status;
    private String ownerName;
    private String ownerPhone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Automobile that = (Automobile) o;
        return Objects.equals(vin, that.vin) && Objects.equals(year, that.year) && Objects.equals(make, that.make) && Objects.equals(model, that.model) && Objects.equals(color, that.color) && Objects.equals(status, that.status) && Objects.equals(ownerName, that.ownerName) && Objects.equals(ownerPhone, that.ownerPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin, year, make, model, color, status, ownerName, ownerPhone);
    }

    @Override
    public String toString() {
        return "Automobile{" +
                "id=" + id +
                ", vin='" + vin + '\'' +
                ", year='" + year + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", status='" + status + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                '}';
    }
}
