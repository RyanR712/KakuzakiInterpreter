/**
 * Thrown when a nonexistant variable is referenced in interpreting.
 */

package Exceptions;

public class NonexistantVariableException extends SyntaxErrorException
{
    public NonexistantVariableException(String name, int lineNumber)
    {
        super("Nonexistant variable " + name + " referenced on line " + lineNumber);
    }
}
