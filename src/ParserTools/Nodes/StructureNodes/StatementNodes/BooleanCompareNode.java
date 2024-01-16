/**
 * Describes a boolean operation in the AST.
 */

package ParserTools.Nodes.StructureNodes.StatementNodes;

import CrossStageTools.tokenType;
import ParserTools.Nodes.ASTNode;

public class BooleanCompareNode extends ASTNode
{
    public enum comparisonType {GTHAN, LTHAN, GETO, LETO, EQUAL, NEQUAL, NOT, AND, OR}

    private ASTNode leftComparand, rightComparand;
    private comparisonType compType;

    private int lineNumber;

    /**
     * Constructs a BooleanCompareNode with the former incoming ASTNode as its left comparand,
     * the incoming comparisonType as its comparison operation, the latter incoming ASTNode as its right comparand,
     * and the incoming integer as its line number.
     *
     * @param leftComp Former incoming ASTNode.
     * @param comp Incoming comparisonType.
     * @param rightComp Latter incoming ASTNode.
     * @param line Incoming integer.
     */
    public BooleanCompareNode(ASTNode leftComp, tokenType comp, ASTNode rightComp, int line)
    {
        leftComparand = leftComp;
        compType = tokenTypeToComparisonType(comp);
        rightComparand = rightComp;

        lineNumber = line;
    }

    /**
     * Translates the incoming tokenType to a comparisonType and returns that comparisonType.
     *
     * @param tType Incoming tokenType.
     * @return Resulting comparisonType.
     */
    private comparisonType tokenTypeToComparisonType(tokenType tType)
    {
        switch (tType)
        {
            case GTHAN  : return comparisonType.GTHAN;
            case LTHAN  : return comparisonType.LTHAN;
            case GETO   : return comparisonType.GETO;
            case LETO   : return comparisonType.LETO;
            case EQUAL  : return comparisonType.EQUAL;
            case NEQUAL : return comparisonType.NEQUAL;
            case NOT    : return comparisonType.NOT;
            case AND    : return comparisonType.AND;
            case OR     : return comparisonType.OR;
            default     : return null;
        }
    }

    @Override
    public String toString()
    {
        return null;
    }

    @Override
    public int getLineNumber()
    {
        return 0;
    }
}
