/**
 * Describes a declared variable in the AST.
 */

package ParserTools.Nodes.StructureNodes.StatementNodes;

import CrossStageTools.tokenType;
import ParserTools.Nodes.ASTNode;

public class VariableNode extends ASTNode
{
    private String name, value;

    private tokenType type;

    private int lineNumber;

    private boolean isChangeable;

    //TODO: try to receive the type and line number from a Token object?
    /**
     * Constructs a VariableNode with the incoming String as its name, the incoming tokenType as its type,
     * the incoming int as its line number.
     * The incoming boolean determines if it is a variable or constant.
     *
     * @param incomingName Incoming String.
     * @param incomingType Incoming tokenType.
     * @param incomingLineNumber Incoming int.
     * @param changeable Incoming boolean.
     */
    public VariableNode(String incomingName, tokenType incomingType, int incomingLineNumber, boolean changeable)
    {
        name = incomingName;
        type = incomingType;
        lineNumber = incomingLineNumber;
        isChangeable = changeable;
    }

    //TODO: try to receive the type and value and line number from a Token object?
    /**
     * Constructs a VariableNode with the former incoming String as its name, the incoming tokenType as its type,
     * the latter incoming String as its value, the incoming int as its line number.
     * The incoming boolean determines if it is a variable or constant.
     *
     * @param incomingName Former incoming String.
     * @param incomingType Incoming tokenType.
     * @param incomingValue Latter incoming String.
     * @param incomingLineNumber Incoming int.
     * @param changeable Incoming boolean.
     */
    public VariableNode(String incomingName, tokenType incomingType, String incomingValue, int incomingLineNumber,
                        boolean changeable)
    {
        name = incomingName;
        type = incomingType;
        value = incomingValue;
        lineNumber = incomingLineNumber;
        isChangeable = changeable;
    }

    /**
     * Sets this VariableNode's type to the incoming tokenType.
     *
     * @param incomingType Incoming tokenType.
     */
    public void setType(tokenType incomingType)
    {
        type = incomingType;
    }

    /**
     * Sets this VariableNode's value to the incoming String.
     *
     * @param incomingValue Incoming String.
     */
    public void setValue(String incomingValue)
    {
        value = incomingValue;
    }

    @Override
    public String toString()
    {
        String variableString = "";

        variableString += isChangeable ? "Variable " : "Constant ";

        variableString += name + " of type " + type + " on line " + lineNumber;

        variableString += value == null ? " with no value assigned" : " with value " + value;

        return variableString;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
