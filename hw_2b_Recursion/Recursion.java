
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Your implementation of the isPalindrome and gcd methods
 *
 * @author Yue Gu
 * @userid ygu65
 * @GTID 903055355
 * @version 1.0
 */
public class Recursion {

    /**
     * Returns a boolean value representing whether the passed in character
     * sequence is a valid palindrome. A palindrome is defined as such:
     * A word, phrase, or sequence that reads the same backward as forward.
     *
     * Palindromes are recursively defined as such:
     * Case 1: An empty string or single character is considered a palindrome
     * Case 2: A string is a palindrome if and only if the first and last
     * characters are the same and the remaining string is also a palindrome
     *
     * For the purposes of this method, two characters are considered
     * 'the same' if they have the same primitive value. You do not need
     * to do any case conversion. Do NOT ignore spaces.
     *
     * This method must be computed recursively! Failure to do so will result
     * in zero credit for this method.
     *
     * @param text The sequence that will be tested
     * @return Whether the passed in word is a palindrome
     * @throws IllegalArgumentException if text is null
     */ 

    public static boolean isPalindrome(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        }
        String[] splitText = text.split("");
        ArrayList<String> textArray = new ArrayList<String>(Arrays.asList(splitText));
        boolean result = recursiveStringCheck(textArray);
        return result;
    }


    public static boolean recursiveStringCheck(ArrayList<String> textArray) {
        // Break the recursive loop if the textArray ever hits size 1 or 0, since at this point it is a palindrome.
        if (textArray.size() <= 1) {
            return true;
        }
        // Determine the left edge and the right edge of the textArray.
        String leftEdge = textArray.get(0);
        String rightEdge = textArray.get(textArray.size()-1);
        // If the edges match, remove the front and back of the ArrayList and then recursively check again.
        if (leftEdge.equals(rightEdge)) {
            textArray.remove(0);
            textArray.remove(textArray.size()-1);
            recursiveStringCheck(textArray);
            return true;
        }
        // If it fails to match, return false.
        return false;
    }



    /**
     * Returns the greatest common divisor of integers x and y. The greatest
     * common divisor can be determined by the recursive function as follows:
     *
     * Case 1: gcd(x, y) = gcd(x-y, y) where x > y
     * Case 2: gcd(x, y) = gcd(x, y-x) where x < y
     * Case 3: gcd(x, y) = x = y where x == y
     * Case 4 (Edge case): gcd(x, y) = {x if y == 0 or y if x == 0}
     *
     * This method must be computed recursively! Failure to do so will result
     * in zero credit for this method.
     *
     * For the purposes of this assignment, do not worry about
     * handling negative numbers. Throw an IllegalArgumentException
     * if either x or y is negative.
     *
     * @param x The first integer
     * @param y The second integer
     * @throws IllegalArgumentException if either x or y is negative
     * @return The greatest common divisor of x and y
     */
    public static int gcd(int x, int y) {
        
    }
}
