/**
 * Describes a string in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class StringNode extends DataTypeNode
{
    private String data;

    private int lineNumber;

    public StringNode(String incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
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
}
