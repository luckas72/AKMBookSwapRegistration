/*
Name        : BookSwap event software for Alpha Kappa Mu
Description : This project will be used to help AKM club to sort the information for the book swap event
Author      : Luckas(Yinghong) Huang
Date        : Dec/23th/2018
 */
package bookswapakm;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class BookSwapAKM extends Application {

   static private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {

        // Set the stage to the primaryStage
        primaryStage=stage;
        
        //  Get the front end from the FXML
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        //  Set it to the scene
        Scene scene = new Scene(root);

        //  Set the scene to the primary stage
        primaryStage.setResizable(false);
       primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    static public void setStage(double width,double height){
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }
    
   static public Stage getStage(){
        return primaryStage;
    }
    
}
