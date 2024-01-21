/**
 * Describes a declared variable in the AST.
 */

package CrossStageTools.CrossStageNodes;

import CrossStageTools.CrossStageNodes.ASTNode;
import CrossStageTools.tokenType;

public class VariableNode extends ASTNode
{
    private ASTNode lowerRange, higherRange;

    private String name, value;

    private tokenType type;

    private int lineNumber;

    private boolean isChangeable, isRanged;

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
        isRanged = incomingType == tokenType.INTEGER || incomingType == tokenType.STRING ||
                   incomingType == tokenType.REAL;
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
        isRanged = incomingType == tokenType.INTEGER || incomingType == tokenType.STRING ||
                   incomingType == tokenType.REAL;
    }

    /**
     * Returns this VariableNode's name.
     *
     * @return This VariableNode's name.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public tokenType getType()
    {
        return type;
    }

    /**
     * Returns this VariableNode's data.
     *
     * @return This VariableNode's data.
     */
    public String getValue()
    {
        return value;
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

    public ASTNode getHigherRange()
    {
        return higherRange;
    }

    public ASTNode getLowerRange()
    {
        return lowerRange;
    }

    public void setLowerRange(ASTNode lower)
    {
        lowerRange = lower;
    }

    public void setHigherRange(ASTNode higher)
    {
        higherRange = higher;
    }

    /**
     * Returns if this VariableNode is changeable, i.e. if it is variable.
     *
     * @return True if this VariableNode is variable. False if it is constant.
     */
    public boolean isChangeable()
    {
        return isChangeable;
    }

    /**
     * Checks and returns if this VariableNode is ranged.
     *
     * @return True if this VariableNode is ranged.
     */
    public boolean isRanged()
    {
        return isRanged;
    }

    /**
     * Sets isRanged to true.
     */
    public void setRangedAsTrue()
    {
        isRanged = true;
    }

    @Override
    public String toString()
    {
        String variableString = "";

        variableString += isChangeable ? "Variable " : "Constant ";

        variableString += name + " of type " + type + " on line " + lineNumber;

        variableString += value == null ? " with no value assigned" : " with value " + value;

        variableString += isRanged ? " from " + lowerRange + " to " + higherRange : " with no range ";

        return variableString;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }
}
