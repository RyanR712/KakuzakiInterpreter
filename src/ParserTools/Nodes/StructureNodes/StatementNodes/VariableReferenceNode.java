/**
 * Describes a reference to an already defined VariableNode in the AST.
 */

package ParserTools.Nodes.StructureNodes.StatementNodes;

import CrossStageTools.CrossStageNodes.ASTNode;

public class VariableReferenceNode extends ASTNode
{
    private ASTNode arrayExpression;

    private String referencedName;

    private int lineNumber;

    /**
     * Constructs a VariableReferenceNode with the incoming String as its name,
     * null for its array expression and the incoming integer as its line number.
     *
     * @param incomingName Incoming String.
     * @param line Incoming integer.
     */
    public VariableReferenceNode(String incomingName, int line)
    {
        arrayExpression = null;
        referencedName = incomingName;

        lineNumber = line;
    }

    /**
     * Constructs a VariableReferenceNode with the incoming ASTNode as its array expression,
     * the incoming String as its name and the incoming integer as its line number.
     *
     * @param arrayExp Incoming ASTNode.
     * @param incomingName Incoming String.
     * @param line Incoming integer.
     */
    public VariableReferenceNode(ASTNode arrayExp, String incomingName, int line)
    {
        arrayExpression = arrayExp;
        referencedName = incomingName;

        lineNumber = line;
    }

    @Override
    public String toString()
    {
        return arrayExpression == null ? referencedName : referencedName + "[" + arrayExpression + "]";
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
