/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package butorszallitas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

public class UjAblakController implements Initializable {

    DB ab = new DB();

    @FXML
    private TableView<Naplo> tblNaplo;

    @FXML
    private TableColumn<Naplo, String> oNev;

    @FXML
    private TableColumn<Naplo, Integer> oOsszeg;

    @FXML
    private TableColumn<Naplo, String> oStatusz;

    @FXML
    private Button btnBezar;

    @FXML
    private Label lblStatisztika;

    @FXML
    void bezar() {
        Window ablak = btnBezar.getScene().getWindow();
        ablak.hide();
    }

    @FXML
    private Button btnTorles;

    @FXML
    private Button btnStatisztika;

    @FXML
    void statisztika() {

        ab.statisztika_elvegzett(tblNaplo.getItems());
        int elvosszeg = 0;
        int elvegzett = 0;
        int db = 0;
        for (Naplo sz : tblNaplo.getItems()) {
            elvosszeg += sz.getOsszeg();
            elvegzett++;
            db++;
        }

        ab.statisztika_lemondva(tblNaplo.getItems());
        int lemondott = 0;
        int losszeg = 0;
        for (Naplo sz : tblNaplo.getItems()) {
            losszeg += sz.getOsszeg();
            lemondott++;
            db++;
        }
        lblStatisztika.setText("Elvgézett fuvarok száma: " + elvegzett
                + "\nBefolyt összeg: " + elvosszeg + " Ft"
                + "\nLemondott fuvarok száma:  " + lemondott
                + "\nKiesett összeg: " + losszeg + " Ft");

        naplobeolvas();
    }

    @FXML
    void vegleges_torles() {
        int index = tblNaplo.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            return;
        }
        if (!Panel.igennem("Törlés", "Biztosan törölni szeretné ezt a megrendelést? ")) {
            return;
        }
        String s = tblNaplo.getItems().get(index).getNev();
        int sor = ab.naplo_torol(s);
        if (sor > 0) {
            naplobeolvas();
        }
        lblStatisztika.setText("");
    }

    @FXML
    void bill(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            vegleges_torles();
        }
    }

    public void naplobeolvas() {
        ab.beolvasnaplo(tblNaplo.getItems());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        oOsszeg.setCellValueFactory(new PropertyValueFactory<>("osszeg"));
        oStatusz.setCellValueFactory(new PropertyValueFactory<>("statusz"));
        naplobeolvas();
    }

}
