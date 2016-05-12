package com.example.mahmoud.fcisquare.model;

/**
 * Created by Mahmoud on 4/18/2016.
 */
public class Place {
    String name, category, Taste, numberOfLikes, placeID;

    int likePlaceImage, savePlaceImage;

    public Place(String name, String category, String taste, String numberOfLikes, int likePlaceImage, int savePlaceImage) {
        this.name = name;
        this.category = category;
        Taste = taste;
        this.numberOfLikes = numberOfLikes;
        this.likePlaceImage = likePlaceImage;
        this.savePlaceImage = savePlaceImage;
    }

    public Place(String name, String category, String taste, String numberOfLikes) {
        this.name = name;
        this.category = category;
        Taste = taste;
        this.numberOfLikes = numberOfLikes;
    }

    public Place(String name, String category, String taste) {
        this.name = name;
        this.category = category;
        Taste = taste;
    }

    public Place(String name, String category, String numberOfLikes, int likePlaceImage, int savePlaceImage) {
        this.name = name;
        this.category = category;
        this.numberOfLikes = numberOfLikes;
        this.likePlaceImage = likePlaceImage;
        this.savePlaceImage = savePlaceImage;
    }

    public Place(String place, String text, int like, int save, String placeID) {
        this.name = place;
        this.category = text;
        this.likePlaceImage = like;
        this.savePlaceImage = save;
        this.placeID = placeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTaste() {
        return Taste;
    }

    public void setTaste(String taste) {
        Taste = taste;
    }

    public String getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(String numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public int getLikePlaceImage() {
        return likePlaceImage;
    }

    public void setLikePlaceImage(int likePlaceImage) {
        this.likePlaceImage = likePlaceImage;
    }

    public int getSavePlaceImage() {
        return savePlaceImage;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public void setSavePlaceImage(int savePlaceImage) {
        this.savePlaceImage = savePlaceImage;
    }
}
