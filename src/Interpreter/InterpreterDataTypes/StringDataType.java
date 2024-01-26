/**
 * Describes a String in Interpreter.
 */

package Interpreter.InterpreterDataTypes;

import CrossStageTools.Nodes.VariableNode;
import CrossStageTools.Nodes.DataTypeNodes.StringNode;

public class StringDataType extends InterpreterDataType
{
    private String data;

    private final int lineNumber;

    private final boolean isChangeable;

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

    public StringDataType(VariableNode currentVar, boolean isInitializer)
    {
        data = isInitializer ? "" : currentVar.getValue();

        lineNumber = currentVar.getLineNumber();

        isChangeable = true;
    }

    public StringDataType(StringNode currentString)
    {
        data = currentString.toString();

        lineNumber = currentString.getLineNumber();

        isChangeable = true;
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

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
