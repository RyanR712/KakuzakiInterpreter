/**
 * Describes a real in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class RealNode extends DataTypeNode
{
    private float data;

    public RealNode(float incomingData)
    {
        data = incomingData;
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
        return null;
    }
}
