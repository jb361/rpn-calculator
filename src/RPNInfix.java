/**
 * RPNInfix
 *
 * Implement the "shunting-yard" algorithm to convert an infix expression (i.e. "1+2*3") to a 
 * postfix expression (i.e. "1 2 3 * +"). It has been modified in two ways to match the broken 
 * behaviour of the legacy calculator. Firstly, the BODMAS order is slightly different (aside from 
 * brackets being unsupported, divide > multiply and add > subtract). Secondly, when the operator 
 * at the top of the stack has a higher precedence than the current operator, the operator stack 
 * is cleared rather than just removing the operators with a higher precedence.
 *
 * https://en.wikipedia.org/wiki/Shunting-yard_algorithm
 */

import java.util.Stack;

public class RPNInfix
{
  // The postfix expression to output.
  private StringBuilder postfix = new StringBuilder();

  // A stack to hold the operators before they get added to the postfix expression.
  private Stack<Integer> operatorStack = new Stack<Integer>();

  // The operators that are supported, in order of precedence from lowest to highest.
  private static final String OPERATORS = "-+*/%^";

  // This is required to record whether a number has a unary minus infront of it.
  private boolean unaryMinus = false;

  /**
   * Convert an infix expression to a postfix expression. The expression is split into numbers, 
   * operators and characters before being processed separately.
   */
  public String convertToPostfix(String infix)
  {
    String[] tokens = infix.split("(?=\\D)|(?<=\\D)");

    for (int i = 0; i < tokens.length; ++i)
    {
      String prevToken = i > 0 ? tokens[i-1] : "";
      String nextToken = i < tokens.length - 1 ? tokens[i+1] : "";

      int index = OPERATORS.indexOf(tokens[i]);

      if (index != -1) {
        processOperator(tokens[i], prevToken, nextToken, index);
      }
      else {
        processNumberOrCharacter(tokens[i]);
      }
    }
    clearOperatorStack();

    return postfix.toString();
  }

  /**
   * Handle operators, which get placed in an operator stack before being added to the postfix 
   * expression based on their precedence. Also handle unary minus in the same weird way as the 
   * legacy calculator by toggling a boolean whenever a possible unary minus is found.
   */
  private void processOperator(String token, String prevToken, String nextToken, int index)
  {
    unaryMinus = token.matches("-") && !prevToken.matches("\\d+")
        ? !unaryMinus : false;

    if (!(unaryMinus && nextToken.matches("\\d+")))
    {
      if (!operatorStack.isEmpty() && operatorStack.peek() > index) {
        clearOperatorStack();
      }
    }
    operatorStack.push(index);
  }

  /**
   * Handle numbers and characters, which get added directly to the postfix expression. If we 
   * encounter a "d" then we need to clear the operator stack ("1+2*4" != "1d+2d*4d"). If the 
   * number is negative then pop a unary minus from the stack to prefix it with.
   */
  private void processNumberOrCharacter(String token)
  {
    if (unaryMinus && token.matches("\\d+") && !operatorStack.isEmpty())
    {
      token = OPERATORS.charAt(operatorStack.pop()) + token;
      unaryMinus = false;
    }
    else if (token.matches("d")) {
      clearOperatorStack();
    }
    postfix.append(token).append(" ");
  }

  /**
   * Move all of the operators from the stack to the postfix expression.
   */
  private void clearOperatorStack()
  {
    while (!operatorStack.isEmpty()) {
      postfix.append(OPERATORS.charAt(operatorStack.pop())).append(" ");
    }
  }
}
