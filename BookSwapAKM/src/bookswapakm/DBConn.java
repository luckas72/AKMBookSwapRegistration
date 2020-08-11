/*
Name        : Database class for BookSwap event software for Alpha Kappa Mu
Description : This class will help the main program to retrieve, update and store data to database.
Author      : Luckas(Yinghong) Huang
Date        : Dec/28th/2019
 */
package bookswapakm;

import java.sql.*;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author Luckas
 */
public class DBConn {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/akmbookswap?autoReconnect=true&useSSL=false";
    String query;        //  Declare the query execute string

    /*Back up url*/
    // jdbc:mysql://localhost/akmbookswap
    public DBConn() throws SQLException {

        try {
            //  Connect the driver
            Class.forName(DRIVER);
            //  Database: akmbookswap
            //  Username: LuckasAKM
            //  Password: akm2018bhcc
            con = DriverManager.getConnection(DATABASE_URL, "LuckasAKM", "akm2018bhcc");

        } catch (ClassNotFoundException ex) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Class Not Found Exception in getting connected the MySQL driver");
            alert.showAndWait();
            
        }
    }

    //  Method Description: This method will check and return whether the administrator exist or not
    public boolean doesAdminExist(String username) throws SQLException {

        // Try to esecute and get the result set
        try {
            //  Initialize the query and set it to the preparedStatement instance
            query = ("Select count(*) from admininfo where admin_name= ?");
            pst = con.prepareStatement(query);

            //  Set the missing field of the preparedStatement
            pst.setString(1, username);

            //  Get the resultSet from the database
            rs = pst.executeQuery();
            rs.next();

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            query = null;
        }

        // After the result is retrieved, then validate and return
        return rs.getInt(1) > 0;
    }

    //  Method description: This method will check and return whether the administrator's username matches with their password
    public boolean doesAdminUserMatchPass(String username, String enteredPass) throws SQLException {

        // Try to esecute and get the result set 
        try {
            //  Initialize the query and set it to the preparedStatement instance
            query = ("Select admin_pass from admininfo where admin_name= ?");
            pst = con.prepareStatement(query);

            //  Set the missing field of the preparedStatement
            pst.setString(1, username);

            //  Get the resultSet from the database
            rs = pst.executeQuery();
            rs.next();

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            query = null;
        }

        // After the result is retrieved, then validate and return
        return rs.getString(1).equals(enteredPass);
    }

    //  Method description: This method will check and return whether the administrator's authoirty level is high enough to create new admins
    public boolean doesAdminHaveAuthority(String username) throws SQLException {

        // Try to esecute and get the result set
        try {
            //  Initialize the query and set it to the preparedStatement instance
            query = ("Select admin_authority_level from admininfo where admin_name= ?");
            pst = con.prepareStatement(query);

            //  Set the missing field of the preparedStatement
            pst.setString(1, username);

            //  Get the resultSet from the database
            rs = pst.executeQuery();
            rs.next();

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            query = null;
        }

        // After the result is retrieved, then validate and return
        return rs.getInt(1) >= 5;
    }

    //  Method description: This method will get the info of the administrator
    public String[] getAdmin(String username) throws SQLException {

        // Try to esecute and get the result set 
        try {
            //  Initialize the query and set it to the preparedStatement instance
            query = ("Select * from admininfo where admin_name= ?");
            pst = con.prepareStatement(query);

            //  Set the missing field of the preparedStatement
            pst.setString(1, username);

            //  Get the resultSet from the database
            rs = pst.executeQuery();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            query = null;
        }

        String[] adminInfoArray = null;

        //  Looping through database 
        while (rs.next()) {
            String adminN = rs.getString("admin_name");
            String adminP = rs.getString("admin_pass");
            String adminA = Integer.toString(rs.getInt("admin_authority_level"));
            String adminD = rs.getDate("last_login").toString();
            adminInfoArray = new String[]{adminN, adminP, adminA, adminD};
        }

        // Return the arrayList once it is done
        return adminInfoArray;
    }

    //  Method description: This method will store the info of the administrator to the database
    public void storeAdmin(String adminN, String adminP, int adminA) throws SQLException {

        //  Initialize the query and set it to the preparedStatement instance
        query = ("insert into admininfo values(?,?,?,?)");
        pst = con.prepareStatement(query);

        //  Set the missing field of the preparedStatement
        pst.setString(1, adminN);
        pst.setString(2, adminP);
        pst.setInt(3, adminA);
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        pst.setDate(4, date);

        //  Store
        pst.executeUpdate();
    }

    //  Method description: This method will store the info of the person to the database
    public void storePerson(String firstName, String middleName, String lastName, String studentId) throws SQLException {

        //  Check the student has already register or not
        query = ("select count(*) from personinfo where studentId = ? ");
        pst = con.prepareStatement(query);
        pst.setString(1, studentId);
        rs = pst.executeQuery();
        rs.next();
        int count = rs.getInt(1);  // Getting the number of existance in database

        //  If the number is less than one which means they have not registered yet such that we can register otherwise just ignore
        if (count < 1) {

            //  Initialize the query and set it to the preparedStatement instance
            query = ("insert into personinfo values(?,?,?,?)");
            pst = con.prepareStatement(query);

            //  Set the missing field of the preparedStatement
            pst.setString(1, firstName);
            pst.setString(2, middleName);
            pst.setString(3, lastName);
            pst.setString(4, studentId);

            //  Store
            pst.executeUpdate();
        }
    }

    //  Method description: This method will look up the existance of the person
    public boolean doesPersonExist(String studentId) throws SQLException {
        //  Check the student has already register or not
        query = ("select count (*) from personinfo where studentId = ? ");
        pst = con.prepareStatement(query);
        pst.setString(1, studentId);
        rs = pst.executeQuery();
        rs.next();
        int count = rs.getInt(1);  // Getting the number of existance in database

        return count < 1;
    }

    //  Method description: This method will store the info of the book to the database
    public void storePerformance(String studentId, String status, String providedBook, String receivedBook) throws SQLException {

        //  Initialize the query and set it to the preparedStatement instance
        query = ("insert into bookinfo values(?,?,?,?)");
        pst = con.prepareStatement(query);

        //  Set the missing field of the preparedStatement
        pst.setString(1, studentId);
        pst.setString(2, status);
        pst.setString(3, providedBook);
        pst.setString(4, receivedBook);

        //  Store
        pst.executeUpdate();
    }

    //  Method description: This method will update the last login of the admin
    public void updateLastLogin(String adminN) throws SQLException {

        query = ("update admininfo set last_login = ? where admin_name =?");
        pst = con.prepareStatement(query);
        //  Made a MySql date class
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        //  Setting the missing fields of the preparedStatement
        pst.setDate(1, date);
        pst.setString(2, adminN);

        //  Update.
        pst.executeUpdate();
    }

    //  Method desciption: This method will update any desired field for the personTable by the person
    public void updatePersonInfo(String field, String newData, String studentId) throws SQLException {
        // Initialize a query that will fit all the fields
        query = ("Update personinfo set " + field + "=? where studentId=?");
        pst = con.prepareStatement(query);

        //  Set the missing fields
        pst.setString(1, newData);
        pst.setString(2, studentId);

        //  Update
        pst.executeUpdate();

        //  If the changing field is the studentId then change it in the bookInfo table as well.
        if (field.equals("studentId")) {
            this.updateBookInfo("studentId", newData, studentId);
        }
    }

    //  Method desciption: This method will update any desired field for the bookTables by the person
    public void updateBookInfo(String field, String newData, String studentId) throws SQLException {
        // Initialize a query that will fit all the fields
        query = ("Update bookinfo set " + field + "=? where studentId=?");
        pst = con.prepareStatement(query);

        //  Set the missing fields
        pst.setString(1, newData);
        pst.setString(2, studentId);

        //  Update
        pst.executeUpdate();
    }

    //  Method description: This method will update the password for the administrator
    public void updateAdminPassword(String adminN, String adminP) throws SQLException {
        query = ("Update admininfo set admin_pass=? where admin_name=?");
        pst = con.prepareStatement(query);

        //  Set the missing fields
        pst.setString(1, adminP);
        pst.setString(2, adminN);

        // Update
        pst.executeUpdate();
    }

    //  Method description: This method will look up for keyword in database
    public ArrayList<Person> lookUp(String keywordContent) throws SQLException {

        
        //  Four different string query to look for all fields
        String query1 = ("Select * from personinfo where FirstN like ?");
        String query2 = ("Select * from personinfo where MiddleI like ?");
        String query3 = ("Select * from personinfo where LastN like ?");
        String query4 = ("Select * from personinfo where studentId like ?");
        ArrayList<Person> peopleAL = new ArrayList<>();

        //  Four result set to get each query result
        pst = con.prepareStatement(query1);
        pst.setString(1, "%"+keywordContent+"%");
        rs = pst.executeQuery();
          while (rs.next()) {
            peopleAL.add(new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        //--
       pst = con.prepareStatement(query2);
        pst.setString(1, "%"+keywordContent+"%");
        rs = pst.executeQuery();
          while (rs.next()) {
            peopleAL.add(new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        //--
        pst = con.prepareStatement(query3);
        pst.setString(1, "%"+keywordContent+"%");
        rs = pst.executeQuery();
          while (rs.next()) {
            peopleAL.add(new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        //--
        pst = con.prepareStatement(query4);
        pst.setString(1, "%"+keywordContent+"%");
        rs = pst.executeQuery();
          while (rs.next()) {
            peopleAL.add(new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
 
        return peopleAL;
    }

    //  Method description: This method will load all the people in the database
    public ArrayList<Person> loadAll() throws SQLException {

        // Get all participants' data
        pst = con.prepareStatement("Select * from personinfo");
        rs = pst.executeQuery();

        ArrayList<Person> peopleAL = new ArrayList<>();

        // Store the string into the arrayList
        while (rs.next()) {
            peopleAL.add(new Person(rs.getString("FirstN"), rs.getString("MiddleI"), rs.getString("LastN"), rs.getString("studentId")));
        }

        //  Return the arrayList
        return peopleAL;
    }

    
     //  Method description: This method will load all the people in the database
    public ArrayList<Book> loadParticipantsBook(String personId) throws SQLException {

        // Get all data of the participant
        pst = con.prepareStatement("Select * from bookinfo where studentId=?");
        pst.setString(1, personId);
        rs = pst.executeQuery();

        ArrayList<Book> bookAL = new ArrayList<>();

        // Store the string into the book arrayList
        while (rs.next()) {
            bookAL.add(new Book(rs.getString("Status"), rs.getString("ProvidedBook"), rs.getString("ReceivedBook")));
        }

        //  Return the arrayList
        return bookAL;
    }
    
    //  Method description: This method will delete the book info of the user
    public void deleteBookInfo(String id,String status, String providedBook) throws SQLException{
        //  Setting up the deleting query
        query = ("Delete from bookinfo where studentId=? AND Status=? AND ProvidedBook=?");
        // Set up the prepared statement
        pst=con.prepareStatement(query);
        pst.setString(1, id);
        pst.setString(2, status);
        pst.setString(3, providedBook);
        
        //  Execute the query
        pst.executeUpdate();
    }
    
    //  Method description: Close the connection of the database.
    public void closeConnection() throws SQLException {
        con.close();
    }

    //  Method desciption: Get the person
    public ArrayList<Person> getPerson(String id) throws SQLException{
        query = ("Select * from personinfo where studentId=?");
        pst=con.prepareStatement(query);
        pst.setString(1, id);
        rs=pst.executeQuery();
        ArrayList<Person>personAL = new ArrayList<>();
        
        //  Store the thing into arraylist
        while(rs.next()){
            personAL.add(new Person(rs.getString("FirstN"), rs.getString("MiddleI"), rs.getString("LastN"), rs.getString("studentId")));
        }
        
        return personAL;
    }
    
     //  Method description: This method will get the info of the administrator
    public ArrayList<Administrator>getAdminArrayList(String level) throws SQLException {

        // Try to esecute and get the result set 
        try {
            //  Initialize the query and set it to the preparedStatement instance
            query = ("Select * from admininfo where admin_authority_level< ?");
            pst = con.prepareStatement(query);
            pst.setString(1, level);

            //  Get the resultSet from the database
            rs = pst.executeQuery();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        ArrayList<Administrator>admAL =new ArrayList<>();

        //  Looping through database 
        while (rs.next()) {
            admAL.add(new Administrator(rs.getString("admin_name"), rs.getString("admin_pass"), rs.getString("admin_authority_level"), rs.getString("last_login")));
            
        }

        // Return the arrayList once it is done
        return  admAL;
    }
    
    //  Method description: This method will handle the deletion of the admin
    public void deleteAdmim(String username) throws SQLException{
        query=("Delete from admininfo where admin_name=?");
        pst=con.prepareStatement(query);
        pst.setString(1, username);
        //  Delete
        pst.executeUpdate();
    }
    
    //  Method description: This method will change the authority level of the person
    public void modifyAdmin(String username) throws SQLException{
        query = ("Select * from admininfo where admin_name=?");
        pst=con.prepareStatement(query);
        pst.setString(1, username);
       
        rs = pst.executeQuery();
        
        rs.next();
        int level = Integer.parseInt(rs.getString("admin_authority_level"));
        
        if(level<5){
            level = 5;
            
        }else level=0;
        
        query =("Update admininfo set admin_authority_level = ? where admin_name=?");
        pst=con.prepareStatement(query);
        pst.setInt(1, level);
        pst.setString(2, username);
        pst.executeUpdate();
    }
}
