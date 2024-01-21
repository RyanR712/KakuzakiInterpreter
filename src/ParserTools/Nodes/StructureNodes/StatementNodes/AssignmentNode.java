/**
 * Describes an assignment in the AST.
 */

package ParserTools.Nodes.StructureNodes.StatementNodes;

import CrossStageTools.CrossStageNodes.ASTNode;
import CrossStageTools.CrossStageNodes.StatementNode;
import CrossStageTools.tokenType;

public class AssignmentNode extends StatementNode
{
    private VariableReferenceNode referencedTarget;
    private ASTNode referencedValue;

    private int lineNumber;

    /**
     * Constructs an Assignment node with the incoming VariableReferenceNode as its target,
     * the incoming ASTNode as its value and the incoming integer as its line number.
     *
     * @param target Incoming VariableReferenceNode.
     * @param value Incoming ASTNode.
     * @param line Incoming integer.
     */
    public AssignmentNode(VariableReferenceNode target, ASTNode value, int line)
    {
        referencedTarget = target;
        referencedValue = value;

        lineNumber = line;
    }

    /**
     * Returns this AssignmentNode's target.
     *
     * @return This AssignmentNode's target.
     */
    public VariableReferenceNode getTarget()
    {
        return referencedTarget;
    }

    /**
     * Returns this AssignmentNode's value.
     *
     * @return This AssignmentNode's value.
     */
    public ASTNode getValue()
    {
        return referencedValue;
    }

    @Override
    public String toString()
    {
        return referencedTarget + " = " + referencedValue;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.ASSIGN;
    }
}
