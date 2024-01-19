/**
 * Describes a for loop in the AST.
 */

package ParserTools.Nodes.StructureNodes.LoopNodes;

import java.util.ArrayList;

import CrossStageTools.CrossStageNodes.ASTNode;
import CrossStageTools.CrossStageNodes.StatementNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.VariableReferenceNode;

public class ForNode extends StatementNode
{
    private VariableReferenceNode iterator;

    private ASTNode fromNode, toNode;

    private ArrayList<StatementNode> statements;

    private int lineNumber;

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
}
