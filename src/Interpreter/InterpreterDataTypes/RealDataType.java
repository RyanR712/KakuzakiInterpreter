/**
 * Describes a real in Interpreter.
 */

package Interpreter.InterpreterDataTypes;

import CrossStageTools.Nodes.VariableNode;
import CrossStageTools.Nodes.DataTypeNodes.RealNode;

public class RealDataType extends InterpreterDataType
{
    private float data;

    private final int lineNumber;

    private final boolean isChangeable;

    /**
     * Creates a RealDataType with the incoming float as its data,
     * the incoming int as its line number and the incoming boolean as whether this RealDataType is changeable.
     *
     * @param incomingData Incoming float.
     * @param line Incoming line number.
     * @param changeable Incoming boolean.
     */
    public RealDataType(float incomingData, int line, boolean changeable)
    {
        data = incomingData;

        lineNumber = line;

        isChangeable = changeable;
    }

    public RealDataType(VariableNode incomingVar, boolean isInitializer)
    {
        data = isInitializer ? 0.0F : Float.parseFloat(incomingVar.getValue());

        lineNumber = incomingVar.getLineNumber();

        isChangeable = incomingVar.isChangeable();
    }

    public RealDataType(RealNode incomingReal)
    {
        data = Float.parseFloat(incomingReal.toString());

        lineNumber = incomingReal.getLineNumber();

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
        data = Float.parseFloat(input);
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
