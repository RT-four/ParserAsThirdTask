package com.example.ParserAsThirdTask;

public class Card {
    private String name;
    private String price;
    private String address;
    private String description;

    public Card(String name, String price, String address, String description) {
        this.name = name;
        this.price = price;
        this.address = address;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
