/**
 * Describes a while loop in the AST.
 */

package CrossStageTools.Nodes.StructureNodes.StatementBlockNodes;

import java.util.ArrayList;

import CrossStageTools.tokenType;
import CrossStageTools.Nodes.StructureNodes.ExpressionNodes.BooleanCompareNode;
import CrossStageTools.Nodes.StatementNode;

public class WhileNode extends StatementNode
{
    private final BooleanCompareNode conditional;

    private final ArrayList<StatementNode> statements;

    private final int lineNumber;

    /**
     * Creates a WhileNode with the incoming BooleanCompareNode as its conditional,
     * the incoming StatementNode ArrayList as its statement list and the incoming int as the line on which it's stated.
     *
     * @param incomingConditional Incoming BooleanCompareNode.
     * @param incomingStatements Incoming StatementNode ArrayList.
     * @param line Incoming int.
     */
    public WhileNode(BooleanCompareNode incomingConditional, ArrayList<StatementNode> incomingStatements, int line)
    {
        conditional = incomingConditional;
        statements = incomingStatements;

        lineNumber = line;
    }

    /**
     * Returns this WhileNode's conditional.
     *
     * @return This WhileNode's conditional.
     */
    public BooleanCompareNode getConditional()
    {
        return conditional;
    }

    /**
     * Returns this WhileNode's StatementNode list.
     *
     * @return This WhileNode's StatementNode list.
     */
    public ArrayList<StatementNode> getStatements()
    {
        return statements;
    }

    @Override
    public String toString()
    {
        String whileString = "while(" + conditional + ")\nWith statements: ";

        for (int i = 0; i < statements.size(); i++)
        {
            whileString += "\n" + statements.get(i) + ",";
        }

        whileString += "\nEND WHILE";

        return whileString;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.WHILE;
    }
}
