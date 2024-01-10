/**
 * Describes a real in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class RealNode extends DataTypeNode
{
    private float data;

    private int lineNumber;

    public RealNode(float incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    /**
     * Returns this RealNode's data.
     *
     * @return This RealNode's data.
     */
    public float getData()
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
