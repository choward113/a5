import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.lang.RuntimeException;
import java.lang.Math;

/**
 * Calculator for equations given in postfix format.
 * @author Camille Howard
 * @version October 23, 2023. CSC 210.
 */
public class PostFix {
    /**
     * Takes in a queue format of an equation and calculates the answer.
     * @param fullEquationQueue The queue representation of an equation.
     * @return Result of the equation as a Double.
     * @throws NullPointerException if no equation is given.
     * @throws NoSuchElementException if there is an operator but not enough numbers to operate on.
     * @throws RuntimeException if there are multiple numbers left on the queue at the end of calculate. 
     */
    public Double calculate(ArrayDeque<Object> fullEquationQueue) {
        ArrayDeque<Object> numStack = new ArrayDeque<>(fullEquationQueue.size());
        ArrayDeque<Character> charStack = new ArrayDeque<>(fullEquationQueue.size());
        ArrayDeque<String> strStack = new ArrayDeque<>(fullEquationQueue.size()); // for later
        if (!fullEquationQueue.isEmpty()) {
            for (Object obj : fullEquationQueue) {
                if (obj instanceof Double) {
                    numStack.addFirst((Double) obj);
                }
                if (obj instanceof String) {
                    strStack.addFirst(obj.toString()); //sin, cos, pi
                }   
                if (obj instanceof Character) {
                    if (obj.toString().equals("^")) { // Exponent
                        Double d1 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        Double d2 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        numStack.addFirst(Math.pow(d2, d1));
                    }
                    if (obj.toString().equals("*")) { // Multiplication
                        Double d1 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        Double d2 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        numStack.addFirst(d1 * d2);
                    }
                    if (obj.toString().equals("/")) {  // Division
                        Double d1 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        Double d2 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        numStack.addFirst(d2 / d1);
                    }
                    if (obj.toString().equals("+")) { // Addition
                        Double d1 = (Double)numStack.getFirst();
                        numStack.removeFirst();
                        Double d2 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        numStack.addFirst(d1 + d2);
                    }
                    if (obj.toString().equals("-")) { // Subtraction
                        Double d1 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        Double d2 = (Double) numStack.getFirst();
                        numStack.removeFirst();
                        numStack.addFirst(d2 - d1);
                    }
                    charStack.addFirst((Character) obj);
                }  
            }
        }
        if (numStack.size() > 1) {
            throw new RuntimeException();
        }
        return (Double) numStack.getFirst();
    }

    /**
     * Takes in a postfix equation and prints the result of the equation.
     * @param args
     */    
    public static void main(String[] args) {
        ArrayDeque<Object> fullEquationQueue = Tokenizer.main(args);
        try {
            PostFix postfix = new PostFix();
            System.out.println("Result: " +postfix.calculate(fullEquationQueue));    
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
