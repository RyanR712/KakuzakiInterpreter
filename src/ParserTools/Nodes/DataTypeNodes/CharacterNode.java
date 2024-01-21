/**
 * Describes a boolean in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

import CrossStageTools.tokenType;
import InterpreterTools.InterpreterDataTypes.CharacterDataType;

public class CharacterNode extends DataTypeNode
{
    private char data;

    private int lineNumber;

    /**
     * Constructs a Character ASTNode with the incoming char as its data with the incoming int as its line number.
     *
     * @param incomingData Incoming char.
     * @param line Incoming int.
     */
    public CharacterNode(char incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    public CharacterNode(CharacterDataType cdt)
    {
        data = cdt.toString().charAt(0);
        lineNumber = cdt.getLineNumber();
    }

    /**
     * Returns this CharacterNode's data.
     *
     * @return This CharacterNode's data.
     */
    public char getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return data + "";
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.CHARACTER;
    }
}
