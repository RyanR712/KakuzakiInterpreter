/**
 * Describes a function call in the AST.
 */

package ParserTools.Nodes.StructureNodes;

import java.util.ArrayList;

import CrossStageTools.CrossStageNodes.StatementNode;
import CrossStageTools.tokenType;

public class FunctionCallNode extends StatementNode
{
    private final String calledName;

    private final ArrayList<ArgumentNode> arguments;

    private final int lineNumber;

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

    /**
     * Returns this FunctionCallNode's called function name.
     *
     * @return This FunctionCallNode's called function name.
     */
    public String getName()
    {
        return calledName;
    }

    /**
     * Returns the number of arguments this function call was made with.
     *
     * @return Number of arguments this function call was made with.
     */
    public int getNumberOfArguments()
    {
        return arguments.size();
    }

    /**
     * Returns this FunctionCallNode's argument list.
     *
     * @return This FunctionCallNode's argument list.
     */
    public ArrayList<ArgumentNode> getArguments()
    {
        return arguments;
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

    @Override
    public tokenType getType()
    {
        return tokenType.NONE;
    }
}
