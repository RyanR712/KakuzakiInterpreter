/**
 * Thrown when there are bizarre characters found in Lexer.
 */

package LexerTools;

public class SyntaxErrorException extends Exception
{
    public SyntaxErrorException(char unexpectedCharacter)
    {
        super("Unexpected character " + unexpectedCharacter + " found in lexing.");
    }
}
