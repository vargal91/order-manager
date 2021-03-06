/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package butorszallitas;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author varglaszlo
 */
public class FXMLDocumentController implements Initializable {

    DB ab = new DB();

    @FXML
    private ComboBox<Integer> chbxTav;

    @FXML
    private ComboBox<Integer> chbxMennyiseg;

    @FXML
    private Button btnUj;

    @FXML
    private Button btnElvegezve;

    @FXML
    private Button btnLemondva;

    @FXML
    private Button btnNaplo;

    @FXML
    private Button btnModosit;

    @FXML
    private TableView<Szallitas> tblSzallitas;

    @FXML
    private TableColumn<Szallitas, String> oNev;

    @FXML
    private TableColumn<Szallitas, Integer> oTav;

    @FXML
    private TableColumn<Szallitas, Integer> oMennyiseg;

    @FXML
    private TableColumn<Szallitas, String> oDatum;

    @FXML
    private TableColumn<Szallitas, String> oElerhetoseg;

    @FXML
    private TableColumn<Szallitas, Integer> oAr;

    @FXML
    private TextField lblNev;

    @FXML
    private TextField txtElerhetoseg;

    @FXML
    private TextField txtAr;

    @FXML
    private DatePicker dpDatum;

    @FXML
    void arak(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ArAblak.fxml"));

        Scene scene = new Scene(root);
        Stage arablak = new Stage();
        arablak.setTitle("??rak");
        arablak.setResizable(false);
        arablak.getIcons().add(new Image("info.png"));
        arablak.initModality(Modality.APPLICATION_MODAL);
        arablak.setScene(scene);
        arablak.showAndWait();

    }

    @FXML
    void elvegezve() {
        if(txtElerhetoseg.getText().isEmpty()){
            Panel.hiba("Hiba", "Nem jel??lt??l ki megrendel??st!");
            return;
        }
        String nev = lblNev.getText().trim();
        String elerhetoseg = txtElerhetoseg.getText().trim();
        String osszeg = txtAr.getText().trim();

        String statusz = "Elv??gezve";
        ab.beirnaplo(nev, osszeg, statusz);
        int index = tblSzallitas.getSelectionModel().getSelectedIndex();
        int id = tblSzallitas.getItems().get(index).getId();
        int sor = ab.torol(id);
        if (sor > 0) {
            beolvas();
        }
    }

    @FXML
    void lemondas() {
         if(txtElerhetoseg.getText().isEmpty()){
            Panel.hiba("Hiba", "Nem jel??lt??l ki megrendel??st!");
            return;
        }
        //ellenorzes();
        String nev = lblNev.getText().trim();
        String elerhetoseg = txtElerhetoseg.getText().trim();
        String osszeg = txtAr.getText().trim();
        String statusz = "Lemondva";
        ab.beirnaplo(nev, osszeg, statusz);
        int index = tblSzallitas.getSelectionModel().getSelectedIndex();
        int id = tblSzallitas.getItems().get(index).getId();
        int sor = ab.torol(id);
        if (sor > 0) {
            beolvas();
        }
    }

    
    @FXML
    void adat_torles() {
        lblNev.clear();
        txtElerhetoseg.clear();
        dpDatum.setValue(LocalDate.now());
        txtAr.clear();
        chbxMennyiseg.setValue(0);
        chbxTav.setValue(0);
        lblNev.requestFocus();
    }

