/**
 * Describes a String in Interpreter.
 */

package InterpreterTools.InterpreterDataTypes;

public class StringDataType extends InterpreterDataType
{
    private String data;

    private int lineNumber;

    private boolean isChangeable;

    /**
     * Creates a StringDataType with the incoming String as its data,
     * the incoming int as its line number and the incoming boolean as whether this StringDataType is changeable.
     *
     * @param incomingData Incoming String.
     * @param line Incoming line.
     */
    public StringDataType(String incomingData, int line, boolean changeable)
    {
        data = incomingData;

        lineNumber = line;

        isChangeable = changeable;
    }

    @Override
    public String toString()
    {
        return data;
    }

    @Override
    public void fromString(String input)
    {
        data = input;
    }

    @Override
    public boolean isChangeable()
    {
        return isChangeable;
    }
}
