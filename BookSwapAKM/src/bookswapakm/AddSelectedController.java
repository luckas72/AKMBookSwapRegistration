/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookswapakm;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class AddSelectedController implements Initializable {

    @FXML
    private Label lbFN;
    @FXML
    private Label lbMI;
    @FXML
    private Label lbLN;
    @FXML
    private Label lbStuID;
    @FXML
    private RadioButton rdSwap;
    @FXML
    private RadioButton rdDonate;
    @FXML
    private TextField tfPBook;
    @FXML
    private TextField tfRBook;
    @FXML
    private Button btSubmit;

    private DBConn dbc;
    private ToggleGroup tgRd=new ToggleGroup();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbFN.setText("First Name: " + FXMLDocumentController.getPerson().getFirstName());
        lbMI.setText("Middle Name: " + FXMLDocumentController.getPerson().getMiddleName());
        lbLN.setText("Last Name: " + FXMLDocumentController.getPerson().getLastName());
        lbStuID.setText("Student Id: " + FXMLDocumentController.getPerson().getStudentId());
        rdDonate.setToggleGroup(tgRd);
        rdSwap.setToggleGroup(tgRd);

        try {
            dbc = new DBConn();
        } catch (SQLException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText("MySql Connection Error occurred on AddSelectedController class.");
        }

    }

    @FXML
    void handleSubmit() throws SQLException {
        String providedBook = tfPBook.getText();
        String receivedBook = tfRBook.getText();
        String status="";
        boolean swapFlag = false;
        boolean emptyField = false;
        String emptyContent = "";

        Alert al = new Alert(Alert.AlertType.ERROR);
        al.setHeaderText(null);
        al.setTitle("Error");

        if (tgRd.getSelectedToggle() == null) {
            emptyField = true;
            emptyContent+="\n-Method";
        } else if (tgRd.getSelectedToggle().equals(rdSwap)) {
            swapFlag = true;
            status="Swap";
        }else{
            status="Donate";
        }

        if (providedBook.length() < 1) {
            emptyContent+="\n-Provided Book";
            emptyField = true;
        }
        if (swapFlag) {
            if (receivedBook.length() < 1) {
                emptyContent+="\n-Received Book";
                emptyField = true;
            }
        }
        
        if(emptyField){
            al.setContentText("You have the following empty fields: " + emptyContent + "\n\tPlease fill them out!");
            al.showAndWait();
            return; // Stop processing the rest of the code
        }
        
        dbc.storePerformance(FXMLDocumentController.getPerson().getStudentId(), status, providedBook, receivedBook);
        
         //  Congrats alert
        Alert congratAlert = new Alert(Alert.AlertType.INFORMATION);
        congratAlert.setTitle("Adding completed");
        congratAlert.setHeaderText(null);
        congratAlert.setContentText("You have successfully added a record!");
        ImageView imageView = new ImageView(("/images/checkM.png").toString());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        congratAlert.getDialogPane().setGraphic(imageView);
        congratAlert.showAndWait();
        
        Stage stage = (Stage) btSubmit.getScene().getWindow();
        stage.close();
    }

}
