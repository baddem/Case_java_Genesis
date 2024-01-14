package com.gensis.model;


import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name="company")
public class Company {
    @Id
    private String vat;
    private String address;
    private ArrayList<Long> employeeList;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Long> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(Long employeeID) {
        if ((employeeList == null)) {
            employeeList = new ArrayList<>();
        }
        this.employeeList.add(employeeID);
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }
}
