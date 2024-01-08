/**
 * Thrown when there are bizarre characters found in Lexer.
 */

package Exceptions;

public class SyntaxErrorException extends Exception
{
    public SyntaxErrorException(String message)
    {
        super(message);
    }
}
