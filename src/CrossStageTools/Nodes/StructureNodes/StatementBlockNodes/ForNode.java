/**
 * Describes a for loop in the AST.
 */

package CrossStageTools.Nodes.StructureNodes.StatementBlockNodes;

import java.util.ArrayList;

import CrossStageTools.Nodes.ASTNode;
import CrossStageTools.Nodes.StatementNode;
import CrossStageTools.Nodes.StructureNodes.ExpressionNodes.VariableReferenceNode;
import CrossStageTools.tokenType;

public class ForNode extends StatementNode
{
    private final VariableReferenceNode iterator;

    private final ASTNode fromNode, toNode;

    private final ArrayList<StatementNode> statements;

    private final int lineNumber;

    /**
     * Creates a ForNode with the incoming VariableReferenceNode as its iterator,
     * the incoming former ASTNode as its from condition,
     * the incoming latter ASTNode as its to condition, the incoming StatementNode ArrayList as its statement list,
     * and the incoming int as the line on which it's stated.
     *
     * @param it Incoming VariableReferenceNode.
     * @param infroming Incoming former ASTNode.
     * @param incomingTo Incoming latter ASTNode.
     * @param incomingStatements Incoming StatementNode ArrayList.
     * @param line Incoming int.
     */
    public ForNode(VariableReferenceNode it,
                   ASTNode infroming, ASTNode incomingTo, ArrayList<StatementNode> incomingStatements, int line)
    {
        iterator = it;

        fromNode = infroming;
        toNode = incomingTo;

        statements = incomingStatements;

        lineNumber = line;
    }

    /**
     * Returns this ForNode's iterator.
     *
     * @return This ForNode's iterator.
     */
    public VariableReferenceNode getIterator()
    {
        return iterator;
    }

    /**
     * Returns this ForNode's from condition.
     *
     * @return This ForNode's from condition.
     */
    public ASTNode getFromNode()
    {
        return fromNode;
    }

    /**
     * Returns this ForNode's to condition.
     *
     * @return This ForNode's to condition.
     */
    public ASTNode getToNode()
    {
        return toNode;
    }

    /**
     * Returns this ForNode's StatementNode list.
     *
     * @return This ForNode's StatementNode list.
     */
    public ArrayList<StatementNode> getStatements()
    {
        return statements;
    }

    @Override
    public String toString()
    {
        String forString = "for(" + iterator + " from " + fromNode + " to " + toNode + ")\nWith statements:";

        for (int i = 0; i < statements.size(); i++)
        {
            forString += "\n" + statements.get(i) + ",";
        }

        forString += "\nEND FOR";

        return forString;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.FOR;
    }
}
