/** 
 * Used to check different types of user input.
 */
package uptodate;
import java.util.Scanner;

/**
 *
 * @author marktan
 */
public class CheckInput
{
    /**
    * Checks that the inputted value is an integer.
    * @return the valid input.
    */
   public static int checkInt() {
      Scanner in = new Scanner(System.in);
      int input = 0;
      boolean valid = false;
      while(!valid) {
         if(in.hasNextInt()) {
            input = in.nextInt();
            valid = true;
         } else {
            in.next();
            System.out.println("Invalid Input.");
         }
      }
      return input;
   }
   
   /**
    * Checks that the inputted value is an integer and 
    * within the specified range.
    * @param low lower bound of the range.
    * @param high upper bound of the range.
    * @return the valid input.
    */
   public static int checkIntRange(int low, int high) {
      Scanner in = new Scanner(System.in);
      int input = 0;
      boolean valid = false;
      while(!valid) {
         if(in.hasNextInt()) {
            input = in.nextInt();
            if(input <= high && input >=low) {
               valid = true;
            } else {
               System.out.println("Invalid Input.");
            }
         } else {
            in.next();
            System.out.println("Invalid Input.");
         }
      }
      return input;
   }
   
   /**
    * Checks that the inputted value is a double.
    * @return the valid input.
    */
   public static double checkDouble() {
      Scanner in = new Scanner(System.in);
      double input = 0;
      boolean valid = false;
      while(!valid) {
         if(in.hasNextInt()) {
            input = in.nextDouble();
            valid = true;
         } else {
            in.next();
            System.out.println("Invalid Input.");
         }
      }
      return input;
   }
   
   /**
    * Takes in a string from the user.
    * @return the inputted String.
    */
   public static String getString() {
      Scanner in = new Scanner(System.in);
      String input = in.nextLine();
      return input;
   }
}
