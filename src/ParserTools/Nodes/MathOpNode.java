/**
 * Describes a math operation in the AST.
 */

package ParserTools.Nodes;

import CrossStageTools.CrossStageNodes.ASTNode;
import CrossStageTools.tokenType;

public class MathOpNode extends ASTNode
{
    public enum operationType {ADD, SUB, MULT, DIV, MOD}

    private ASTNode leftOperand, rightOperand;
    private operationType opType;

    private int lineNumber;

    /**
     * Constructs a MathOpNode with the former incoming ASTNode as its left operand,
     * the incoming tokenType as its operation type, the latter incoming ASTNode as its right operand,
     * and the incoming int as its line number.
     *
     * @param incomingLeftOperand Former incoming ASTNode.
     * @param incomingTokenType Incoming tokenType.
     * @param incomingRightOperand Latter incoming ASTNode.
     * @param line Incoming int.
     */
    public MathOpNode(ASTNode incomingLeftOperand, tokenType incomingTokenType, ASTNode incomingRightOperand,
                      int line)
    {
        leftOperand = incomingLeftOperand;
        opType = tokenTypeToOperationType(incomingTokenType);
        rightOperand = incomingRightOperand;
        lineNumber = line;
    }

    /**
     * Returns the left operand ASTNode of this MathOpNode.
     *
     * @return Left operand ASTNode.
     */
    public ASTNode getLeftOperand()
    {
        return leftOperand;
    }

    /**
     * Returns the right operand ASTNode of this MathOpNode.
     *
     * @return Right operand ASTNode.
     */
    public ASTNode getRightOperand()
    {
        return rightOperand;
    }

    /**
     * Returns the operationType of this MathOpNode.
     *
     * @return operationType of this MathOpNode.
     */
    public operationType getOpType()
    {
        return opType;
    }

    /**
     * Returns the incoming operationType as a readable String.
     *
     * @param incomingType Incoming operationType.
     * @return Readable String.
     */
    public String opTypeToString(operationType incomingType)
    {
        switch (incomingType)
        {
            case ADD  : return "+";
            case SUB  : return "-";
            case MULT : return "*";
            case DIV  : return "/";
            case MOD  : return "%";
            default   : return "No valid opType detected.";
        }
    }

    /**
     * Translates and returns the incoming tokenType to an equivalent operationType.
     *
     * @param tType Incoming tokenType.
     * @return Equivalent operationType.
     */
    private operationType tokenTypeToOperationType(tokenType tType)
    {
        switch (tType)
        {
            case ADD   : return operationType.ADD;
            case MINUS : return operationType.SUB;
            case MULT  : return operationType.MULT;
            case DIV   : return operationType.DIV;
            case MOD   : return operationType.MOD;
            default    : return null;
        }
    }

    @Override
    public String toString()
    {
        return "(" + leftOperand + " " + opTypeToString(opType) + " " + rightOperand + ")";
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.NONE;
    }
}
