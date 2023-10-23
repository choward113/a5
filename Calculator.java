import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.lang.RuntimeException;

/**
 * Calculator for equations given in infix format.
 * @author Camille Howard
 * @version October 23, 2023. CSC 210.
 */
public class Calculator {
    /** Takes in ArrayDeque as a queue and reformats it to be in postfix format.
     * @param args The string to convert.
     * @return The string converted to a queue.
     * @throws RuntimeException if parentheses are mismatched.
     */
    private ArrayDeque<Object> shuntYard(String[] args) {
        ArrayDeque<Object> originalQueue = Tokenizer.main(args); //original queue
        ArrayDeque<Object> queue = new ArrayDeque<Object>(args.length); //output queue
        ArrayDeque<Character> stack = new ArrayDeque<Character>(); //operators and parentheses

        for (Object token : originalQueue) {
            if (token instanceof Double) {
                queue.addLast(token);
            } else if (token instanceof Character) {
                char operator = (Character) token;
                if (operator == '(') {
                    stack.addFirst(operator); // Push left parenthesis onto stack
                } else if (operator == ')') {
                    while (!stack.isEmpty() && stack.getFirst() != '(') {
                        queue.addLast(stack.removeFirst()); // Pop operators off stack and onto queue queue
                    }
                    if (!stack.isEmpty() && stack.getFirst() == '(') {
                        stack.removeFirst(); //Pop the left parenthesis from stack
                    } else {
                        throw new RuntimeException("Error: Mismatched parentheses.");
                    }
                } else {
                    while (!stack.isEmpty() && stack.getFirst() instanceof Character) {
                        char topOperator = stack.getFirst();
                        if (priority(topOperator) >= priority(operator)) {
                            queue.addLast(stack.removeFirst()); // Pop stack operator and add to queue
                        } else {
                            break; //no more high priority operators, leave while loop/go back to for loop
                        }
                    }
                    stack.push(operator); // Push queue operator onto stack
                }
            }
        }
        while (!stack.isEmpty()) {
            if (stack.getFirst() == '(' || stack.getFirst() == ')') {
                throw new RuntimeException("Mismatched parentheses.");
            }
            queue.addLast(stack.removeFirst()); // Pop any remaining operators onto the output queue
        }
        return queue;
    }

    /** Gets the priority of an operator.
     * @param operator The operator.
     * @return An int representation of priority.
    */
    private int priority(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        } else if (operator == '^'){
            return 3;
        } else {
            return 0; // not an operators
        }
    }

    /**
     * Takes in an infix equation and prints the result of the equation.
     * @param args The infix equation.
     */
    public static void main(String[] args) {
        Calculator infix = new Calculator();
        PostFix postfix = new PostFix();

        try {
            ArrayDeque<Object> postfixQueue = infix.shuntYard(args);
            System.out.println("Result: " + postfix.calculate(postfixQueue));
        } catch (NullPointerException e) {
            System.err.println("Error: No equation given.");
            System.exit(-1);
        } catch (NoSuchElementException e) {
            System.err.println("Error: Equation could not be completed. Too many operators.");
            System.exit(-1);
        } catch (RuntimeException e) {
            System.err.println("Error: Equation could not be completed. Not enough operators");
            System.exit(-1);
        }
    }
}
