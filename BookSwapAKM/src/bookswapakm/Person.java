/*
Name        : Person object class for observable list in the main program of Book Swap AKM 2018
Description : This class will encapsulate the book info.
Author      : Luckas(Yinghong) Huang
Date        : Dec/28th/2018
 */
package bookswapakm;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    
    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty studentId;
    
    
    // Default constructor
    public Person(){
        
    }
    //  Argumental constructor
    public Person(String firstName, String middleName, String lastName, String studentId) {
        this.firstName = new SimpleStringProperty(firstName);
        this.middleName = new SimpleStringProperty(middleName);
        this.lastName = new SimpleStringProperty(lastName);
        this.studentId = new SimpleStringProperty(studentId);
    }

    //----------------------------------------------------------     Setters ----------------------------------------------------------------
    public void setFirstName(String firstName) {
        this.firstName.set(firstName); 
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    //----------------------------------------------------------     Getters ----------------------------------------------------------------    
    public String getFirstName() {
        return this.firstName.get();
    }

    public String getMiddleName() {
        return this.middleName.get();
    }

    public String getLastName() {
        return this.lastName.get();
    }

    public String getStudentId() {
        return this.studentId.get();
    }

   
    
    @Override
    public String toString() {
        return "Person{" + "firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", studentId=" + studentId + '}';
    }
}
