/**
 * Describes a string in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class StringNode extends DataTypeNode
{
    private String data;

    public StringNode(String incomingData)
    {
        data = incomingData;
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
        return null;
    }
}
