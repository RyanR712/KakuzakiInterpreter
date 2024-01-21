/**
 * Describes an integer in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

import CrossStageTools.tokenType;
import InterpreterTools.InterpreterDataTypes.IntegerDataType;

public class IntegerNode extends DataTypeNode
{
    private int data;

    private int lineNumber, lowerRange, higherRange;

    /**
     * Constructs an Integer ASTNode with the former incoming int as its data
     * and the latter incoming int as its line number.
     *
     * @param incomingData Former incoming int.
     * @param line Latter incoming int.
     */
    public IntegerNode(int incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    public IntegerNode(IntegerDataType idt)
    {
        data = Integer.parseInt(idt.toString());
        lineNumber = idt.getLineNumber();
    }

    /**
     * Returns this IntegerNode's data.
     *
     * @return This IntegerNode's data.
     */
    public int getData()
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
        return tokenType.INTEGER;
    }
}
