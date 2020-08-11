/*
Name        : BookSwap event software for Alpha Kappa Mu
Description : This class is used to modify the participants informations
Author      : Luckas(Yinghong) Huang
Date        : Jan/1st/2018
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ModifyPersonController implements Initializable {

    @FXML
    Label lbField;
    @FXML
    Button btChange;
    @FXML
    TextField tfField;
    private DBConn dbc;
    String fieldName = ViewController.getField();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            dbc = new DBConn();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("MySql exception in ModifyPersonController class. Connection error!");
            alert.showAndWait();
            return;// Stop processing the rest of it 
        }

        if (fieldName.equals("FirstN")) {
            fieldName = "First Name:";
            tfField.setPromptText("50 characters limit");
        } else if (fieldName.equals("MiddleI")) {
            fieldName = "Middle Name:";
            tfField.setPromptText("10 characters limit");
        } else if (fieldName.equals("LastN")) {
            fieldName = "Last Name:";
            tfField.setPromptText("50 characters limit");
        } else {
            fieldName = "Student Id:";
            tfField.setPromptText("10 characters limit");
        }

        lbField.setText(fieldName);
    }

    @FXML
    private void handleChange() throws SQLException {

        String content = tfField.getText();
        boolean errorExceeding = false;
        boolean errorEmpty = false;

        if (ViewController.getField().equals("FirstN") || ViewController.getField().equals("LastN")) {
            if (content.length() > 50) {
                errorExceeding = true;
            } else if (content.length() < 1) {
                errorEmpty = true;
            }
        } else {
            if (content.length() > 10) {
                errorExceeding = true;
            } else if (content.length() < 1) {
                errorEmpty = true;
            }
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        if (errorEmpty) {
            alert.setContentText("Please enter the " + fieldName.replace(":", "") + " of the person.");
            alert.showAndWait();
            return; // Stop processing
        }
        if (errorExceeding) {
            alert.setContentText("Please do not exceed the characters of the " + fieldName.replace(":", "") + ".");
            alert.showAndWait();
            return; // Stop processing
        }

        if (doesItContainSpecialChar(content)) {
            alert.setContentText("Please do not enter any special characters for " + fieldName.replace(":", "") + ".");
            alert.showAndWait();
            return; //  Strop processing
        }

        if (fieldName.equals("Student Id:")) {
            if (!content.matches("[0-9]*")) {
                alert.setContentText("Student Id has to be numeric.");
                alert.showAndWait();
                return; // Stop processing
            }
        }

        dbc.updatePersonInfo(ViewController.getField(), content, FXMLDocumentController.getId());
        //  Congrats alert
        Alert congratAlert = new Alert(Alert.AlertType.INFORMATION);
        congratAlert.setTitle("Adding completed");
        congratAlert.setHeaderText(null);
        congratAlert.setContentText(fieldName.replace(":", "") + " have been successfully changed!");
        ImageView imageView = new ImageView(("/images/checkM.png").toString());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        congratAlert.getDialogPane().setGraphic(imageView);
        congratAlert.showAndWait();
        
        //  Close the window for the admin
        Stage stage = (Stage)btChange.getScene().getWindow();
        stage.close();
    }

    //  Method Description: This method will check whether the given field is empty or not
    private boolean isEmptyField(String s) { 
        return s.length() < 1;
    }

    //  Method Description: This method will be used to check whether the input has invalid content or not
    private boolean doesItContainSpecialChar(String s) {
        return !(s.matches("[a-zA-Z0-9 ]+"));
    }

}
