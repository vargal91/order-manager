package butorszallitas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.ObservableList;

/**
 *
 * @author varglaszlo
 */
public class DB {

    final String dbUrl = "jdbc:mysql://localhost:3306/";
    final String user = "root";
    final String pass = "";
    final String dbUrl2 = "jdbc:mysql://localhost:3306/butornaplo"
            + "?useUnicode=true&characterEncoding=UTF-8";

    public DB() {
        String s1 = "CREATE DATABASE IF NOT EXISTS butornaplo "
                + "DEFAULT CHARSET=utf8mb4 "
                + "COLLATE=utf8mb4_hungarian_ci;";
        String s2 = "USE butornaplo";
        String s3 = "CREATE TABLE IF NOT EXISTS alap ("
                + "id int(4) NOT NULL AUTO_INCREMENT,"
                + "nev varchar(50) COLLATE utf8mb4_hungarian_ci NOT NULL,"
                + "tav int(3) NOT NULL,"
                + "mennyiseg int(10) NOT NULL,"
                + "datum date DEFAULT NULL,"
                + "elerhetoseg varchar(50) COLLATE utf8mb4_hungarian_ci NOT NULL,"
                + "ar int(11) NOT NULL,"
                + "PRIMARY KEY (id),"
                + "UNIQUE KEY foglalt (datum)"
                + ");";
        String s4 = "CREATE TABLE IF NOT EXISTS befejezett ("
                + "nev varchar(50) COLLATE utf8mb4_hungarian_ci NOT NULL,"
                + "osszeg int(11) DEFAULT NULL,"
                + "statusz varchar(9) COLLATE utf8mb4_hungarian_ci"
                + ");";
        try (Connection kapcs = DriverManager.getConnection(dbUrl, user, pass);
                PreparedStatement ekp1 = kapcs.prepareStatement(s1);
                PreparedStatement ekp2 = kapcs.prepareStatement(s2);
                PreparedStatement ekp3 = kapcs.prepareStatement(s3);
                PreparedStatement ekp4 = kapcs.prepareStatement(s4)) {
            ekp1.execute();
            ekp2.execute();
            ekp3.execute();
            ekp4.execute();
        } catch (SQLException ex) {
            Panel.hiba("Létrhezoás", ex.getMessage());
        }
    }

    public void beolvas(ObservableList<Szallitas> tabla) {
        String s = "SELECT* FROM alap ORDER BY datum";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(s)) {
            tabla.clear();
            ResultSet eredmeny = ekp.executeQuery();
            while (eredmeny.next()) {
                tabla.add(new Szallitas(
                        eredmeny.getInt("id"),
                        eredmeny.getString("nev"),
                        eredmeny.getInt("tav"),
                        eredmeny.getInt("mennyiseg"),
                        eredmeny.getString("datum"),
                        eredmeny.getString("elerhetoseg"),
                        eredmeny.getInt("ar")
                ));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int torol(int id) {
        String s = "DELETE FROM alap WHERE id=?;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setInt(1, id);
            return ekp.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public int beir(String nev, int tav, int mennyiseg, LocalDate datum, String elerhetoseg, String ar) {
        String p = "INSERT INTO alap (nev, tav, mennyiseg,datum, elerhetoseg,ar)"
                + " VALUES (?,?,?,?,?,?);";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(p)) {
            ekp.setString(1, nev);
            ekp.setInt(2, tav);
            ekp.setInt(3, mennyiseg);
            ekp.setString(4, datum.toString());
            ekp.setString(5, elerhetoseg);
            ekp.setString(6, ar);
            return ekp.executeUpdate();
        } catch (SQLException ex) {
            Panel.hiba("Hiba", "Már van erre a napra megrendelés, kérlek válasz másik napot!");
            return 0;
        }
    }

    public int beirnaplo(String nev, String osszeg, String statusz) {
        String p = "INSERT INTO befejezett (nev,  osszeg, statusz)"
                + " VALUES (?,?,?);";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(p)) {
            ekp.setString(1, nev);
            ekp.setString(2, osszeg);
            ekp.setString(3, statusz);
            return ekp.executeUpdate();
        } catch (SQLException ex) {
            Panel.hiba("Hiba", ex.getMessage());

            return 0;
        }
    }

    public void beolvasnaplo(ObservableList<Naplo> tabla) {
        String s = "SELECT* FROM befejezett ORDER BY nev";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(s)) {
            tabla.clear();
            ResultSet eredmeny = ekp.executeQuery();
            while (eredmeny.next()) {
                tabla.add(new Naplo(
                        eredmeny.getString("nev"),
                        // eredmeny.getString("datum"),
                        eredmeny.getInt("osszeg"),
                        eredmeny.getString("statusz")
                ));
            }
        } catch (SQLException ex) {
            Panel.hiba("Hiba!", ex.getMessage());
        }
    }

    public void statisztika_elvegzett(ObservableList<Naplo> tabla) {
        String s = "SELECT* FROM befejezett WHERE statusz='Elvégezve';";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(s)) {
            tabla.clear();
            ResultSet eredmeny = ekp.executeQuery();
            while (eredmeny.next()) {
                tabla.add(new Naplo(
                        eredmeny.getString("nev"),
                        eredmeny.getInt("osszeg"),
                        eredmeny.getString("statusz")
                ));
            }
        } catch (SQLException ex) {
            Panel.hiba("Hiba!", ex.getMessage());
        }
    }

    public void statisztika_lemondva(ObservableList<Naplo> tabla) {
        String s = "SELECT* FROM befejezett WHERE statusz='Lemondva';";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(s)) {
            tabla.clear();
            ResultSet eredmeny = ekp.executeQuery();
            while (eredmeny.next()) {
                tabla.add(new Naplo(
                        eredmeny.getString("nev"),
                        eredmeny.getInt("osszeg"),
                        eredmeny.getString("statusz")
                ));
            }
        } catch (SQLException ex) {
            Panel.hiba("Hiba!", ex.getMessage());
        }
    }

    public int naplo_torol(String d) {
        String s = "DELETE FROM befejezett WHERE nev=?;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setString(1, d);
            return ekp.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public int modosit(int id, String nev, int tav, int mennyiseg, LocalDate datum, String elerhetoseg, String ar) {
        String s = "UPDATE alap SET nev=?, tav=?, mennyiseg=?, datum=?, elerhetoseg=?,ar=? WHERE id=?;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
                PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setString(1, nev);
            ekp.setInt(2, tav);
            ekp.setInt(3, mennyiseg);
            ekp.setString(4, datum.toString());
            ekp.setString(5, elerhetoseg);
            ekp.setString(6, ar);
            ekp.setInt(7, id);
            return ekp.executeUpdate();
        } catch (SQLException ex) {
            Panel.hiba("Hiba", ex.getMessage());
            return 0;
        }
    }

}
