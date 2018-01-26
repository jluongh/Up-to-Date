




package uptodate;

import java.sql.*;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class User {

    static String USER = "jen";
    static String PASS = "jen";
    static String DBNAME = "uptodate1";
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    static Connection conn;
    static Statement stmt;
    private static String username;

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

    /**
     * Displays the menu for the user
     *
     * @param in the Scanner object used for user input
     * @return the user-specified option
     */
    public static int askUser() {
        System.out.println("What do you want, respectable human being?");
        System.out.println("1. Edit Username");
        System.out.println("2. Edit Password");
        System.out.println("3. Delete Account");
        System.out.println("0. Exit");
        System.out.print("User Input : ");
        return CheckInput.checkIntRange(0, 3);
    }

    public static int askLogIn() {
        System.out.println("What would you like to do?");
        System.out.println("1. Create Account");
        System.out.println("2. Sign In");
        System.out.println("0. Exit");
        return CheckInput.checkIntRange(0, 2);
    }

    public static void createAccount(String u, String p) {
        String password;
        username = u;
        password = p;
        
        try {
            PreparedStatement pstmt = null;
            String sql = "SELECT * FROM \"USER\" WHERE LOWER(username) = LOWER(?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                System.out.println("Username is taken. Try another username.");
                username = CheckInput.getString();
                pstmt.setString(1, username);
                rs = pstmt.executeQuery();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement pstmt = null;
            String sql = "INSERT INTO \"USER\" (username, password) VALUES (?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows user to enter application
     */
    // code does not work when called in a GUI, works when done on console
    public static void signIn() {
        System.out.print("Username: ");
        String uName = CheckInput.getString();
        System.out.print("Password: ");
        String pWord = CheckInput.getString();

        try {
            PreparedStatement ps = null;
            String signIn = "select username, password from \"USER\" where username = ? and password = ?";
            ps = conn.prepareStatement(signIn);
            ps.setString(1, uName);
            ps.setString(2, pWord);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //UpToDateUI is the class name of the GUI for the main calendar page

                //sign in
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

      	/**
     * Allows user to update username
     * @param in the Scanner object used for user input
     */
    public static void editUsername() {
        System.out.print("Type in username to edit: ");
      	String oldName = CheckInput.getString();
      	System.out.print("Type in new username: ");
      	String newName = CheckInput.getString();
      	try
        {
          PreparedStatement editName = null;
          String editNameSQL = "UPDATE \"USER\" SET username = ? WHERE username = ?";
          editName = conn.prepareStatement(editNameSQL);
          editName.setString(1,newName);
          editName.setString(2,oldName);
          editName.executeUpdate();
        }
      	catch(Exception e)
        {
          e.printStackTrace();
        }
    }

    /**
     * Allows user to update password
     */
    public static void editPassword() {
        //Without the username in the WHERE clause, the update statement will edit all rows with user-specified password.
      	//That means multiple users with the same password will be edited, and we do not want that.
      	//Therefore, we must also ask for username.
        System.out.print("Type in username to edit: ");
      	String name = CheckInput.getString();
      	System.out.print("Type in old password: ");
      	String oldPW = CheckInput.getString();
      	System.out.print("Type in new password: ");
      	String newPW = CheckInput.getString();
      	try
        {
          PreparedStatement editPW = null;
          String editPWSQL = "UPDATE \"USER\" SET password = ? WHERE username = ? AND password = ?";
          editPW = conn.prepareStatement(editPWSQL);
          editPW.setString(1,newPW);
          editPW.setString(2,name);
          editPW.setString(3,oldPW);
          editPW.executeUpdate();
        }
      	catch(Exception e)
        {
          e.printStackTrace();
        }
    }


    public static void deleteAccount() {
        System.out.println("Are you sure you want to delete your account?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        
        int userInput = CheckInput.checkIntRange(1, 2);
        
        if (userInput == 1) {
            try {
                PreparedStatement pstmt = null;
                String sql = "DELETE FROM USER WHERE username = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
            
            } catch (SQLException ex) {
                
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
       DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            int userSignIn = 0, userMenu = 0;

            do {
                userSignIn = askLogIn();
                switch (userSignIn) {
                    case 1:
//                        CreateAccount f = new CreateAccount();
//                        f.setSize(300,200);
//                        f.setVisible(true);
                        break;
                    case 2:
                        userMenu = askUser();
                        do {
                            switch (userMenu) {
                                case 1:
                                    editUsername();
                                    break;
                                case 2:
                                    editPassword();
                                    break;
                                case 3:
                                    deleteAccount();
                                    break;
                                case 0:
                                    break;
                            }
                        } while (userMenu != 0);
                        break;
                }
            } while (userSignIn != 0);

            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

    public User() {
    }
}
