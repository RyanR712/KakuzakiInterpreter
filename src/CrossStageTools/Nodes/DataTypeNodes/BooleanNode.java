/**
 * Describes a boolean in the AST.
 */

package CrossStageTools.Nodes.DataTypeNodes;

import CrossStageTools.tokenType;
import Interpreter.InterpreterDataTypes.BooleanDataType;

public class BooleanNode extends DataTypeNode
{
    private final boolean data;

    private final int lineNumber;

    /**
     * Constructs a Boolean ASTNode with the incoming boolean as its data with the incoming int as its line number.
     *
     * @param incomingData Incoming char.
     * @param line Incoming int.
     */
    public BooleanNode(boolean incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    public BooleanNode(tokenType incomingData, int line)
    {
        data = incomingData == tokenType.TRUE;
        lineNumber = line;
    }


    public BooleanNode(BooleanDataType bdt)
    {
        data = Boolean.parseBoolean(bdt.toString());
        lineNumber = bdt.getLineNumber();
    }

    /**
     * Returns this BooleanNode's data.
     *
     * @return This BooleanNode's data.
     */
    public boolean getData()
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
        return tokenType.BOOLEAN;
    }
}
