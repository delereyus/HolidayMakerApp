package com.company;

public class Bokning {

    private int bokningsId;
    private int kundId;
    private int antalPersoner;
    private MealPlan måltider;
    private boolean extraSäng;

    public Bokning(int bokningsId, int kundId, int antalPersoner, MealPlan måltider, boolean extraSäng) {
        this.bokningsId = bokningsId;
        this.kundId = kundId;
        this.antalPersoner = antalPersoner;
        this.måltider = måltider;
        this.extraSäng = extraSäng;
    }

    public void setMåltider(MealPlan måltider) {
        this.måltider = måltider;
    }

    public void setExtraSäng(boolean extraSäng) {
        this.extraSäng = extraSäng;
    }

    public int getBokningsId() {
        return bokningsId;
    }

    public int getKundId() {
        return kundId;
    }

    public int getAntalPersoner() {
        return antalPersoner;
    }

    public MealPlan getMåltider() {
        return måltider;
    }

    public boolean isExtraSäng() {
        return extraSäng;
    }
}
