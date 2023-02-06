/**
 * RPNStack
 *
 * A wrapper class for a Java Stack to add functionality required by the calculator such as
 * checking for underflow and overflow and printing error messages. Values on the stack are 
 * saturated and stored as doubles but printed as integers.
 */

import java.util.Stack;

public class RPNStack
{
  // A stack to store operands and the results of operations.
  private Stack<Double> stack = new Stack<Double>();

  // The minimum number of values required on the stack to perform an operation.
  private static final int MIN_SIZE = 2;

  // The maximum number of values that can be added to the stack.
  private static final int MAX_SIZE = 23;

  /**
   * Push a saturated double value onto the stack. The value will be clamped to the range 
   * -2147483648 <= value <= 2147483647. 
   */
  public void push(double value)
  {
    if (!overflow()) {
      stack.push(Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, value)));
    }
  }

  /**
   * Pop the top value of the stack or returns -2147483648 if the stack is empty.
   */
  public double pop()
  { 
    return !stack.empty() ? stack.pop() : Integer.MIN_VALUE;
  }

  /**
   * Print the top value of the stack as an integer or an error message if the stack is empty.
   */
  public void peek()
  {
    if (!stack.empty()) {
      System.out.println(stack.peek().intValue());
    }
    else {
      System.err.println("Stack empty.");
    }
  }

  /**
   * Print all the values on the stack as integers or -2147483648 if the stack is empty.
   */
  public void display()
  {
    if (!stack.empty())
    {
      for (Double d : stack) {
        System.out.println(d.intValue());
      }
    }
    else {
      System.err.println(Integer.MIN_VALUE);
    }
  }

  /**
   * Check that the stack size is above 2 and print an error message if not. We need 2 values on 
   * the stack to perform an operation.
   */
  public boolean underflow()
  {
    if (stack.size() < MIN_SIZE)
    {
      System.err.println("Stack underflow.");
      return true;
    }
    return false;
  }

  /**
   * Check that the stack size is below 23 and print an error message if not.
   */
  public boolean overflow()
  {
    if (stack.size() >= MAX_SIZE)
    {
      System.err.println("Stack overflow.");
      return true;
    }
    return false;
  }
}
