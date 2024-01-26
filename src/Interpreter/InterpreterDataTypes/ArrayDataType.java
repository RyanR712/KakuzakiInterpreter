/**
 * Describes an array in Interpreter.
 */

package Interpreter.InterpreterDataTypes;

import CrossStageTools.Nodes.VariableNode;
import CrossStageTools.tokenType;

public class ArrayDataType extends InterpreterDataType
{
    private InterpreterDataType[] data;

    private final int lineNumber;

    private final boolean isChangeable;

    private final tokenType typeOfData;

    public ArrayDataType(VariableNode incomingVar, boolean isInitializer)
    {
        int lower = Integer.parseInt(incomingVar.getLowerRange().toString());
        int higher = Integer.parseInt(incomingVar.getHigherRange().toString());

        for (int i = lower; i < higher; i++)
        {
            data[i] = makeNodeFromTokenType(incomingVar, incomingVar.getType());
        }

        lineNumber = incomingVar.getLineNumber();

        isChangeable = isInitializer;

        typeOfData = incomingVar.getType();
    }

    private InterpreterDataType makeNodeFromTokenType(VariableNode incomingVar, tokenType arrayType)
    {
        switch (arrayType)
        {
            case INTEGER   : return new IntegerDataType(incomingVar, true);
            case REAL      : return new RealDataType(incomingVar, true);
            case STRING    : return new StringDataType(incomingVar, true);
            case CHARACTER : return new CharacterDataType(incomingVar, true);
            case BOOLEAN   : return new BooleanDataType(incomingVar, true);
            default        : return null;
        }
    }

    /**
     * Returns this ArrayDataType's data.
     *
     * @return This ArrayDataType's data.
     */
    public InterpreterDataType[] getData()
    {
        return data;
    }

    /**
     * Returns the data type shared by all InterpreterDataTypes in this ArrayDataType.
     *
     * @return Data type shared by all InterpreterDataTypes in this ArrayDataType.
     */
    public tokenType getTypeOfData()
    {
        return typeOfData;
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

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
