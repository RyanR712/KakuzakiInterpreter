package ParserTools.Nodes.StructureNodes.StatementNodes;

import CrossStageTools.tokenType;
import ParserTools.Nodes.ASTNode;

public class VariableNode extends ASTNode
{
    private String name, value;

    private tokenType type;

    private int lineNumber;

    private boolean isChangeable;

    public VariableNode(String incomingName, tokenType incomingType, int incomingLineNumber, boolean changeable)
    {
        name = incomingName;
        type = incomingType;
        lineNumber = incomingLineNumber;
        isChangeable = changeable;
    }

    public VariableNode(String incomingName, tokenType incomingType, String incomingValue, int incomingLineNumber, boolean changeable)
    {
        name = incomingName;
        type = incomingType;
        value = incomingValue;
        lineNumber = incomingLineNumber;
        isChangeable = changeable;
    }

    public void setType(tokenType incomingType)
    {
        type = incomingType;
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
