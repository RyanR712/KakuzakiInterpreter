/**
 * Describes one Kakuzaki program.
 */

package CrossStageTools.CrossStageNodes;

import java.util.HashMap;

import CrossStageTools.CrossStageNodes.ASTNode;
import CrossStageTools.CrossStageNodes.FunctionNode;

public class ProgramNode extends ASTNode
{
    private HashMap<String, FunctionNode> functionMap;

    /**
     * Constructs a ProgramNode with the incoming HashMap from String to FunctionNode as its function HashMap.
     *
     * @param functions Incoming HashMap from String to FunctionNode.
     */
    public ProgramNode(HashMap<String, FunctionNode> functions)
    {
        functionMap = functions;
    }

    /**
     * Adds the incoming FunctionNode to this ProgramNode's function HashMap.
     *
     * @param incomingFunction Incoming FunctionNode.
     */
    public void addFunction(FunctionNode incomingFunction)
    {
        functionMap.put(incomingFunction.getName(), incomingFunction);
    }

    /**
     * Adds all the K/V pairs from the incoming HashMap into this ProgramNode's function map.
     *
     * @param addendumMap Incoming HashMap.
     */
    public void addMap(HashMap<String, FunctionNode> addendumMap)
    {
        functionMap.putAll(addendumMap);
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
