/*
Name        : BookSwap event software for Alpha Kappa Mu
Description : This class is used to set all the event handler methods
Author      : Luckas(Yinghong) Huang
Date        : Jan/1st/2018
 */
package bookswapakm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author HP
 */
public class FXMLDocumentController implements Initializable {

    //  Ultimate pane
    @FXML
    BorderPane bpUltimate;
    //  Menu
    @FXML
    MenuBar mnbBookSwap;
    @FXML
    Menu mnAccess;
    @FXML
    MenuItem mniLogin;
    @FXML
    MenuItem mniChangePass;
    @FXML
    MenuItem mniCreateAdmin;
    @FXML
    MenuItem mniQuit;
    @FXML
    Menu mnHelp;
    @FXML
    MenuItem mniTut;
    @FXML
    MenuItem mniAbout;

    //  Initial login page
    @FXML
    Label lbUser;
    @FXML
    Label lbPassword;
    @FXML
    TextField tfUser;
    @FXML
    PasswordField pfPassword;
    @FXML
    Button btLogin;
    @FXML
    AnchorPane apLogingIn;

    //  New Administrator registering
    @FXML
    AnchorPane apReg;
    @FXML
    TextField tfUSupAdm;
    @FXML
    TextField tfNAdm;
    @FXML
    PasswordField pfPSupAdm;
    @FXML
    PasswordField pfNAmd;
    @FXML
    PasswordField pfNConAdm;
    @FXML
    CheckBox cbAuthority;
    @FXML
    Button btCreate;

    //  Administrator page
    @FXML
    AnchorPane apLogedIn;
    @FXML
    Button btModify;
    @FXML
    Button btAccessAll;
    @FXML
    TextField tfSearch;
    @FXML
    Button btSearch;
    @FXML
    TableView tvData;
    @FXML
    TableColumn tcFN;
    @FXML
    TableColumn tcMI;
    @FXML
    TableColumn tcLN;
    @FXML
    TableColumn tcID;
    @FXML
    Button btAdd;
    @FXML
    Button btView;
    @FXML
    Button btBack;
    @FXML
    Label lbConnection;
    @FXML
    Label lbGreet;

    //  Change the password page
    @FXML
    AnchorPane apChangingPass;
    @FXML
    TextField tfUChange;
    @FXML
    PasswordField pfCChange;
    @FXML
    PasswordField pfNewChange;
    @FXML
    PasswordField pfNewConf;
    @FXML
    Button btChange;

    DBConn dbc;
    //  Flaggy for updata the last log in
    boolean logedIn = false;
    static String idNum;

    //  String for the inputs
    String username, password;
    private ObservableList<Person> data;
    static Person aPerson;
static private Administrator admin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //  Set the menu memociPasing so they can use shortcuts
        mnAccess.setMnemonicParsing(true);
        mnHelp.setMnemonicParsing(true);

        //  Set accelerator fo the menu items
        mniAbout.setAccelerator(KeyCombination.keyCombination("shortcut+B"));
        mniChangePass.setAccelerator(KeyCombination.keyCombination("shortcut+H"));
        mniCreateAdmin.setAccelerator(KeyCombination.keyCombination("shortcut+R"));
        mniLogin.setAccelerator(KeyCombination.keyCombination("shortcut+L"));
        mniQuit.setAccelerator(KeyCombination.keyCombination("shortcut+Q"));
        mniTut.setAccelerator(KeyCombination.keyCombination("shortcut+T"));

        //  Set the anchorPane visible
        apLogingIn.setVisible(true);
        bpUltimate.setVisible(true);
        bpUltimate.setCenter(apLogingIn);
        BookSwapAKM.setStage(450, 285);
        BookSwapAKM.getStage().setTitle("AKM Book Swap 2018");
        ////////////// Make the icon here ******************************************************
        try {
            //  Connect the database
            dbc = new DBConn();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("MySql exception in FXMLDocumentCOntroller class. Connection error!");
            alert.showAndWait();
        }

