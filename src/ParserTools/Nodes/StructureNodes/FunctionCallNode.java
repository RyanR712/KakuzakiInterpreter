/**
 * Describes a function call in the AST.
 */

package ParserTools.Nodes.StructureNodes;

import java.util.ArrayList;

import ParserTools.Nodes.StructureNodes.StatementNodes.StatementNode;

public class FunctionCallNode extends StatementNode
{
    private String calledName;

    private ArrayList<ArgumentNode> arguments;

    private int lineNumber;

    /**
     * Creates a FunctionCallNode with the incoming String as its name,
     * the incoming ArgumentNode ArrayList as its arguments and the incoming int as its line number.
     *
     * @param name Incoming String.
     * @param incomingArguments Incoming ArgumentNode ArrayList.
     * @param line Incoming int.
     */
    public FunctionCallNode(String name, ArrayList<ArgumentNode> incomingArguments, int line)
    {
        calledName = name;
        arguments = incomingArguments;

        lineNumber = line;
    }

    @Override
    public String toString()
    {
        String functionCallString = calledName + "(";

        for (int i = 0; i < arguments.size(); i++)
        {
            if (i > 0)
            {
                functionCallString += " ";
            }

            functionCallString += arguments.get(i) + ",";
        }

        return functionCallString + ")";
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
