/**
 * Describes an integer in Interpreter.
 */

package InterpreterTools.InterpreterDataTypes;

import CrossStageTools.CrossStageNodes.VariableNode;
import ParserTools.Nodes.DataTypeNodes.IntegerNode;

public class IntegerDataType extends InterpreterDataType
{
    private int data, lineNumber;

    private boolean isChangeable;

    /**
     * Creates an IntegerDataType with the former incoming int as its data,
     * the latter incoming int as its line number
     * and the incoming boolean as whether this IntegerDataType is changeable.
     *
     * @param incomingData Former incoming int.
     * @param line Latter incoming int.
     * @param changeable Incoming boolean.
     */
    public IntegerDataType(int incomingData, int line, boolean changeable)
    {
        data = incomingData;

        lineNumber = line;

        isChangeable = changeable;
    }

    public IntegerDataType(VariableNode incomingVar)
    {
        data = Integer.parseInt(incomingVar.toString());

        lineNumber = incomingVar.getLineNumber();

        isChangeable = incomingVar.isChangeable();
    }

    @Override
    public String toString()
    {
        return data + "";
    }

    @Override
    public void fromString(String input)
    {
        data = Integer.parseInt(input);
    }

    @Override
    public boolean isChangeable()
    {
        return isChangeable;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
