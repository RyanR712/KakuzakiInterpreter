package LexerTools;

public class Token
{
    private tokenType type;

    private String value;

    /**
     * Generates a Token with type EOL and value "ENDOFLINE".
     */
    public Token()
    {
        type = tokenType.EOL;
        value = "ENDOFLINE";
    }

    public Token(tokenType incomingType, String incomingValue)
    {
        type = incomingType;
        value = incomingValue;
    }

    public tokenType getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }
    public void addToValue(char newCharacter)
    {
        value += newCharacter;
    }

    public String toString()
    {
        return type + "(" + value + ") ";
    }
}
