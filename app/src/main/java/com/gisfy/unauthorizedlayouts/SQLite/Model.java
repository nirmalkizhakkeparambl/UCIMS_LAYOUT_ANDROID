package com.gisfy.unauthorizedlayouts.SQLite;

public class Model {


    private String Owner;

    public Model(String owner, String fatherName, String phonenumber) {
        Owner = owner;
        FatherName = fatherName;
        Phonenumber = phonenumber;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    private String FatherName;
    private String Phonenumber;

    }