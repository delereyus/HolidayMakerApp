package com.company;

public class Boende {

    private int boendeId;
    private String namn;
    private boolean hasPool;
    private boolean hasRestaurant;
    private boolean hasEntertainment;
    private boolean hasKidsClub;
    private float distanceToBeach;
    private float distanceToCenter;
    private float reviewScore;

    public Boende(int boendeId, String namn, boolean hasPool, boolean hasRestaurant, boolean hasEntertainment, boolean hasKidsClub, float distanceToBeach, float distanceToCenter) {
        this.boendeId = boendeId;
        this.namn = namn;
        this.hasPool = hasPool;
        this.hasRestaurant = hasRestaurant;
        this.hasEntertainment = hasEntertainment;
        this.hasKidsClub = hasKidsClub;
        this.distanceToBeach = distanceToBeach;
        this.distanceToCenter = distanceToCenter;
    }

    public void setReviewScore(float reviewScore) {
        this.reviewScore = reviewScore;
    }

    public int getBoendeId() {
        return boendeId;
    }

    public String getNamn() {
        return namn;
    }

    public boolean isHasPool() {
        return hasPool;
    }

    public boolean isHasRestaurant() {
        return hasRestaurant;
    }

    public boolean isHasEntertainment() {
        return hasEntertainment;
    }

    public boolean isHasKidsClub() {
        return hasKidsClub;
    }

    public float getDistanceToBeach() {
        return distanceToBeach;
    }

    public float getDistanceToCenter() {
        return distanceToCenter;
    }

    public float getReviewScore() {
        return reviewScore;
    }
}
