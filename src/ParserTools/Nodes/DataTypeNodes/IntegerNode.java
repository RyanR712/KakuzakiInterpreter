/**
 * Describes an integer in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class IntegerNode extends DataTypeNode
{
    private int data;

    public IntegerNode(int incomingData)
    {
        data = incomingData;
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
        return null;
    }
}
