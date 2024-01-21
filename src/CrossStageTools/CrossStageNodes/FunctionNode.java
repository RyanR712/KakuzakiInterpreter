/**
 * Describes one user defined or builtin Kakuzaki function.
 */

package CrossStageTools.CrossStageNodes;

import CrossStageTools.tokenType;

import java.util.ArrayList;

public class FunctionNode extends ASTNode
{
    private final ArrayList<StatementNode> statementList;
    private final ArrayList<VariableNode> parameterList, variableList;

    private final String name;

    private final int lineNumber;

    private final boolean isVariadic;

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
        isVariadic = false;
    }

    /**
     * Creates a FunctionNode with the incoming String as its name and the incoming boolean as whether it is variadic.
     * Used in loading builtin functions.
     *
     * @param incomingName Incoming String.
     * @param variadic Incoming boolean.
     * @param parameters Incoming parameter list.
     */
    public FunctionNode(String incomingName, boolean variadic, ArrayList<VariableNode> parameters)
    {
        statementList = null;
        parameterList = parameters;
        variableList = null;

        name = incomingName;

        lineNumber = -1;

        isVariadic = variadic;
    }

    /**
     * Creates a FunctionNode with the incoming String as its name and the incoming boolean as whether it is variadic.
     * Used in loading builtin functions.
     *
     * @param incomingName Incoming String.
     * @param variadic Incoming boolean.
     */
    public FunctionNode(String incomingName, boolean variadic)
    {
        statementList = null;
        parameterList = null;
        variableList = null;

        name = incomingName;

        lineNumber = -1;

        isVariadic = variadic;
    }

    /**
     * Returns the name of this FunctionNode.
     * @return Name of this FunctionNode.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns this FunctionNode's parameter list.
     *
     * @return This FunctionNode's parameter list.
     */
    public ArrayList<VariableNode> getParameterList()
    {
        return parameterList;
    }

    /**
     * Returns the number of parameters in this FunctionNode's definition.
     *
     * @return Number of parameters in this FunctionNode's definition.
     */
    public int getNumberOfParameters()
    {
        return parameterList.size();
    }

    /**
     * Returns this FunctionNode's variable list.
     *
     * @return This FunctionNode's variable list.
     */
    public ArrayList<VariableNode> getVariableList()
    {
        return variableList;
    }

    /**
     * Returns this FunctionNode's statement list.
     *
     * @return This FunctionNode's statement list.
     */
    public ArrayList<StatementNode> getStatementList()
    {
        return statementList;
    }

    /**
     * Checks and returns if this FunctionNode is variadic.
     * @return True if this FunctionNode is variadic.
     */
    public boolean isVariadic()
    {
        return isVariadic;
    }

    @Override
    public String toString()
    {
        if (lineNumber == -1)
        {
            return "";
        }

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

        for (int i = 0; i < statementList.size(); i++)
        {
            if (i == 0)
            {
                functionString += "\nWith statements:";
            }

            functionString += "\n" + statementList.get(i) + ",";
        }

        return functionString;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public tokenType getType()
    {
        return tokenType.DEFINE;
    }
}
