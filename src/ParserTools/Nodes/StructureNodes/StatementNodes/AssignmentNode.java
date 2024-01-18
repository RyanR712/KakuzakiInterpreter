/**
 * Describes an assignment in the AST.
 */

package ParserTools.Nodes.StructureNodes.StatementNodes;

import ParserTools.Nodes.ASTNode;

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
     * @param line Incoming line number.
     */
    public AssignmentNode(VariableReferenceNode target, ASTNode value, int line)
    {
        referencedTarget = target;
        referencedValue = value;

        lineNumber = line;
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
}