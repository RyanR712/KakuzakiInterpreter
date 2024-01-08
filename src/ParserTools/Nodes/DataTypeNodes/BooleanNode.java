/**
 * Describes a boolean in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class BooleanNode extends DataTypeNode
{
    private boolean data;

    public BooleanNode(boolean incomingData)
    {
        data = incomingData;
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
        return null;
    }
}
