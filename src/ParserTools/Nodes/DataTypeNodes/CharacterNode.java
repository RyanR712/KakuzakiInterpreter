/**
 * Describes a boolean in the AST.
 */

package ParserTools.Nodes.DataTypeNodes;

public class CharacterNode extends DataTypeNode
{
    private char data;

    private int lineNumber;

    /**
     * Constructs a Character ASTNode with the incoming char as its data with the incoming int as its line number.
     *
     * @param incomingData Incoming char.
     * @param line Incoming int.
     */
    public CharacterNode(char incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    /**
     * Returns this CharacterNode's data.
     *
     * @return This CharacterNode's data.
     */
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
