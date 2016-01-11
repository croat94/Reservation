package com.rafo.reservation;

/**
 * Created by Ja sam Rafoooo on 5.1.2016..
 */
public class Rezervacija {

    private String naziv;
    private String inventoryNumber;
    private String startDate;
    private String endDate;
    private boolean vraceno;
    private String createdAt;

    public Rezervacija(String naziv, String inventoryNumber, String startDate, int vraceno, String createdAt) {
        this.startDate = startDate;
        this.inventoryNumber = inventoryNumber;
        this.naziv = naziv;
        this.createdAt = createdAt;
        setVraceno(vraceno);
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public boolean getVraceno() {
        return vraceno;
    }

    public void setVraceno(int vraceno) {
        if (vraceno == 0)
            this.vraceno = false;
        else
            this.vraceno = true;
    }
}
