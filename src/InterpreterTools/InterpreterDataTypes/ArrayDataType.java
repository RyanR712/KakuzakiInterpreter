/**
 * Describes an array in Interpreter.
 */

package InterpreterTools.InterpreterDataTypes;

public class ArrayDataType extends InterpreterDataType
{
    private InterpreterDataType[] data;

    private int lineNumber;

    private boolean isChangeable;

    /**
     * Creates an ArrayDataType with the incoming InterpreterDataType array as its data,
     * the incoming int as its line number and the incoming boolean as whether this ArrayDataType is changeable.
     *
     * @param incomingData Incoming InterpreterDataType array.
     * @param line Incoming int.
     * @param changeable Incoming boolean.
     */
    public ArrayDataType(InterpreterDataType[] incomingData, int line, boolean changeable)
    {
        data = incomingData;

        lineNumber = line;

        isChangeable = changeable;
    }

    /**
     * Returns this ArrayDataType's data.
     * @return This ArrayDataType's data.
     */
    public InterpreterDataType[] getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        String arrayString = "";

        for (int i = 0; i < data.length; i++)
        {
            arrayString += data[i];
            if (i != data.length - 1)
            {
                arrayString += " ";
            }
        }

        return arrayString;
    }

    @Override
    public void fromString(String input)
    {
        for (int i = 0; i < data.length; i++)
        {
            data[i] = null; //TODO: Figure out something for parsing specific IDTs
        }
    }

    @Override
    public boolean isChangeable()
    {
        return isChangeable;
    }
}
