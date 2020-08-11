/*
Name        : BookSwap event software for Alpha Kappa Mu
Description : This class will handle the window that pops up once the admin click on add button to add a new swap for a person
Author      : Luckas(Yinghong) Huang
Date        : Dec/23th/2018
 */
package bookswapakm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class AddController implements Initializable {

    @FXML
    private AnchorPane apNode;
    @FXML
    private RadioButton rdSwap;
    @FXML
    private RadioButton rdDonate;
    @FXML
    private TextField tfFN;
    @FXML
    private TextField tfMI;
    @FXML
    private TextField tfLN;
    @FXML
    private TextField tfSID;
    @FXML
    private TextField tfPBook;
    @FXML
    private TextField tfRBook;
    @FXML
    private Button btSubmit;

    private ToggleGroup tgRd = new ToggleGroup();
    private DBConn dbc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            dbc = new DBConn();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("MySql error in AddController class! Database connection error ");
            alert.showAndWait();
        }

        rdDonate.setToggleGroup(tgRd);
        rdSwap.setToggleGroup(tgRd);
    }

    @FXML
    void handleSubmit() throws SQLException, IOException {
        String fN = tfFN.getText();
        String mI = tfMI.getText();
        String lN = tfLN.getText();
        String stuId = tfSID.getText();
        String pBook = tfPBook.getText();
        String rBook = tfRBook.getText();
        String status = "";
        String exceedingFields = "You have exceeded the following field limit:";
        String emptyFields = "You have the following empty:";
        String specialFields = "You have entered special characters for the following fields:";
        boolean emptyFlag = false;
        boolean exceedingFlag = false;
        boolean swapFlag = false;
        boolean specialFlag = false;

        if (tgRd.getSelectedToggle() == null) {
            emptyFields += "\n-Method";
            emptyFlag = true;
        } else {
            if (tgRd.getSelectedToggle().equals(rdDonate)) {
                status = "Donate";
            } else {
                status = "Swap";
                swapFlag = true;
            }
        }

        if (isEmptyField(fN)) {
            emptyFields += "\n-First Name";
            emptyFlag = true;
        }
        /*if (isEmptyField(mI)) {
            emptyFields += "\n-Middle Initial";
            emptyFlag = true;
        }*/
        if (isEmptyField(lN)) {
            emptyFields += "\n-Last Name";
            emptyFlag = true;
        }
        if (isEmptyField(stuId)) {
            emptyFields += "\n-Student Id";
            emptyFlag = true;
        }
        if (isEmptyField(pBook)) {
            emptyFields += "\n-Provided Book";
            emptyFlag = true;
        }
        if (swapFlag) {
            if (isEmptyField(rBook)) {
                emptyFields += "\n-Received Book";
                emptyFlag = true;
            }
        }

        //  If the empty flag is on then send a message to the user
        if (emptyFlag) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(emptyFields + "\n\tPlease fill them out!");
            alert.showAndWait();
            return;
        }

        if (fN.length() > 50) {
            exceedingFields += "\n-First Name, 50 characters limit";
            exceedingFlag = true;
        }
        if (mI.length() > 10) {
            exceedingFields += "\n-Middle Name, 10 characters limit";
            exceedingFlag = true;
        }
        if (lN.length() > 50) {
            exceedingFields += "\n-Last Name, 50 characters limit";
            exceedingFlag = true;
        }
        if (stuId.length() > 10) {
            exceedingFields += "\n-Student Id";
            exceedingFlag = true;
        }
        if (exceedingFlag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(exceedingFields + "\n\tPlease adjust!");
            alert.showAndWait();
            return;
        }

        if (doesItContainSpecialChar(fN)) {
            specialFlag = true;
            specialFields += "\n-First Name";
        }
        if(mI.length()>0){
        if (doesItContainSpecialChar(mI)) {
            specialFlag = true;
            specialFields += "\n-Middle Name";
        }}
        if (doesItContainSpecialChar(lN)) {
            specialFlag = true;
            specialFields += "\n-Last Name";
        }
        if (specialFlag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(specialFields + " \n\tPlease adjust");
            alert.showAndWait();
            return;
        }
        if (!stuId.matches("[0-9]*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Student Id has to be numeric.");
            alert.showAndWait();
            return;
        }
        // Once the code surpass the checking then store the data
        dbc.storePerson(fN, mI, lN, stuId);
        dbc.storePerformance(stuId, status, pBook, rBook);
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

        //  Close the window for the admin
        Stage stage = (Stage) btSubmit.getScene().getWindow();
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
