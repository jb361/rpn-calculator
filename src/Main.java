/**
 * Main
 *
 * The aim of this coursework was to mirror the functionality of a legacy Reverse Polish Notation 
 * (RPN) calculator. It differs from a regular RPN in that numbers beyond the minimum or maximum 
 * values for an integer are clamped instead of wrapped around. The legacy calculator contains various 
 * logical errors and crashes that have been faithfully replicated in this program.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;

class Main
{
  /**
   * The main entry point of the application. Read input from the command line and pass each 
   * line to the processCommand() method in the RPNCalculator class.
   */
  public static void main(String[] args)
  {
    RPNCalculator calc = new RPNCalculator();

    // Create a buffered reader to capture user input from the command line. The charset is set to 
    // US-ASCII to match the legacy calculator.
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, 
        Charset.forName("US-ASCII")));

    try {
      while (true)
      {
        String command = reader.readLine();

        // Close the application when Ctrl + D is pressed in the terminal.
        if (command == null) {
          System.exit(0);
        }
        // The legacy calculator crashes with error code 139 (SIGSEGV) for long input lines that
        // exceed about 128 characters.
        else if (command.length() > 128) {
          System.exit(139);
        }
        calc.processCommand(command, false);
      }
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
