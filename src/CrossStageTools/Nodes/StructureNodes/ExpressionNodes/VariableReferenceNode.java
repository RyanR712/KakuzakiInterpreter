/**
 * Describes a reference to an already defined VariableNode in the AST.
 */

package CrossStageTools.Nodes.StructureNodes.ExpressionNodes;

import CrossStageTools.Nodes.ASTNode;
import CrossStageTools.tokenType;

public class VariableReferenceNode extends ASTNode
{
    private final ASTNode arrayExpression;

    private final String referencedName;

    private final int lineNumber;

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

    /**
     * Returns this VariableReferenceNode's name.
     *
     * @return This VariableReferenceNode's name.
     */
    public String getName()
    {
        return referencedName;
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

    @Override
    public tokenType getType()
    {
        return tokenType.NONE;
    }
}
