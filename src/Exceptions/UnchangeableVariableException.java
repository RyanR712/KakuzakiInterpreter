/**
 * Thrown when a constant is attempted to be changed.
 */

package Exceptions;

public class UnchangeableVariableException extends SyntaxErrorException
{
    public UnchangeableVariableException(String name, int lineNumber)
    {
        super("Constant " + name + " was attempted to be changed on line " + lineNumber);
    }
}
