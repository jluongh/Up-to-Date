/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uptodate;
import java.sql.*;
import java.util.*;
/**
 *
 * @author marktan
 */
public class Reminder {
    
    static String USER;
    static String PASS;
    static String DBNAME = "upToDate";
    static final String displayFormat="%-25s%-25s%-25s%-25s\n";
    static final String displayReminderFormat="%-25s%-25s\n";
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    static Connection conn;
    static Statement stmt;
    
    /**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    /**
     * Displays the menu for the user
     * @param in the Scanner object used for user input
     * @return the user-specified option
     */
    public static int askUser(Scanner in)
    {
        System.out.println("What do you want, respectable human being?");
        System.out.println("1. Add Reminder");
        System.out.println("2. View Reminder");
        System.out.println("3. Edit Reminder");
        System.out.println("4. Delete Reminder");
        System.out.println("5. Exit");
        System.out.print("User Input : ");
        return in.nextInt();
    }
    
    /**
     * Creates a reminder for an event complete with a specified date and time
     * @param username the name of the user that created the reminder
     */
    public static void addReminder(String username)
    {
        System.out.print("Event Number: ");
        String eventNo = CheckInput.getString();
        System.out.print("Reminder Date (YYYY-MM-DD): ");
        String reminderDate = CheckInput.getString();
        System.out.print("Reminder Time (HH:MM:SS): ");
        String reminderTime = CheckInput.getString();
        System.out.print("Frequency (daily,weekly,monthly): ");
        String frequency = CheckInput.getString();
        
        try
        {
          String sql = "INSERT INTO REMINDER (USERNAME,EVENTNUMBER,REMINDERDATE,REMINDERTIME,FREQUENCY) "
                     + "VALUES (?,?,?,?,?)";
          PreparedStatement pstmt = conn.prepareStatement(sql);
          
          pstmt.setString(1,username);
          pstmt.setString(2,eventNo);
          pstmt.setString(3,reminderDate);
          pstmt.setString(4,reminderTime);
          pstmt.setString(5,frequency);
          
          pstmt.execute();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
   /**
    * Returns reminder date and time based on the user-specified event
    */
    public static void getReminder()
    {
        System.out.println("Which reminder do you want to see?");
        String eventNo = String.valueOf(CheckInput.checkIntRange(1, 25));
                
        try {
            stmt = conn.createStatement();
            String sql = "SELECT REMINDERDATE,REMINDERTIME FROM REMINDER WHERE EVENTNUMBER = '" + eventNo +"'";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.printf(displayReminderFormat, "Reminder Date", "Reminder Time");
            while (rs.next()) {
                String remDate = rs.getString("reminderdate");
                String remTime = rs.getString("remindertime");

                System.out.printf(displayReminderFormat, dispNull(remDate), dispNull(remTime));
                System.out.println("");
                System.out.println("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
   /**
    * Allows user to update reminder
    */
    public static void editReminder()
    {
      System.out.print("Select event number to edit: ");
      String editEvNum = CheckInput.getString();
      System.out.print("New reminder date: ");
      String newDate = CheckInput.getString();
      System.out.print("New reminder time: ");
      String newTime = CheckInput.getString();
      System.out.print("New frequency: ");
      String newFreq = CheckInput.getString();
      try
      {
        PreparedStatement ps = null;
        String editRem = "update reminder set reminderdate = ?, remindertime = ?, frequency = ? where eventnumber = ?";
        ps = conn.prepareStatement(editRem);
        ps.setString(1,newDate);
        ps.setString(2,newTime);
        ps.setString(3,newFreq);
        ps.setString(4,editEvNum);
        ps.executeUpdate();
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
    }
    
    /**
     * Deletes user-specified reminder from database
     */
    public static void deleteReminder()
    {
        System.out.print("Select event number reminder to delete: ");
        String evNum = CheckInput.getString();
        try
        {
            PreparedStatement pstmt = null;
            String str = "delete from reminder where eventnumber = ?";
            pstmt = conn.prepareStatement(str);
            pstmt.setString(1,evNum);
            pstmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
         System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        conn = null;
        stmt = null;
        try
        {
        //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            int userInput = 0;
            
            do
            {
            userInput = askUser(in);
            switch (userInput)
            {
                case 1:
                    System.out.print("Username: ");
                    String user = CheckInput.getString();
                    addReminder(user);
                    break;
                case 2:
                    getReminder();
                    break;
                case 3:
                    editReminder();
                    break;
                case 4:
                    deleteReminder();
                    break;
                case 5:
                    System.out.println("Goodbye");
                    break;
                default:
                    System.out.print("Invalid Selection");    
            }
            }while(userInput != 5);
            conn.close();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        finally
        {
            try
            {
                if(stmt != null)
                    stmt.close();
            }
            catch(SQLException se2)
            {}
            try
            {
                if(conn != null)
                    conn.close();
            }
            catch(SQLException se)
            {
                se.printStackTrace();
            }
        }
        
    }
}
