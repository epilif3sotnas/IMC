package com.example.imc.classes;

public class objData {
    private String date;
    private String age;
    private String height;
    private String weight;
    private String imc;

    public objData(String date, String age, String height, String weight, String imc){
        this.date = date;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.imc = imc;
    }
    public String getDate() {
        return date;
    }
    public String getAge() {
        return age;
    }
    public String getHeight() {
        return height;
    }
    public String getWeight() {
        return weight;
    }
    public String getImc() {
        return imc;
    }
}
