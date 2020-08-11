/*
Name        : Person object class for observable list in the main program of Book Swap AKM 2018
Description : This class will encapsulate the admin info.
Author      : Luckas(Yinghong) Huang
Date        : Jan/1st/2019
 */
package bookswapakm;

import javafx.beans.property.SimpleStringProperty;

public class Administrator {
    
    private SimpleStringProperty admName;
    private SimpleStringProperty admPass;
    private SimpleStringProperty admLevel;
    private SimpleStringProperty admLastAccess;
    
    //  Constructors
    public Administrator(){
        
    }
    public Administrator(String admName,String admPass,String admLevel, String admLastAccess){
        this.admLastAccess= new SimpleStringProperty(admLastAccess);
        this.admLevel=new SimpleStringProperty(admLevel);
        this.admName=new SimpleStringProperty(admName);
        this.admPass=new SimpleStringProperty(admPass);
    }
    
    //---------------------------------------------------------------- Setters --------------------------------
    
    public void setAdmName(String admName){
        this.admName.set(admName);
    }
    public void setAdmPass(String admPass){
        this.admPass.set(admPass);
    }
    public void setAdmLevel(String admLevel){
        this.admLevel.set(admLevel);
    }
    public void setAdmLastAccess(String admLastAccess){
        this.admLastAccess.set(admLastAccess);
    }
    
    //---------------------------------------------------------------- Getters --------------------------------
    public String getAdmName(){
        return this.admName.get();
    }
    
    public String getAdmPass(){
        return this.admPass.get();
    }
    
    public String getAdmLevel(){
        return this.admLevel.get();
    }
   public String getAdmLastAccess(){
       return this.admLastAccess.get();
   } 
   
}
