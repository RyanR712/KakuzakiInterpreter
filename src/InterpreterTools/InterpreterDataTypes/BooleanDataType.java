/**
 * Describes a boolean in Interpreter.
 */

package InterpreterTools.InterpreterDataTypes;

public class BooleanDataType extends InterpreterDataType
{
    private boolean data, isChangeable;

    private int lineNumber;

    /**
     * Creates a BooleanDataType with the former incoming boolean as its data,
     * the incoming int as its line number
     * and the latter incoming boolean as whether this BooleanDataType is changeable.
     *
     * @param incomingBoolean Former incoming boolean.
     * @param line Incoming int.
     * @param changeable Latter incoming boolean.
     */
    public BooleanDataType(boolean incomingBoolean, int line, boolean changeable)
    {
        data = incomingBoolean;

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
        data = Boolean.parseBoolean(input);
    }

    @Override
    public boolean isChangeable()
    {
        return isChangeable;
    }
}
