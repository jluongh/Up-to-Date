/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uptodate;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author marktan
 */
public class Event {
    //  Database credentials
    final static String DBNAME = "uptoDate";
    static final String displayEventFormat="%-25s%-25s%-25s%-25s%-25s\n";
    static final String displayAllEventFormat="%-25s%-25s%-25s";
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    static Connection conn;
    static Statement stmt;
    
    /**
     * Takes the input string and outputs "N/A" if the string is empty or null.
     *
     * @param input The string to be mapped.
     * @return Either the input string or "N/A" as appropriate.
     */
    public static String dispNull(String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0) {
            return "N/A";
        } else {
            return input;
        }
    }

  public static int askUser() {
        System.out.println("What do you want, respectable human being?");
        Scanner input = new Scanner(System.in);
        System.out.println("1. Add Event");
        System.out.println("2. Check Event Info");
        System.out.println("3. Edit Event Info");
        System.out.println("4. Remove Event");
        System.out.println("0. Exit");
        System.out.print("User Input : ");
        return input.nextInt();
    }
    
    
    public static void addEvent(String username) {
        System.out.println("Event Information is needed to add an event.");
        System.out.println("Title name:");
        String title = CheckInput.getString();
        System.out.println("Start Date (YYYY-MM-DD):");
        String sDate = CheckInput.getString();
        System.out.println("End Date (YYYY-MM-DD):");
        String eDate = CheckInput.getString();
        System.out.println("Location: ");
        String locn = CheckInput.getString();
        System.out.println("Description: ");
        String descrpn = CheckInput.getString();

        
        try {            
            String sql = "INSERT INTO Event (UserName, EventNo, EventName, StartDate, EndDate, Location, Description)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            String sqlTotal = "SELECT " +
                    "Count(EventNo) " +
                    "FROM Event";
            PreparedStatement total = conn.prepareStatement(sqlTotal);
            
            ResultSet rs = total.executeQuery();
            
            String nextEventNo = "";
            
            while (rs.next()) {
                nextEventNo = rs.getString(1);
            }
            
            int nextInt = Integer.parseInt(nextEventNo) + 1;
            
            pstmt.setString(1, username);
            pstmt.setString(2, Integer.toString(nextInt));
            pstmt.setString(3, title);
            pstmt.setString(4, sDate);
            pstmt.setString(5, eDate);
            pstmt.setString(6, locn);
            pstmt.setString(7, descrpn);

            pstmt.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    public static void displayEvents() {
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "Select eventNo, eventName, startDate FROM Event ORDER BY startDate";
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.printf(displayAllEventFormat, "Event Number", "Event Name", "Start Date");
            System.out.println("");
            while (rs.next()) {
                String eventNo = rs.getString("eventNo");
                String eventName = rs.getString("eventName");
                String startDate = rs.getString("startDate");
                
                System.out.printf(displayAllEventFormat, dispNull(eventNo), dispNull(eventName), dispNull(startDate));
                System.out.println("");
                System.out.println("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void getEvent() {

        displayEvents();
        
        System.out.println("Which event do you want to see?");
        String eventNo = String.valueOf(CheckInput.checkIntRange(1, 5));
                
        try {
            stmt = conn.createStatement();
            String sql = "SELECT eventName, startDate, endDate, location, description FROM Event WHERE eventNo = '" + eventNo +"'";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.printf(displayEventFormat, "Event Name", "Start Date", "End Date", "Location", "Description");
            while (rs.next()) {
                String eventName = rs.getString("eventName");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                String location = rs.getString("location");
                String description = rs.getString("description");

                System.out.printf(displayEventFormat,
                        dispNull(eventName), dispNull(startDate), dispNull(endDate), dispNull(location), dispNull(description));
                System.out.println("");
                System.out.println("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
  
    public static void editEvent () {
        
        displayEvents();
        
        System.out.println("Which event would you like to edit?");
        String eventNo = String.valueOf(CheckInput.checkInt());
        
        System.out.println("What to edit?");
        System.out.println("1. Title");
        System.out.println("2. Start Date");
        System.out.println("3. End Date");
        System.out.println("4. Location");
        System.out.println("5. Description");
        
        int edit = CheckInput.checkIntRange(1, 5);
        String thingToEdit = "";
        switch (edit) {
            case 1: 
                thingToEdit = "EventName";
                break;
            case 2:
                thingToEdit = "StartDate";
                break;
            case 3:
                thingToEdit = "EndDate";
                break;
            case 4:
                thingToEdit = "Location";
                break;
            case 5:
                thingToEdit = "Description";
                break;
        }
        
        System.out.println("What do you want to change it to?");
        String change = CheckInput.getString();
        
        try {    
            String sql = "UPDATE Event SET "+ thingToEdit + " = ? WHERE EventNo = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, change);
            pstmt.setString(2, eventNo);
            
            pstmt.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void removeEvent () {
        //displayEvents();
        
        System.out.println("Which event would you like to remove?");
        String remove = String.valueOf(CheckInput.checkInt());
        try {
            String sql = "DELETE FROM Event "
            + "WHERE EventNo = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, remove);
            pstmt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME;
        conn = null; //initialize the connection
        stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            int userInput = 0;

            do {
                userInput = askUser();
                switch (userInput) {
                    case 1:
                        addEvent("jen");
                        break;
                    case 2:
                        getEvent();
                        break;
                    case 3:
                        editEvent();
                        break;
                    case 4:
                        removeEvent();
                        break;
                    case 0:
                        break;
                }
            } while (userInput != 0);

            //STEP 6: Clean-up environment
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main

    
}
