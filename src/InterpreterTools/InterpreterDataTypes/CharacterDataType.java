/**
 * Describes a character in Interpreter.
 */

package InterpreterTools.InterpreterDataTypes;

public class CharacterDataType extends InterpreterDataType
{
    private char data;

    private int lineNumber;

    private boolean isChangeable;

    /**
     * Creates a CharacterDataType with the incoming char as its data,
     * the incoming int as its line number and the incoming boolean as whether this CharacterDataType is changeable.
     *
     * @param incomingData Incoming char.
     * @param line Incoming int.
     * @param changeable Incoming boolean.
     */
    public CharacterDataType(char incomingData, int line, boolean changeable)
    {
        data = incomingData;

        lineNumber = line;

        isChangeable = changeable;
    }

    @Override
    public String toString()
    {
        return data + "";
    }

    @Override
    public void fromString(String input)
    {
        data = input.charAt(0);
    }

    @Override
    public boolean isChangeable()
    {
        return isChangeable;
    }
}
