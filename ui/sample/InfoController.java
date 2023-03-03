/*
 * Basim Bennaji
 * Thema: Autoren des Programms (Credits).
 * Erstellungsdatum: 26.02.2023
 * Letzte Aenderung: 26.02.2023 01:20
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * Namen der Autoren wurden hinzugefuegt. Markierung mit einem '*' fuer Schueler der Fichtenberg-Oberschule wurden hinzugefuegt. 26.02.2023
 */

 import java.io.IOException;

 import javafx.event.ActionEvent;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.stage.Stage;
 
 public class InfoController {
     
     private Stage stage;
     private Scene scene;
 
     @FXML
     private Button homebtn;
 
     @FXML
     private Label people1;
 
     @FXML
     private Label people2;
     
     public void switchtoHome (ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
         stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         
     }
     
     @FXML
     void initialize() {
         people2.setText("Basim Bennaji \n Till Dietrich \n Emil Habermann \n Alexander Horns* \n Moritz Oehme* \n Felix Trautwein*");
         people1.setText("Niklas Bamberg \n Alexander Betke \n Maximilian Gombala \n Samuel Hoffleit* \n Jonas Lossin \n Rosan Sharma*");
 
     }
 
 }