        BookSwapAKM.getStage().setOnCloseRequest(e -> {
            if (logedIn) {
                try {
                    //  Update the last access of the administrator
                    dbc.updateLastLogin(username);

                } catch (SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("MySql exception in FXMLDocumentCOntroller class. Update latest access error!");
                    alert.showAndWait();
                }
            }

            //  Closing the database connection
            try {
                dbc.closeConnection();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("MySql exception in FXMLDocumentCOntroller class. Closing connection error!");
                alert.showAndWait();
            }
        });

    }

    @FXML
    void login() throws SQLException {

        //  Get the inputted username and pass word and store them into string variable
        username = tfUser.getText();
        password = pfPassword.getText();

        //  Check the length of username and password, limit for username: 20 & limit for password: 25
        if (username.length() > 20 || password.length() > 25) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You are exceeding the limit of " + (username.length() > 20 ? "20" : "25") + " characters for the " + (username.length() > 20 ? "username" : "password"));
            alert.showAndWait();
            //  Stop the login process
            tfUser.clear();
            pfPassword.clear();
            return;
        }

        //  Check whether the fields are empty or not 
        if (isEmptyField(username) || isEmptyField(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You did not enter anything for " + (isEmptyField(username) ? "username" : "password") + ".");
            alert.showAndWait();
            //  Stop the login process
            return;
        }

        //  Validation for existance of the administrator and whether the password matches with the username or not
        if (dbc.doesAdminExist(username)) {
            if (dbc.doesAdminUserMatchPass(username, password)) {

                //  Set the admin
                String []s=dbc.getAdmin(username);
                Administrator adm = new Administrator(s[0], s[1], s[2], s[3]);
                setAdm(adm);
                
                // Bring the second scene
                logedIn = true;
                bpUltimate.setCenter(null);
                bpUltimate.setCenter(apLogedIn);
                BookSwapAKM.setStage(484, 702);
                apLogingIn.setVisible(false);
                apReg.setVisible(false);
                apLogedIn.setVisible(true);
                lbGreet.setText("Hi " + username + "! \nYour last access was: " + dbc.getAdmin(username)[3]);

                // If the person has authority then let them use modify admin
                if (Integer.parseInt(dbc.getAdmin(username)[2]) >= 5) {
                    btModify.setVisible(true);
                } else {
                    btModify.setVisible(false);
                }

                ///  *************************************** Connection label here-----------------------------------------------
            } else {
                //  Error message for the incorret password
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect password");
                alert.showAndWait();
                pfPassword.clear();
            }
        } else {
            //  Error message for not such an administrator
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not find such an administrator");
            alert.showAndWait();
            tfUser.clear();
            pfPassword.clear();
        }

        //  Check for special characters like white spaces that might occur accidentally
        if (doesItContainSpecialChar(username) || doesItContainSpecialChar(password)) {
            //  Error message for having special characters on the inputs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You have typed special characters on your " + (doesItContainSpecialChar(username) ? "username" : "password") + " which might be the reason of invalid login");
            alert.showAndWait();
            tfUser.clear();
            pfPassword.clear();
        }
    }

    @FXML
    void backToLogging() {
        // Bring back to the first scene
        logedIn = false;
        bpUltimate.setCenter(null);
        bpUltimate.setCenter(apLogingIn);
        BookSwapAKM.setStage(450, 285);
        apLogingIn.setVisible(true);
        apLogedIn.setVisible(false);
        apReg.setVisible(false);
        username = null;
        password = null;
        tfUser.clear();
        pfPassword.clear();
        tvData.setItems(null);
    }

    @FXML
    void registerAdmin() {
        //  Bring the user to the registering user interface
        logedIn = false;
        bpUltimate.setCenter(null);
        bpUltimate.setCenter(apReg);
        BookSwapAKM.setStage(600, 405);
        apReg.setVisible(true);
        apLogedIn.setVisible(false);
        apLogingIn.setVisible(false);
        tfUSupAdm.clear();
        pfPSupAdm.clear();
        tfNAdm.clear();
        pfNAmd.clear();
        pfNConAdm.clear();
        tvData.setItems(null);
    }

    @FXML
    void changeAdminPassword() {
        //  Bring the user to the password chnaging scene   
        logedIn = false;
        bpUltimate.setCenter(null);
        bpUltimate.setCenter(apChangingPass);
        BookSwapAKM.setStage(500, 350);
        apReg.setVisible(false);
        apLogedIn.setVisible(false);
        apLogingIn.setVisible(false);
        apChangingPass.setVisible(true);
        tfUChange.clear();
        pfCChange.clear();
        pfNewChange.clear();
        pfNewConf.clear();
        tvData.setItems(null);
    }

    @FXML
    void createAdmin() throws SQLException {
        // String to store the supervisory administrators 
        username = tfUSupAdm.getText();
        password = pfPSupAdm.getText();
        String newAdmUsername = tfNAdm.getText();
        String newAdmPassword = pfNAmd.getText();
        String newAdmCon = pfNConAdm.getText();
        int authorityLevel = 0;
        boolean emptyField = false;
        String alertContent = "You are missing the following:";

        //  Statements to check whether the fields are empty or not
        if (isEmptyField(username)) {
            emptyField = true;
            alertContent += "\n-Supervisory's username";
        }
        if (isEmptyField(password)) {
            emptyField = true;
            alertContent += "\n-Supervisory's password";
        }
        if (isEmptyField(newAdmUsername)) {
            emptyField = true;
            alertContent += "\n-New administrator's username";
        }
        if (isEmptyField(newAdmPassword)) {
            emptyField = true;
            alertContent += "\n-New administrator's password";
        }
        if (isEmptyField(newAdmCon)) {
            emptyField = true;
            alertContent += "\n-Confirmation of the password";
        }
        //  If the any field is empty then send an error message
        if (emptyField) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(alertContent + "\n\tPlease fill them out!");
            alert.showAndWait();
            //  Stop the rest of the process
            return;
        }

        //  Check for the combo box 
        if (cbAuthority.isSelected()) {
            authorityLevel = 5;
        }

        //  Check the length of the password and username
        if (newAdmUsername.length() > 20 || newAdmPassword.length() > 25) {
            //  Error message for exceeding limit of characters
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            String cont = "You have exceeded the limit of";
            if (newAdmUsername.length() > 20 && newAdmPassword.length() > 25) {
                cont += " 20 characters for username \nand 25 characters for password";
                tfNAdm.clear();
                pfNAmd.clear();
                pfNConAdm.clear();
            } else if (newAdmUsername.length() > 20) {
                cont += " 20 characters for username.";
                tfNAdm.clear();

            } else {
                cont += " 25 characters for password.";
                pfNAmd.clear();
                pfNConAdm.clear();
            }
            alert.setContentText(cont);
            alert.showAndWait();
            return;
        }

        // Check whether the supervisory admin exists in the database or not
        if (dbc.doesAdminExist(username) && dbc.doesAdminHaveAuthority(username)) {
            //  Check whether the password and the username matches or not for the admin
            if (dbc.doesAdminUserMatchPass(username, password)) {
                if (doesItContainSpecialChar(newAdmUsername) || doesItContainSpecialChar(newAdmPassword)) {
                    //  Error message for having special characters on the inputs
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("You have typed special characters on your " + (doesItContainSpecialChar(username) ? "username" : "password") + ".");
                    alert.showAndWait();
                    if (doesItContainSpecialChar(username)) {
                        tfNAdm.clear();
                    }
                    pfNAmd.clear();
                    pfNConAdm.clear();
                    //  Stop processing
                    return;
                } else {

                    if (newAdmPassword.equals(newAdmCon)) {

                        if (!dbc.doesAdminExist(newAdmUsername)) {
                            dbc.storeAdmin(newAdmUsername, newAdmPassword, authorityLevel);
                            Alert congratAlert = new Alert(Alert.AlertType.INFORMATION);
                            congratAlert.setTitle("Registration completed");
                            congratAlert.setHeaderText(null);
                            congratAlert.setContentText(newAdmUsername + " has successfully created \nand the account can be used to access the database now."
                                    + (authorityLevel >= 5 ? "\nAlso, the account has the authority to create new administrators" : "\nThe account does not have authority to create new administrators"));
                            ImageView imageView = new ImageView(("/images/checkM.png").toString());
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(50);
                            congratAlert.getDialogPane().setGraphic(imageView);
                            congratAlert.showAndWait();
                            tfUSupAdm.clear();
                            tfNAdm.clear();
                            pfNAmd.clear();
                            pfNConAdm.clear();
                            pfPSupAdm.clear();
                            cbAuthority.setSelected(false);
                        } else {
                            //  Error for overlaping the username
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("The username: \'" + newAdmUsername + "\' has already taken. \nPlease use another one.");
                            alert.showAndWait();
                            // Clear the username fields for the new admin
                            tfNAdm.clear();
                            //  Stop processing
                            return;
                        }
                    } else {
                        //  Error for not matching both password
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("The passwords you entered are not the same.\nPlease remember that these are case sensitive.");
                        alert.showAndWait();
                        // Clear the password fields for the new admin
                        pfNAmd.clear();
                        pfNConAdm.clear();
                        //  Stop processing
                        return;
                    }
                }
            } else {
                //  Error for not matching account
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect password for the supervisory administrator");
                alert.showAndWait();
                pfPSupAdm.clear();
                //  Stop processing
                return;
            }
        } else {
            if (!dbc.doesAdminExist(username)) {
                //  Error for not such an account
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Could not find such an administrator");
                alert.showAndWait();
                tfUSupAdm.clear();
                pfPSupAdm.clear();
                //  Stop the rest of the process
                return;
            } else {
                //  Error for not having enough authority
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("You don't have the authority to create a new administrator ");
                alert.showAndWait();
                tfUSupAdm.clear();
                pfPSupAdm.clear();
                tfNAdm.clear();
                pfNAmd.clear();
                pfNConAdm.clear();

                //  Stop the rest of the process
                return;
            }
        }

    }

    @FXML
    void handleLoadAllData() throws SQLException {
        tvData.setItems(null);
        data = FXCollections.observableArrayList(dbc.loadAll());
        tcFN.setCellValueFactory(new PropertyValueFactory("firstName"));
        tcMI.setCellValueFactory(new PropertyValueFactory("middleName"));
        tcLN.setCellValueFactory(new PropertyValueFactory("lastName"));
        tcID.setCellValueFactory(new PropertyValueFactory("studentId"));
        tvData.setItems(data);
        tvData.setEditable(false);
    }

    @FXML
    void handleLookUp() throws SQLException {
        tvData.setItems(null);
        data = FXCollections.observableArrayList(dbc.lookUp(tfSearch.getText()));
        tcFN.setCellValueFactory(new PropertyValueFactory("firstName"));
        tcMI.setCellValueFactory(new PropertyValueFactory("middleName"));
        tcLN.setCellValueFactory(new PropertyValueFactory("lastName"));
        tcID.setCellValueFactory(new PropertyValueFactory("studentId"));
        tvData.setItems(data);
        tvData.setEditable(false);
    }

    @FXML
    void handleChange() throws SQLException {
        // String to store the supervisory administrators 
        username = tfUChange.getText();
        password = pfCChange.getText();
        String newAdmPassword = pfNewChange.getText();
        String newAdmCon = pfNewConf.getText();
        boolean emptyField = false;
        String alertContent = "You are missing the following:";

        //  Statements to check whether the fields are empty or not
        if (isEmptyField(username)) {
            emptyField = true;
            alertContent += "\n-The username";
        }
        if (isEmptyField(password)) {
            emptyField = true;
            alertContent += "\n-Current password";
        }
        if (isEmptyField(newAdmPassword)) {
            emptyField = true;
            alertContent += "\n-New password";
        }
        if (isEmptyField(newAdmCon)) {
            emptyField = true;
            alertContent += "\n-Confirmation of the password";
        }
        //  If the any field is empty then send an error message
        if (emptyField) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(alertContent + "\n\tPlease fill them out!");
            alert.showAndWait();
            //  Stop the rest of the process
            return;
        }

        //  Check the length of the password and username
        if (username.length() > 20 || newAdmPassword.length() > 25) {
            //  Error message for exceeding limit of characters
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            String cont = "You have exceeded the limit of";
            if (username.length() > 20 && newAdmPassword.length() > 25) {
                cont += " 20 characters for username \nand 25 characters for password";
                tfUChange.clear();
                pfNewChange.clear();
                pfNewConf.clear();
            } else if (username.length() > 20) {
                cont += " 20 characters for username.";
                tfUChange.clear();

            } else {
                cont += " 25 characters for password.";
                pfNewChange.clear();
                pfNewConf.clear();
            }
            alert.setContentText(cont);
            alert.showAndWait();
            return;
        }

        //  Check the existance of the user in the database
        if (dbc.doesAdminExist(username)) {
            if (dbc.doesAdminUserMatchPass(username, password)) {
                if (newAdmPassword.equals(newAdmCon)) {
                    if (!doesItContainSpecialChar(newAdmPassword) || doesItContainSpecialChar(newAdmCon)) {
                        dbc.updateAdminPassword(username, newAdmPassword);
                        Alert congratAlert = new Alert(Alert.AlertType.INFORMATION);
                        congratAlert.setTitle("Request completed");
                        congratAlert.setHeaderText(null);
                        congratAlert.setContentText("You have successfully changed the password!");
                        ImageView imageView = new ImageView(("/images/checkM.png").toString());
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);
                        congratAlert.getDialogPane().setGraphic(imageView);
                        congratAlert.showAndWait();
                        //  Clear everything
                        tfUChange.clear();
                        pfCChange.clear();
                        pfNewConf.clear();
                        pfNewChange.clear();

                    } else {
                        //  Special character error 
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setTitle("Error");
                        alert.setContentText("You entered special characters for your password. Please try again.");
                        alert.showAndWait();
                        //  Clear the fields of password
                        pfNewConf.clear();
                        pfNewChange.clear();
                        //  Stop processing
                        return;
                    }
                } else {
                    //  Both passwords not matching error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("The password you entered are not the same.\nPlease try it again. Remember it is case sensitive");
                    alert.showAndWait();
                    //  Clear the fields of password
                    pfNewConf.clear();
                    pfNewChange.clear();
                    //  Stop processing
                    return;
                }
            } else {
                //  Password not matching error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Password did not match the username: " + username + " !");
                alert.showAndWait();
                //  Clear the fields of password
                pfCChange.clear();
                //  Stop processing
                return;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Could not find such an administrator with the username: " + username + "!");
            alert.showAndWait();
            //  Clear the fields for the user
            tfUChange.clear();
            pfCChange.clear();
            pfNewChange.clear();
            pfNewConf.clear();
            //  Stop processing
            return;
        }
    }

    @FXML
    void addPerson() {
        if (tvData.getSelectionModel().getSelectedItem() != null) {
            Person p = (Person) tvData.getSelectionModel().getSelectedItem();
            setPerson(p);
            //  Load the addSelected window
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddSelected.fxml"));
                Parent root1;
                root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Add new record");
                stage.setScene(new Scene(root1));
                stage.setResizable(false);
                stage.show();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("FXML loader error in FXMLDocumntCOntroller class, handle addPerson inside the if statement! ");
                alert.showAndWait();
            }

        } else {
            //  Load the add window
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Add.fxml"));
                Parent root1;
                root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Add new record");
                stage.setScene(new Scene(root1));
                stage.setResizable(false);
                stage.show();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("FXML loader error in FXMLDocumntCOntroller class, handle addPerson! ");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void handleAbout() {

        //  Load about window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
            Parent root1;
            root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("About");
            stage.setScene(new Scene(root1, 480, 290));
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("FXML loader error in FXMLDocumntCOntroller class, handle handleAbout! ");
            alert.showAndWait();
        }

    }

    @FXML
    void handleView() {

        //  Check if the admin has selected a user
        if (tvData.getSelectionModel().getSelectedItem() == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText("You did not select the person to view!");
            a.showAndWait();
            return; // Stop processing the rest of the code if error encountered
        }

        Person person = (Person) tvData.getSelectionModel().getSelectedItem();
        setId(person.getStudentId());
        /*//System.out.println(person.toString());  Debug statement*/

        //  Load view window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View.fxml"));
            Parent root1;
            root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("View: " + person.getFirstName());
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("FXML loader error in FXMLDocumntController class, handle handleView! ");
            alert.showAndWait();
        }
    }
    
    @FXML
    public void handleModifyAdmin(){
    
        
     //  Load view window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ModifyTheAdmin.fxml"));
            Parent root1;
            root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Administrators modification");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("FXML loader error in FXMLDocumntController class, handle handleView! ");
            alert.showAndWait();
        }
    }

    //  Method Description: This method will be used to check whether the input has invalid content or not
    private boolean doesItContainSpecialChar(String s) {
        return !(s.matches("[#a-zA-Z0-9]+"));
    }

    //  Method Description: This method will check whether the given field is empty or not
    private boolean isEmptyField(String s) {
        return s.length() < 1;
    }

    static public String getId() {
        return idNum;
    }

    static public void setId(String s) {
        idNum = s;
    }

    static public void setPerson(Person p) {
        aPerson = p;
    }

    static public Person getPerson() {
        return aPerson;
    }
    
    
    static public Administrator getAdm(){
        return admin;
    }
    
    static public void setAdm(Administrator adm){
        admin=adm;
    }

}
