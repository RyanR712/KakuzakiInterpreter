/**
 * Describes a boolean in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class CharacterNode extends DataTypeNode
{
    private char data;

    private int lineNumber;

    public CharacterNode(char incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    public char getData()
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
