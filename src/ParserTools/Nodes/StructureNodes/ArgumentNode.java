/**
 * Describes an argument in a function call in the AST.
 */

package ParserTools.Nodes.StructureNodes;

import CrossStageTools.CrossStageNodes.ASTNode;
import CrossStageTools.tokenType;
import ParserTools.Nodes.StructureNodes.StatementNodes.VariableReferenceNode;

public class ArgumentNode extends ASTNode
{
    private VariableReferenceNode variableParam;

    private ASTNode constantParam;

    private int lineNumber;

    private boolean isConstant;

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
