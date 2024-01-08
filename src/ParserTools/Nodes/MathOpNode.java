/**
 * Describes a math operation in the AST.
 */

package ParserTools.Nodes;

public class MathOpNode extends ASTNode
{
    public enum operationType {ADD, SUB, MULT, DIV, MOD};

    private ASTNode leftOperand, rightOperand;
    private operationType opType;

    public MathOpNode(ASTNode incomingLeftOperand, ASTNode incomingRightOperand, operationType incomingOpType)
    {
        leftOperand = incomingLeftOperand;
        rightOperand = incomingRightOperand;
        opType = incomingOpType;
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
        switch(incomingType)
        {
            case ADD :  return "+";
            case SUB :  return "-";
            case MULT : return "*";
            case DIV  : return "/";
            case MOD  : return "%";
            default   : return "No valid opType detected.";
        }
    }

    @Override
    public String toString()
    {
        return leftOperand + "(" + opTypeToString(opType) + ")" + rightOperand;
    }
}
