/**
 * Real numbers cannot have more than one decimal point!
 */

package LexerTools;

public class SecondDecimalPointException extends Exception
{
    public SecondDecimalPointException()
    {
        super("Second decimal point found in a number.");
    }
}