    @FXML
    void rogzit() {
        String nev = lblNev.getText().trim();
        if (nev.length() < 1 || nev.length() > 50) {
            Panel.hiba("Hiba", "N??v nem lett megadva, k??rlek add meg a "
                    + "megrendel?? nev??t!");
            lblNev.requestFocus();
            return;
        }
        
        Integer tav = chbxTav.getValue();
        if (tav <= 0) {
            Panel.hiba("Hiba", "T??vols??g nem lett megadva");
            chbxTav.requestFocus();
            return;
        }
        Integer mennyiseg = chbxMennyiseg.getValue();
        if (mennyiseg <= 0) {
            Panel.hiba("Hiba", "Mennyis??g nem lett megadva");
            chbxMennyiseg.requestFocus();
            return;
        }

        String elerhetoseg = txtElerhetoseg.getText().trim();
        if (elerhetoseg.length() < 1 || elerhetoseg.length() > 50) {
            Panel.hiba("Hiba", "Nem lett el??rhet??s??g megadva"
                    + ", k??rlek adj meg egy e-mail c??met!");
            lblNev.requestFocus();
            return;
        }

        LocalDate datum = dpDatum.getValue();
        if (datum.isBefore(LocalDate.now())) {
            Panel.hiba("Hiba", "Nem lehet kor??bbi d??tumra megrendel??st leadni");
            dpDatum.requestFocus();
            return;
        }
        if (datum.equals(LocalDate.now())) {
            Panel.hiba("Hiba", "Mai napra m??r nem lehet megrendel??st leadni");
            dpDatum.requestFocus();
            return;
        }

        String ar = txtAr.getText().trim();
        if (ar.equals("")) {
            Panel.hiba("Hiba", "K??rlek kalkul??lj egy ??rat");
            txtAr.requestFocus();
            return;
        }

        if (ab.beir(nev, tav, mennyiseg, datum, elerhetoseg, ar) > 0) {
            adat_torles();
            beolvas();
        }

    }

    @FXML
    void szamol() {
        int munkadij = 10000;
        int mennyiseg = chbxMennyiseg.getValue() * 1000;
        int km = 500;
        int tav = chbxTav.getValue() * 500;
        int osszes = munkadij + mennyiseg + tav;
        txtAr.setText("" + osszes);

    }

    @FXML
    void ujablak(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ujAblak.fxml"));

        Scene scene = new Scene(root);
        Stage ujablak = new Stage();
        ujablak.setTitle("Naplo");
        ujablak.setResizable(false);
        ujablak.getIcons().add(new Image("konyv.png"));
        ujablak.initModality(Modality.APPLICATION_MODAL);
        ujablak.setScene(scene);
        ujablak.showAndWait();

    }

    @FXML
    void bill(KeyEvent event) {

        if (event.getCode() == KeyCode.INSERT) {
            adat_torles();
        }

    }

    @FXML
    void iarak(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ArAblak.fxml"));

        Scene scene = new Scene(root);
        Stage arablak = new Stage();
        arablak.setTitle("??rak");
        arablak.setResizable(false);

        arablak.initModality(Modality.APPLICATION_MODAL);
        arablak.setScene(scene);
        arablak.showAndWait();
    }

    @FXML
    void ikilepes(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void iexport(ActionEvent event) {
        export();
    }

    @FXML
    void modosit() {
        
        int index = tblSzallitas.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Panel.hiba("Hiba", "Nincs kijel??lve megrendel??s");
            return;
        }
        int id = tblSzallitas.getItems().get(index).getId();
        String nev = lblNev.getText().trim();
        if (nev.length() < 1 || nev.length() > 50) {
            Panel.hiba("Hiba", "N??v nem lett megadva, k??rlek add meg a "
                    + "megrendel?? nev??t!");
            lblNev.requestFocus();
            return;
        }

        Integer tav = chbxTav.getValue();
        if (tav <= 0) {
            Panel.hiba("Hiba", "T??vols??g nem lett megadva");
            chbxTav.requestFocus();
            return;
        }
        Integer mennyiseg = chbxMennyiseg.getValue();
        if (mennyiseg <= 0) {
            Panel.hiba("Hiba", "Mennyis??g nem lett megadva");
            chbxMennyiseg.requestFocus();
            return;
        }

        String elerhetoseg = txtElerhetoseg.getText().trim();
        if (nev.length() < 1 || nev.length() > 50) {
            Panel.hiba("Hiba", "Nem lett el??rhet??s??g megadva"
                    + ", k??rlek adj meg egy e-mail c??met!");
            lblNev.requestFocus();
            return;
        }

        LocalDate datum = dpDatum.getValue();
        if (datum.isBefore(LocalDate.now())) {
            Panel.hiba("Hiba", "Nem lehet kor??bbi d??tumra megrendel??st leadni");
            dpDatum.requestFocus();
            return;
        }
        if (datum.equals(LocalDate.now())) {
            Panel.hiba("Hiba", "Mai napra m??r nem lehet megrendel??st leadni");
            dpDatum.requestFocus();
            return;
        }

        String ar = txtAr.getText().trim();
        if (ar.equals("")) {
            Panel.hiba("Hiba", "K??rlek kalkul??lj egy ??rat");
            txtAr.requestFocus();
            return;
        }

        int sor = ab.modosit(id, nev, tav, mennyiseg, datum, elerhetoseg, ar);
        if (sor > 0) {
            if (Panel.kalkulacio("Figyelmeztet??s", "Ha m??dos??tott??l a t??von vagy a "
                    + "mennyis??gen ??j ??rat is kell kalkul??lnod!"
                    + "\nKalkul??lt??l? ")) {
                beolvas();
                for (int i = 0; i < tblSzallitas.getItems().size(); i++) {
                    if (tblSzallitas.getItems().get(i).getId() == id) {
                        tblSzallitas.getSelectionModel().select(i);
                        break;
                    }
                }
            }
        }
    }

