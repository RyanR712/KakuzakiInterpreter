/**
 * Describes an integer in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class IntegerNode extends DataTypeNode
{
    private int data;

    private int lineNumber;

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
}
