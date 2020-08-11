/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookswapakm;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class ModifyTheAdminController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfLevel;
    @FXML
    private TableView tvAdminData;
    @FXML
    private TableColumn tcAdminName;
    @FXML
    private TableColumn tcAdminPass;
    @FXML
    private TableColumn tcAuthorityLevel;
    @FXML
    private TableColumn tcLastAccess;
    @FXML
    private Button btView;
    @FXML
    private Button btDelete;

    DBConn dbc;
    private ObservableList<Administrator> data;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       tfLevel.setText(FXMLDocumentController.getAdm().getAdmLevel());
       tfName.setText(FXMLDocumentController.getAdm().getAdmName());
       
        try {
            dbc=new DBConn();
        } catch (SQLException ex) {
              Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText("MySql Connection Error occurred on ModifyTheAdminController class.");
        }
       
        loadAdminTable();
    }

    public void loadAdminTable()
    {
        try {
            data=FXCollections.observableArrayList(dbc.getAdminArrayList(FXMLDocumentController.getAdm().getAdmLevel()));
            
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("MySql exception in ModifyTheAdminController class. Connection error!");
            alert.showAndWait();
            return;// Stop processing the rest of it 
        }
        tvAdminData.setItems(null);
        tcAdminName.setCellValueFactory(new PropertyValueFactory("admName"));
        tcAdminPass.setCellValueFactory(new PropertyValueFactory("admPass"));
        tcAuthorityLevel.setCellValueFactory(new PropertyValueFactory("admLevel"));
        tcLastAccess.setCellValueFactory(new PropertyValueFactory("admLastAccess"));
        tvAdminData.setItems(data);
        tvAdminData.setEditable(false);
    } 
    
    @FXML 
    public void handleDelete() throws SQLException{
       
        // Check if the admin has selected a set of data or not
        if (tvAdminData.getSelectionModel().getSelectedItem() == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText("You did not select the data to delete!");
            a.showAndWait();
            return; // Stop processing the rest of the code if error encountered
        }

        Administrator administrator = (Administrator) tvAdminData.getSelectionModel().getSelectedItem();

        //  delete the data
        ButtonType bttDelete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType bttCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Create an alert object to show a confirmation of quiting the program
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete the "+ administrator.getAdmName()+"?", bttDelete, bttCancel);
        quitAlert.setTitle("Delete confirmation");
        quitAlert.setHeaderText(null);
        quitAlert.setResizable(false);

        Optional<ButtonType> exitOptional = quitAlert.showAndWait();

        //  If the users click on exit, then exit the platform, otherwise remain, which can be accomplished with one if statement
        if (exitOptional.get() == bttDelete) {
            dbc.deleteAdmim(administrator.getAdmName());
            // Create an alert object to notify the book has been deleted
            Alert congratAlert = new Alert(Alert.AlertType.INFORMATION);
            congratAlert.setTitle("Deletion completed");
            congratAlert.setHeaderText(null);
            congratAlert.setContentText("You have deleted " + administrator.getAdmName()+ "!");
            ImageView imageView = new ImageView(("/images/checkM.png").toString());
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            congratAlert.getDialogPane().setGraphic(imageView);
            congratAlert.showAndWait();
            loadAdminTable();
        }
        // Do nothing if they click on the other button which is cancel 
    }
    
    @FXML
    public void modifyAuthority() throws SQLException{
         // Check if the admin has selected a set of data or not
        if (tvAdminData.getSelectionModel().getSelectedItem() == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText("You did not select the administrator to modify the authority!");
            a.showAndWait();
            return; // Stop processing the rest of the code if error encountered
        }

       Administrator administrator = (Administrator) tvAdminData.getSelectionModel().getSelectedItem();
        //  Modify the data
        ButtonType bttAuthorize = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        ButtonType bttCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Create an alert object to show a confirmation of quiting the program
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to modify "+ administrator.getAdmName()+"?", bttAuthorize, bttCancel);
        quitAlert.setTitle("Modification confirmation");
        quitAlert.setHeaderText(null);
        quitAlert.setResizable(false);

        Optional<ButtonType> exitOptional = quitAlert.showAndWait();

        //  If the users click on exit, then exit the platform, otherwise remain, which can be accomplished with one if statement
        if (exitOptional.get() == bttAuthorize) {
            dbc.modifyAdmin(administrator.getAdmName());
            // Create an alert object to notify the book has been deleted
            Alert congratAlert = new Alert(Alert.AlertType.INFORMATION);
            congratAlert.setTitle("Modification completed");
            congratAlert.setHeaderText(null);
            congratAlert.setContentText("You have authorized " + administrator.getAdmName()+ "!");
            ImageView imageView = new ImageView(("/images/checkM.png").toString());
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            congratAlert.getDialogPane().setGraphic(imageView);
            congratAlert.showAndWait();
            loadAdminTable();
        }
        // Do nothing if they click on the other button which is cancel 
    }
}
