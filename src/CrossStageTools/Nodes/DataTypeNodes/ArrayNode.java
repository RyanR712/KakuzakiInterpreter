/**
 * Describes an array in Parser.
 */

package CrossStageTools.Nodes.DataTypeNodes;

import CrossStageTools.tokenType;

public class ArrayNode extends DataTypeNode
{
    private DataTypeNode[] data;

    private final tokenType typeOfData;

    private final int lowestIndex, highestIndex, lineNumber;

    /**
     * Creates an ArrayNode with the incoming tokenType as its type,
     * the first incoming int as its lowest index, the second incoming int as its highest index,
     * and the third incoming int as its line number.
     *
     * @param incomingType Incoming tokenType.
     * @param low First incoming int.
     * @param high Second incoming int.
     * @param line Third incoming int.
     */
    public ArrayNode(tokenType incomingType, int low, int high, int line)
    {
        typeOfData = incomingType;

        lowestIndex = low;

        highestIndex = high;

        lineNumber = line;
    }

    /**
     * Returns the tokenType shared by each element of this array.
     *
     * @return tokenType shared by each element of this array.
     */
    public tokenType getTypeOfData()
    {
        return typeOfData;
    }

    @Override
    public String toString()
    {
        return "Array from " + lowestIndex + " to " + highestIndex + " on line " + lineNumber;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.ARRAY;
    }
}
