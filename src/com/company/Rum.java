package com.company;

public class Rum {

    private int rumId;
    private int antalSängar;
    private int prisKlass;
    private boolean isAvailable;
    private int bokningsId;
    private int boendeId;

    public Rum(int rumId, int antalSängar, int prisKlass, boolean isAvailable, int boendeId) {
        this.rumId = rumId;
        this.antalSängar = antalSängar;
        this.prisKlass = prisKlass;
        this.isAvailable = isAvailable;
        this.boendeId = boendeId;
    }

    public int getRumId() {
        return rumId;
    }

    public int getAntalSängar() {
        return antalSängar;
    }

    public int getPrisKlass() {
        return prisKlass;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getBokningsId() {
        return bokningsId;
    }

    public int getBoendeId() {
        return boendeId;
    }
}
