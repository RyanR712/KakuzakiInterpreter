/**
 * Defines a Token object.
 * A Token is an indivisible part of this language for use in constructing statements.
 */

package LexerTools;

public class Token
{
    private tokenType type;

    private String value;

    private final int lineNumber;

    /**
     * Constructs a Token with type EOL and value "ENDOFLINE".
     */
    public Token(int incomingLine)
    {
        type = tokenType.EOL;

        lineNumber = incomingLine;
    }

    /**
     * Constructs a Token with the incoming tokenType and no value String.
     *
     * @param incomingType Incoming tokenType.
     */
    public Token(tokenType incomingType, int incomingLine)
    {
        type = incomingType;

        lineNumber = incomingLine;
    }

    /**
     * Constructs a Token with the incoming tokenType and the incoming value String.
     *
     * @param incomingType Incoming tokenType.
     * @param incomingValue Incoming value string.
     */
    public Token(tokenType incomingType, String incomingValue, int incomingLine)
    {
        type = incomingType;
        value = incomingValue;

        lineNumber = incomingLine;
    }

    /**
     * Returns this Token's tokenType.
     *
     * @return This Token's tokenType.
     */
    public tokenType getType()
    {
        return type;
    }

    /**
     * Sets this Token's tokenType to the incoming tokenType.
     *
     * @param incomingType Incoming tokenType.
     */
    public void setType(tokenType incomingType)
    {
        type = incomingType;
    }

    /**
     * Returns this Token's value String.
     *
     * @return This Token's value String.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Adds the incoming char to this Token's value String.
     *
     * @param newCharacter Incoming char.
     */
    public void addToValue(char newCharacter)
    {
        value += newCharacter;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    /**
     * Creates and returns this Token in the format {tokenType}({value String}).
     * If the value String is null, then only the tokenType is printed.
     *
     * @return This Token in the above format.
     */
    @Override
    public String toString()
    {
        return value == null ? type + "" : type + "(" + value + ")";
    }
}