    @FXML
    void export() {
        FileChooser fv = new FileChooser();
        fv.setTitle("Exportl??s");
        FileChooser.ExtensionFilter szuro = new FileChooser.ExtensionFilter("CSV f??jl", "*.csv");
        fv.getExtensionFilters().add(szuro);
        fv.setInitialDirectory(new File("."));
        File f = fv.showSaveDialog(null);
        if (f != null) {
            try (PrintWriter ki = new PrintWriter(f.getAbsoluteFile(), "cp1250")) {
                ki.println("N??v;T??v;Mennyis??g;D??tum;El??rhet??s??g;??r");
                for (Szallitas sz : tblSzallitas.getItems()) {
                    ki.println(sz);
                }
            } catch (IOException ex) {
                Panel.hiba("Hiba", "Nem tudtam export??lni!");
            }
        }
    }

    public void beolvas() {
        ab.beolvas(tblSzallitas.getItems());
    }

    public void beszurmennyiseg() {
        chbxMennyiseg.getItems().add(0);
        chbxMennyiseg.getItems().add(1);
        chbxMennyiseg.getItems().add(2);
        chbxMennyiseg.getItems().add(3);
        chbxMennyiseg.getItems().add(4);
        chbxMennyiseg.getItems().add(5);
        chbxMennyiseg.getItems().add(6);
        chbxMennyiseg.getItems().add(7);
        chbxMennyiseg.getItems().add(8);
        chbxMennyiseg.getItems().add(9);
        chbxMennyiseg.getItems().add(10);
        chbxMennyiseg.getSelectionModel().select(0);
    }

    public void beszurtav() {
        chbxTav.getItems().add(0);
        chbxTav.getItems().add(5);
        chbxTav.getItems().add(10);
        chbxTav.getItems().add(15);
        chbxTav.getItems().add(20);
        chbxTav.getItems().add(25);
        chbxTav.getItems().add(30);
        chbxTav.getSelectionModel().select(0);
    }

    private void tablabol(int i) {
        if (i == -1) {
            return;
        }
        Szallitas sz = tblSzallitas.getItems().get(i);
        lblNev.setText("" + sz.getNev());
        txtAr.setText("" + sz.getAr());
        txtElerhetoseg.setText(sz.getElerhetoseg());
        chbxTav.setValue(sz.getTav());
        chbxMennyiseg.setValue(sz.getMennyiseg());
        dpDatum.setValue(LocalDate.parse(sz.getDatum()));

    }
    
    private void ellenorzes(){
        if(txtElerhetoseg.getText().isEmpty()){
            Panel.hiba("Hiba", "Nem jel??lt??l ki megrendel??st");
            
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        beszurtav();
        beszurmennyiseg();

        oNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        oTav.setCellValueFactory(new PropertyValueFactory<>("tav"));
        oMennyiseg.setCellValueFactory(new PropertyValueFactory<>("mennyiseg"));
        oDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        oElerhetoseg.setCellValueFactory(new PropertyValueFactory<>("elerhetoseg"));
        oAr.setCellValueFactory(new PropertyValueFactory<>("ar"));
        beolvas();
        dpDatum.setValue(LocalDate.now());
        tblSzallitas.getSelectionModel().selectedIndexProperty().addListener(
                (o, regi, uj) -> tablabol(uj.intValue()));
    }

}
