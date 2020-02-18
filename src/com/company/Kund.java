package com.company;

public class Kund {

    private int kundId;
    private String namn;
    private String eMail;
    private String teleNr;

    public Kund(int kundId, String namn, String eMail, String teleNr){
        this.kundId = kundId;
        this.namn = namn;
        this.eMail = eMail;
        this.teleNr = teleNr;
    }

    public int getKundId() {
        return kundId;
    }

    public String getNamn() {
        return namn;
    }

    public String geteMail() {
        return eMail;
    }

    public String getTeleNr() {
        return teleNr;
    }
}
