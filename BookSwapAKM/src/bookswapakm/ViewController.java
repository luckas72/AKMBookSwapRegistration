/*
Name        : BookSwap event software for Alpha Kappa Mu
Description : This class will handle the viewing the information of a person
Author      : Luckas(Yinghong) Huang
Date        : Dec/23th/2018
 */
package bookswapakm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class ViewController implements Initializable {

    //  Labels for personal information
    @FXML
    Label lbFN;
    @FXML
    Label lbLN;
    @FXML
    Label lbMI;
    @FXML
    Label lbID;

    //  Buttons to modify stuff
    @FXML
    Button btModFN;
    @FXML
    Button btModMI;
    @FXML
    Button btModLN;
    @FXML
    Button btModID;
    @FXML
    Button btChange;
    @FXML
    Button btDelete;

    //  Table front end 
    @FXML
    TableView tvBookData;
    @FXML
    TableColumn tcStatus;
    @FXML
    TableColumn tcProvided;
    @FXML
    TableColumn tcReceived;

    private DBConn dbc;
    private ArrayList<Person> personAL;
    private ObservableList<Book> data;
    static String field;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //  Declaring the sql database class
        try {
            dbc = new DBConn();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("MySql exception in viewController class. Connection error!");
            alert.showAndWait();
            return;// Stop processing the rest of it 
        }

        try {
            //  Look up the person by the studentId
            personAL = dbc.getPerson(FXMLDocumentController.getId());
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("MySql exception in viewController class. LookUp error!");
            alert.showAndWait();
            return;// Stop processing the rest of it 
        }

        if (personAL.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Selected person does not exist in the database anymore!");
            alert.showAndWait();
            return;  // Stop processing the rest of it 
        }

        lbFN.setText(personAL.get(0).getFirstName());
        lbMI.setText(personAL.get(0).getMiddleName());
        lbLN.setText(personAL.get(0).getLastName());
        lbID.setText(personAL.get(0).getStudentId());

        loadTable();
    }

    @FXML
    void handleDelete() throws SQLException {

        // Check if the admin has selected a set of data or not
        if (tvBookData.getSelectionModel().getSelectedItem() == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText("You did not select the data to delete!");
            a.showAndWait();
            return; // Stop processing the rest of the code if error encountered
        }

        Book book = (Book) tvBookData.getSelectionModel().getSelectedItem();

        //  delete the data
        ButtonType bttDelete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType bttCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Create an alert object to show a confirmation of quiting the program
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete the set of data?", bttDelete, bttCancel);
        quitAlert.setTitle("Deletion confirmation");
        quitAlert.setHeaderText(null);
        quitAlert.setResizable(false);

        Optional<ButtonType> exitOptional = quitAlert.showAndWait();

        //  If the users click on exit, then exit the platform, otherwise remain, which can be accomplished with one if statement
        if (exitOptional.get() == bttDelete) {
            dbc.deleteBookInfo(FXMLDocumentController.getId(), book.getStatus(), book.getProvidedBook());
            // Create an alert object to notify the book has been deleted
            Alert congratAlert = new Alert(Alert.AlertType.INFORMATION);
            congratAlert.setTitle("Deletion completed");
            congratAlert.setHeaderText(null);
            congratAlert.setContentText("You have deleted the set of record.");
            ImageView imageView = new ImageView(("/images/checkM.png").toString());
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            congratAlert.getDialogPane().setGraphic(imageView);
            congratAlert.showAndWait();
            loadTable();
        }
        // Do nothing if they click on the other button which is cancel
    }

    @FXML
    void modifyFN() {
        setField("FirstN");
        openModify("First Name");
    }

    @FXML
    void modifyMN() {
        setField("MiddleI");
        openModify("Middle Name");
    }

    @FXML
    void modifyLN() {
        setField("LastN");
        openModify("Last Name");
    }

    @FXML
    void modifyID() {
        setField("studentId");
        openModify("Student Id");
    }

    private void openModify(String field) {
        //  Load modify window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ModifyPerson.fxml"));
            Parent root1;
            root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Modify " + field);
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("FXML loader error in ViewCOntroller class, handle openModify! ");
            alert.showAndWait();
        }
    }

    private void loadTable() {
        try {
            data = FXCollections.observableArrayList(dbc.loadParticipantsBook(lbID.getText()));
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("MySql exception in viewController class. Connection error!");
            alert.showAndWait();
            return;// Stop processing the rest of it 
        }
        tvBookData.setItems(null);
        tcProvided.setCellValueFactory(new PropertyValueFactory("providedBook"));
        tcReceived.setCellValueFactory(new PropertyValueFactory("receivedBook"));
        tcStatus.setCellValueFactory(new PropertyValueFactory("status"));
        tvBookData.setItems(data);
        tvBookData.setEditable(false);
    }

    static public void setField(String s) {
        field = s;
    }

    static public String getField() {
        return field;
    }

}
