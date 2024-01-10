/**
 * Describes a boolean in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class BooleanNode extends DataTypeNode
{
    private boolean data;

    private int lineNumber;

    public BooleanNode(boolean incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
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
}
