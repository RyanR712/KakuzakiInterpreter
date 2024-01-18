/**
 * Describes an if statement and zero or more chained if statements in the AST.
 */

package ParserTools.Nodes.StructureNodes;

import java.util.ArrayList;

import ParserTools.Nodes.StructureNodes.StatementNodes.BooleanCompareNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.StatementNode;

public class IfNode extends StatementNode
{
    private BooleanCompareNode conditional;

    private ArrayList<StatementNode> statements;

    private IfNode nextIf;

    private int lineNumber;

    /**
     * Creates an IfNode with the incoming BooleanCompareNode as its conditional,
     * the incoming StatementNode ArrayList as its statement list,
     * the incoming IfNode as its next chained if statement,
     * and the incoming int as the line on which it was stated.
     * Represents an "if" or "elsif" clause.
     *
     * @param incomingConditional Incoming BooleanCompareNode.
     * @param incomingStatements Incoming StatementNode ArrayList.
     * @param next Incoming IfNode.
     * @param line Incoming int.
     */
    public IfNode(BooleanCompareNode incomingConditional, ArrayList<StatementNode> incomingStatements, IfNode next,
                  int line)
    {
        conditional = incomingConditional;

        statements = incomingStatements;

        nextIf = next;

        lineNumber = line;
    }

    /**
     * Creates an IfNode with the incoming StatementNode ArrayList as its statements
     * and the incoming int as the line on which it was stated.
     * Represents an "else" clause.
     *
     * @param incomingStatements Incoming StatementNode ArrayList.
     * @param line Incoming int.
     */
    public IfNode(ArrayList<StatementNode> incomingStatements, int line)
    {
        conditional = null;

        statements = incomingStatements;

        nextIf = null;

        lineNumber = line;
    }

    /**
     * Checks and returns whether this IfNode has a chained IfNode immediately after.
     *
     * @return True if the next IfNode is not null.
     */
    public boolean hasNext()
    {
        return nextIf != null;
    }

    /**
     * Sets this IfNode's next IfNode to the incoming IfNode.
     * Unfortunately untying your tongue is not in the scope of this function.
     *
     * @param next Incoming IfNode.
     */
    public void setNext(IfNode next)
    {
        nextIf = next;
    }

    /**
     * Checks and returns if this IfNode is an else statement.
     * If there is no conditional, then this must be an else statement.
     *
     * @return True if the conditional of this IfNode is null.
     */
    public boolean isElse()
    {
        return conditional == null;
    }

    @Override
    public String toString()
    {
        String ifString = (conditional != null ? "if(" + conditional : "else");

        for (int i = 0; i < statements.size(); i++)
        {
            if (i == 0)
            {
                ifString += "\nWith statements:";
            }

            ifString += "\n" + statements.get(i) + ",";
        }

        return nextIf != null ? ifString + "\nels" + nextIf : ifString;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
