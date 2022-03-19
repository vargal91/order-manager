/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package butorszallitas;

public class Szallitas {

    private int id;
    private int mennyiseg;
    private String nev;
    private String elerhetoseg;
    private String datum;
    private int tav;
    private int ar;

    public Szallitas(int id, String nev, int tav, int mennyiseg, String datum, String elerhetoseg, int ar) {
        this.id = id;
        this.mennyiseg = mennyiseg;
        this.nev = nev;
        this.elerhetoseg = elerhetoseg;
        this.datum = datum;
        this.tav = tav;
        this.datum = datum;
        this.ar = ar;
    }

    @Override

    public String toString() {
        return nev + ";" + mennyiseg + ";" + tav + ";" + datum + ";" + elerhetoseg + ";" + ar;
    }

    public int getAr() {
        return ar;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public int getTav() {
        return tav;
    }

    public void setTav(int tav) {
        this.tav = tav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMennyiseg() {
        return mennyiseg;
    }

    public void setMennyiseg(int mennyiseg) {
        this.mennyiseg = mennyiseg;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getElerhetoseg() {
        return elerhetoseg;
    }

    public void setElerhetoseg(String elerhetoseg) {
        this.elerhetoseg = elerhetoseg;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

}
