/**
 * Describes a character in Interpreter.
 */

package InterpreterTools.InterpreterDataTypes;

import CrossStageTools.CrossStageNodes.VariableNode;
import ParserTools.Nodes.DataTypeNodes.CharacterNode;

public class CharacterDataType extends InterpreterDataType
{
    private char data;

    private final int lineNumber;

    private final boolean isChangeable;

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

    public CharacterDataType(VariableNode incomingVar, boolean isInitializer)
    {
        data = isInitializer ? ' ' : incomingVar.getValue().charAt(0);

        lineNumber = incomingVar.getLineNumber();

        isChangeable = incomingVar.isChangeable();
    }

    public CharacterDataType(CharacterNode incomingChar)
    {
        data = incomingChar.toString().charAt(0);

        lineNumber = incomingChar.getLineNumber();

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
        data = input.charAt(0);
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
