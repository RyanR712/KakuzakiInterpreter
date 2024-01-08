/**
 * Describes a boolean in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class CharacterNode extends DataTypeNode
{
    private char data;

    public CharacterNode(char incomingData)
    {
        data = incomingData;
    }

    public char getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return null;
    }
}
