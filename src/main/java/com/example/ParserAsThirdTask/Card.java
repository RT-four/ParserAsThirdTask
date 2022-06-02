package com.example.ParserAsThirdTask;

public class Card {
    private String name;
    private String price;
    private String address;
    private String link;
    private String linkToPicture;

    public Card(String name, String price, String address, String link, String linkToPicture) {
        this.name = name;
        this.price = price;
        this.address = address;
        this.link = link;
        this.linkToPicture = linkToPicture;
    }

    public String getLinkToPicture() {
        return linkToPicture;
    }

    public void setLinkToPicture(String linkToPicture) {
        this.linkToPicture = linkToPicture;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
