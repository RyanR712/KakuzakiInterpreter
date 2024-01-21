/**
 * Describes a string in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

import CrossStageTools.tokenType;
import InterpreterTools.InterpreterDataTypes.StringDataType;

public class StringNode extends DataTypeNode
{
    private String data;

    private int lowerRange, higherRange, lineNumber;

    /**
     * Constructs a String ASTNode with the incoming String as its data with the incoming int as its line number.
     *
     * @param incomingData Incoming String.
     * @param line Incoming int.
     */
    public StringNode(String incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    public StringNode(StringDataType sdt)
    {
        data = sdt.toString();
        lineNumber = sdt.getLineNumber();
    }

    /**
     * Returns this StringNode's data.
     *
     * @return This StringNode's data.
     */
    public String getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return data;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.STRING;
    }
}
