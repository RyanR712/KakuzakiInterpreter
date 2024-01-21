/**
 * Describes a boolean in Interpreter.
 */

package InterpreterTools.InterpreterDataTypes;

import CrossStageTools.CrossStageNodes.VariableNode;
import ParserTools.Nodes.DataTypeNodes.BooleanNode;

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

    public BooleanDataType(VariableNode incomingVar, boolean isInitializer)
    {
        data = isInitializer ? false : Boolean.parseBoolean(incomingVar.getValue());

        lineNumber = incomingVar.getLineNumber();

        isChangeable = incomingVar.isChangeable();
    }

    public BooleanDataType(BooleanNode incomingBoolean)
    {
        data = Boolean.parseBoolean(incomingBoolean.toString());

        lineNumber = incomingBoolean.getLineNumber();

        isChangeable = true;
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

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
