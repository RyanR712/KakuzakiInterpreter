/**
 * Defines a Token object.
 * A Token is an indivisible part of this language for use in constructing statements.
 */

package CrossStageTools;

public class Token
{
    private tokenType type;

    private String value;

    private final int lineNumber;

    /**
     * Constructs a Token with tokenType EOL and the incoming line number.
     *
     * @param incomingLineNumber Incoming line number.
     */
    public Token(int incomingLineNumber)
    {
        type = tokenType.EOL;

        lineNumber = incomingLineNumber;
    }

    /**
     * Constructs a Token with the incoming tokenType, no value String and the incoming line number.
     *
     * @param incomingType Incoming tokenType.
     * @param incomingLineNumber Incoming line number.
     */
    public Token(tokenType incomingType, int incomingLineNumber)
    {
        type = incomingType;

        lineNumber = incomingLineNumber;
    }

    /**
     * Constructs a Token with the incoming tokenType and the incoming value String.
     *
     * @param incomingType Incoming tokenType.
     * @param incomingValue Incoming value string.
     * @param incomingLineNumber Incoming line number.
     */
    public Token(tokenType incomingType, String incomingValue, int incomingLineNumber)
    {
        type = incomingType;
        value = incomingValue;

        lineNumber = incomingLineNumber;
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

    /**
     * Returns the line number of this Token.
     * @return Line number of this Token.
     */
    public int getLineNumber()
    {
        return lineNumber;
    }

    /**
     * Checks and returns if this Token describes a real number.
     *
     * @return True if this Token describes a real number.
     */
    public boolean isRealNumber()
    {
        return type == tokenType.NUMBER && value.contains(".");
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
