package com.company;

import java.util.Comparator;

public class SearchResult {

    private int hotel_id;
    private String name;
    private boolean pool;
    private boolean restaurant;
    private boolean entertainment;
    private boolean kidsClub;
    private float distanceToBeach;
    private float distanceToCenter;
    private float reviewScore;
    private int numberOfPeople;
    private int rooms;
    private int pricePerNight;
    private int totalCost;

    public SearchResult(int hotel_id, String name, boolean pool, boolean restaurant, boolean entertainment, boolean kidsClub, float distanceToBeach, float distanceToCenter, float reviewScore, int numberOfPeople, int rooms, int pricePerNight, int totalCost) {
        this.hotel_id = hotel_id;
        this.name = name;
        this.pool = pool;
        this.restaurant = restaurant;
        this.entertainment = entertainment;
        this.kidsClub = kidsClub;
        this.distanceToBeach = distanceToBeach;
        this.distanceToCenter = distanceToCenter;
        this.reviewScore = reviewScore;
        this.numberOfPeople = numberOfPeople;
        this.rooms = rooms;
        this.pricePerNight = pricePerNight;
        this.totalCost = totalCost;
    }

    public static Comparator<SearchResult> sortByPrice = (a, b) -> {
        if (a.getPricePerNight() > b.getPricePerNight()) {
            return 1;
        } else if (a.getPricePerNight() < b.getPricePerNight()){
            return -1;
        } else return 0;
    };

    public static Comparator<SearchResult> sortByReview = (a, b) -> {
        if (a.getReviewScore() < b.getReviewScore()) {
            return 1;
        } else if (a.getReviewScore() > b.getReviewScore()){
            return -1;
        } else return 0;
    };

    public static Comparator<SearchResult> sortByID = (a, b) -> {
        if (a.getHotel_id() > b.getHotel_id()) {
            return 1;
        } else if (a.getHotel_id() < b.getHotel_id()){
            return -1;
        } else return 0;
    };

    public String toString(){
        return hotel_id + ". Boende: " + name
                + ", Pool: " + pool
                + ", Restaurang: " + restaurant
                + ", Kvällsunderhållning: " + entertainment
                + ", Barnklubb: " + kidsClub
                + ", Avstånd till strand: " + distanceToBeach
                + ", Avstånd till centrum: " + distanceToCenter
                + ", Omdöme: " + reviewScore
                + ", Lediga rum för " + numberOfPeople + " personer: " + rooms
                + ", Pris per natt: " + pricePerNight
                + ", Totalkostnad: " + totalCost + "\n";
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public float getReviewScore() {
        return reviewScore;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }
}
