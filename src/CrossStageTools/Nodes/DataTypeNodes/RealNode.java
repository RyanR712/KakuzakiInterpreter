/**
 * Describes a real in the AST.
 */

package CrossStageTools.Nodes.DataTypeNodes;

import CrossStageTools.tokenType;
import Interpreter.InterpreterDataTypes.RealDataType;

public class RealNode extends DataTypeNode
{
    private final float data;
    private float lowerRange, higherRange;

    private final int lineNumber;

    /**
     * Constructs a Real ASTNode with the incoming float as its data and the incoming int as its line number.
     *
     * @param incomingData Incoming float.
     * @param line Incoming int.
     */
    public RealNode(float incomingData, int line)
    {
        data = incomingData;
        lineNumber = line;
    }

    public RealNode(RealDataType rdt)
    {
        data = Float.parseFloat(rdt.toString());
        lineNumber = rdt.getLineNumber();
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

    @Override
    public tokenType getType()
    {
        return tokenType.REAL;
    }
}
