/**
 * Describes one Kakuzaki function.
 */

package ParserTools.Nodes.StructureNodes;

import java.util.ArrayList;

import ParserTools.Nodes.ASTNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.StatementNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.VariableNode;

public class FunctionNode extends ASTNode
{
    private ArrayList<StatementNode> statementList;
    private ArrayList<VariableNode> parameterList, variableList;

    private String name;

    private int lineNumber;

    /**
     * Constructs a FunctionNode with the incoming ArrayLists as, respectively, the FunctionNode's statements,
     * the FunctionNode's parameters, the FunctionNode's variables and constants, the incoming String as its name,
     * and the incoming int as the line number the FunctionNode is defined on.
     *
     * @param statements Incoming ArrayList of statements.
     * @param parameters Incoming ArrayList of parameters.
     * @param variables Incoming ArrayList of variables and constants.
     * @param incomingName Incoming String.
     * @param incomingLineNumber Incoming int.
     */
    public FunctionNode(ArrayList<StatementNode> statements, ArrayList<VariableNode> parameters,
                        ArrayList<VariableNode> variables, String incomingName, int incomingLineNumber)
    {
        statementList = statements;
        parameterList = parameters;
        variableList = variables;
        name = incomingName;
        lineNumber = incomingLineNumber;
    }

    /**
     * Returns the name of this FunctionNode.
     * @return Name of this FunctionNode.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        String functionString = "Function " + name + " defined on line " + lineNumber;

        for (int i = 0; i < parameterList.size(); i++)
        {
            if (i == 0)
            {
                functionString += " takes ";
            }
            functionString += parameterList.get(i) + ", ";
        }

        for (int i = 0; i < variableList.size(); i++)
        {
            if (i == 0)
            {
                functionString += "\nWith variables:\n";
            }
            functionString += variableList.get(i) + ",\n";
        }

        functionString += "\nWith no statements."; //TODO: Update to include statement nodes.

        return functionString;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
