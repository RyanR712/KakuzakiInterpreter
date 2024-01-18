/**
 * Describes a while loop in the AST.
 */

package ParserTools.Nodes.StructureNodes.LoopNodes;

import java.util.ArrayList;

import ParserTools.Nodes.StructureNodes.StatementNodes.BooleanCompareNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.StatementNode;

public class WhileNode extends StatementNode
{
    private BooleanCompareNode conditional;

    private ArrayList<StatementNode> statements;

    private int lineNumber;

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
}