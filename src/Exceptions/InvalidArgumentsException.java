/**
 * Thrown when an argument to a function is made that has the improper number of arguments
 * or an invalid type of argument.
 */

package Exceptions;

public class InvalidArgumentsException extends SyntaxErrorException
{
    public InvalidArgumentsException(String name, int lineNumber)
    {
        super("Bad function call for " + name + " on line " + lineNumber + ".");
    }
}
