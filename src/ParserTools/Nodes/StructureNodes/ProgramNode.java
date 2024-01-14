package ParserTools.Nodes.StructureNodes;

import java.util.HashMap;

import ParserTools.Nodes.ASTNode;

public class ProgramNode extends ASTNode
{
    private HashMap<String, FunctionNode> functionMap;

    public ProgramNode(HashMap<String, FunctionNode> functions)
    {
        functionMap = functions;
    }

    public void addFunction(FunctionNode incomingFunction)
    {
        functionMap.put(incomingFunction.getName(), incomingFunction);
    }

    @Override
    public String toString()
    {
        String programString = "";

        String[] keyArray = new String[functionMap.keySet().size()];

        functionMap.keySet().toArray(keyArray);

        for (int i = 0; i < keyArray.length; i++)
        {
            programString += functionMap.get(keyArray[i]) + "\n";
        }

        return programString;
    }

    @Override
    public int getLineNumber()
    {
        return 0;
    }
}
