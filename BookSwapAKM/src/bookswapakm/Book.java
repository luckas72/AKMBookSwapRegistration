/*
Name        : Person object class for observable list in the main program of Book Swap AKM 2018
Description : This class will encapsulate the book info.
Author      : Luckas(Yinghong) Huang
Date        : Jan/1st/2019
 */
package bookswapakm;

import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleStringProperty status;
    private SimpleStringProperty providedBook;
    private SimpleStringProperty receivedBook;


    
    // Default constructor
    public Book(){
        
    }
    
    // Argumental constructor
    public Book(String status, String providedBook,String receivedBook){
        this.receivedBook= new SimpleStringProperty(receivedBook);
        this.providedBook= new SimpleStringProperty(providedBook);
        this.status=new SimpleStringProperty(status);
    }
    
    // -----------------------------------------------------------  Setters ---------------------------
    public void setReceivedBook(String receivedBook){
        this.receivedBook.set(receivedBook);
    }
    
    public void setProvidedBook(String providedBook){
        this.providedBook.set(providedBook);
    }
    
    public void setStatus(String status){
        this.status.set(status);
    }
    
    // -----------------------------------------------------------  Getters ---------------------------
    public String getStatus(){
        return this.status.get();
    }
    public String getReceivedBook(){
        return this.receivedBook.get();
    }
    public String getProvidedBook(){
        return this.providedBook.get();
    }
    
        @Override
    public String toString() {
        return "Book{" + "status=" + status + ", providedBook=" + providedBook + ", receivedBook=" + receivedBook + '}';
    }
}
