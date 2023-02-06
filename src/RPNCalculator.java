/**
 * RPNCalculator
 *
 * The aim of this coursework was to mirror the functionality of a legacy Reverse Polish Notation 
 * (RPN) calculator. It differs from a regular RPN in that numbers beyond the minimum or maximum 
 * values for an integer are clamped instead of wrapped around. The legacy calculator contains various 
 * logical errors and crashes that have been faithfully replicated in this program.
 */

public class RPNCalculator
{
  // Generate random numbers from a seed of 1 when the user types "r".
  private RPNRandom random = new RPNRandom(1);

  // Store operands and the results of operations.
  private RPNStack stack = new RPNStack();

  // Track whether or not we are in a comment and permit multiline comments.
  private boolean inComment = false;

  /**
   * The only public method of the calculator. It tokenises and evaluates numerical expressions,
   * which can be either infix (i.e. "1+2") or postfix (i.e. "1 2 +").
   */
  public void processCommand(String command, boolean ignoreComments)
  {
    for (String token : command.split("\\s+"))
    {
      // Start or end a comment if we find a "#" that isn't in an infix expression.
      if (!ignoreComments && token.matches("#")) {
        inComment = !inComment;
      }
      else if (!inComment && token.length() > 0)
      {
        // If the token is an octal value then convert it to decimal and push it onto the stack.
        if (token.matches("-?0[0-7]+")) {
          stack.push(convertToDouble(token, 8));
        }
        // If the token is a decimal value with a "0" and two or more digits (i.e. "080") then 
        // ignore it like the legacy SRPN does.
        else if (token.matches("-?0\\d{2,}")) {
          continue;
        }
        // If the token is some other decimal value then push it onto the stack.
        else if (token.matches("-?\\d+")) {
          stack.push(convertToDouble(token, 10));
        }
        // If the token is an operator then process it further.
        else if (token.matches("[-+*/%^]")) {
          processOperator(token);
        }
        // If the token is some other non-whitespace character then process it further.
        else if (token.matches("\\S")) {
          processCharacter(token);
        }
        // If the token is an infix expression then convert it to postfix and invoke this function 
        // again to process its individual tokens.
        else {
          RPNInfix infix = new RPNInfix();
          processCommand(infix.convertToPostfix(token), true);
        }
      }
    }
  }

  /**
   * Perform an operation on the top two values of the stack or report an error if there aren't 
   * two values on the stack.
   */
  private void processOperator(String operator)
  {
    if (!stack.underflow())
    {
      double rightOperand = stack.pop();
      double leftOperand = stack.pop();

      boolean success = true;

      switch (operator)
      {
        case "-":
          stack.push(leftOperand - rightOperand);
          break;

        case "+":
          stack.push(leftOperand + rightOperand);
          break;

        case "*":
          stack.push(leftOperand * rightOperand);
          break;

        case "/":
          success = divide(leftOperand, rightOperand);
          break;

        case "%":
          success = modulo(leftOperand, rightOperand);
          break;

        case "^":
          success = pow(leftOperand, rightOperand);
          break;
      }
      if (!success)
      {
        stack.push(leftOperand);
        stack.push(rightOperand);
      }
    }
  }

  /**
   * Handle characters that aren't numbers or operators and print an error message if we 
   * encounter an unrecognised character.
   */
  private void processCharacter(String character)
  {
    switch (character)
    {
      case "d":
        stack.display();
        break;

      case "r":
        if (!stack.overflow()) {
          stack.push(random.nextInt());
        }
        break;

      case "=":
        stack.peek();
        break;

      default:
        System.err.format("Unrecognised operator or operand \"%s\".\n", character);
        break;
    }
  }

  /**
   * Divide two numbers or report an error if we try to divide by 0.
   */
  private boolean divide(double leftOperand, double rightOperand)
  {
    if (rightOperand == 0) {
      System.err.println("Divide by 0.");
      return false;
    }
    stack.push(leftOperand / rightOperand);
    return true;
  }

  /**
   * Perform a modulus operation with additional error checking. Exit with error code 136 
   * (SIGFPE) as in the legacy calculator if a divide by 0 occurs (the check has been applied 
   * to the left operand by mistake in the legacy calculator).
   */
  private boolean modulo(double leftOperand, double rightOperand)
  {
    if (leftOperand == 0) {
      System.err.println("Divide by 0.");
      return false;
    }
    else if (rightOperand == 0) {
      System.exit(136);
    }
    stack.push(leftOperand % rightOperand);
    return true;
  }

  /**
   * Raise a number to a specified power or report an error if the power is negative.
   */
  private boolean pow(double leftOperand, double rightOperand)
  {
    if (rightOperand < 0) {
      System.err.println("Negative power.");
      return false;
    }
    stack.push(Math.pow(leftOperand, rightOperand));
    return true;
  }

  /**
   * Convert a string of a specified base to a double value. The value will be clamped if the 
   * string is a very high or very low number.
   */
  private double convertToDouble(String number, int radix)
  {
    int value = 0;
    try {
      value = Integer.parseInt(number, radix);
    }
    catch (NumberFormatException e) {
      value = number.startsWith("-") ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }
    return (double) value;
  }
}
