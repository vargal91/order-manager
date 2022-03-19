package butorszallitas;

/**
 *
 * @author varglaszlo
 */
public class Naplo {

    private String nev;
    private int osszeg;
    private String statusz;

    public Naplo(String nev, int osszeg, String statusz) {
        this.nev = nev;
        this.osszeg = osszeg;
        this.statusz = statusz;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public int getOsszeg() {
        return osszeg;
    }

    public void setOsszeg(int osszeg) {
        this.osszeg = osszeg;
    }

    public String getStatusz() {
        return statusz;
    }

    public void setStatusz(String statusz) {
        this.statusz = statusz;
    }

}
