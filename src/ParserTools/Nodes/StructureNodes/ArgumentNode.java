/**
 * Describes an argument in a function call in the AST.
 */

package ParserTools.Nodes.StructureNodes;

import CrossStageTools.CrossStageNodes.ASTNode;
import CrossStageTools.tokenType;
import ParserTools.Nodes.StructureNodes.StatementNodes.VariableReferenceNode;

public class ArgumentNode extends ASTNode
{
    private final VariableReferenceNode variableParam;

    private final ASTNode constantParam;

    private final int lineNumber;

    private final boolean isConstant;

    /**
     * Creates an ArgumentNode with the incoming VariableReferenceNode as its variable value
     * and the incoming int as its line number.
     *
     * @param incomingVariable Incoming VariableReferenceNode.
     * @param line Incoming int.
     */
    public ArgumentNode(VariableReferenceNode incomingVariable, int line)
    {
        variableParam = incomingVariable;
        constantParam = null;

        lineNumber = line;
        isConstant = false;
    }

    /**
     * Creates an ArgumentNode with the incoming ASTNode as its constant value and the incoming int as its line number.
     *
     * @param incomingConstant Incoming ASTNode.
     * @param line Incoming int.
     */
    public ArgumentNode(ASTNode incomingConstant, int line)
    {
        variableParam = null;
        constantParam = incomingConstant;

        lineNumber = line;
        isConstant = true;
    }

    /**
     * Returns the VariableReferenceNode from this ArgumentNode.
     *
     * @return VariableReferenceNode from this ArgumentNode.
     */
    public VariableReferenceNode getVariableReference()
    {
        return variableParam;
    }

    /**
     * Returns the constant ASTNode from this ArgumentNode.
     *
     * @return Constant ASTNode from this ArgumentNode.
     */
    public ASTNode getConstant()
    {
        return constantParam;
    }

    /**
     * Returns whether this ArgumentNode is constant.
     *
     * @return True if this ArgumentNode is constant.
     */
    public boolean isConstant()
    {
        return isConstant;
    }

    @Override
    public String toString()
    {
        return isConstant ? "const " + constantParam : "var " + variableParam;
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
