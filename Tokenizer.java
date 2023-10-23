import java.util.ArrayDeque;
import java.util.Scanner;
import java.io.StringReader;

/** 
 *  Shows use of StreamTokenizer. Modified by Camille Howard.
 *  @author Nick Howe
 *  @version 26 September 2013
 */
public class Tokenizer {
  /** Pattern that matches on words */
  public static final String WORD = "[a-zA-Z]*\\b";

  /** Pattern that matches on arithmetic symbols */
  public static final String SYMBOL = "[^\\w]";

  /** ArrayDeque that is used as a queue to store an equation*/
  private static ArrayDeque<Object> equationQueue;

  /** 
   *  Converts the input string to a queue of tokens 
   *  @param input  the string to convert
   *  @return  the queue of tokens
   */
  public static void readTokens(String input) {
    equationQueue = new ArrayDeque<>(input.length());
    Scanner scanner = new Scanner(new StringReader(input));
    // Below is a complicated regular expression that will split the
    // input string at various boundaries.
    scanner.useDelimiter
      ("(\\s+"                            // whitespace
      +"|(?<=[a-zA-Z])(?=[^a-zA-Z])"      // word->non-word
      +"|(?<=[^a-zA-Z])(?=[a-zA-Z])"      // non-word->word
      +"|(?<=[^0-9\\056])(?=[0-9\\056])"  // non-number->number
      +"|(?<=[0-9\\056])(?=[^0-9\\056])"  // number->non-number
      +"|(?<=[^\\w])(?=[^\\w]))");        // symbol->symbol
    
    while (scanner.hasNext()) {
      if (scanner.hasNextDouble()) {
        equationQueue.addLast(scanner.nextDouble());
      } else if (scanner.hasNext(WORD)) {
        equationQueue.addLast(scanner.next(WORD));
      } else if (scanner.hasNext(SYMBOL)) {
        equationQueue.addLast(scanner.next(SYMBOL).charAt(0));
      } else {
        //System.out.println(scanner.next());
      }
    }
  }

  /** Converts a string array to an array deque.
   * @param args User input string
   * @return A converted array deque */
  public static ArrayDeque<Object> main(String[] args) {
    if (args.length==0) {
      System.err.println("Usage:  java Tokenizer <expr>");
    } else {
      readTokens(args[0]);
    }
     return equationQueue;
  }
}

