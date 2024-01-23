/**
 * Describes the builtin function for writing data to STDOut with \n formatting.
 */

package InterpreterTools.BuiltInFunctions.IO;

import InterpreterTools.BuiltInFunctions.BuiltInFunctionNode;
import InterpreterTools.InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;

public class WriteLine extends BuiltInFunctionNode
{
    /**
     * Creates the builtin function writeLine, which takes <i>n</i> arguments
     * and writes them to STDOut with \n formatting.
     */
    public WriteLine()
    {
        super("writeLine", true, new ArrayList<>());
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        for (int i = 0; i < args.size(); i++)
        {
            System.out.println(args.get(i));
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        for (int i = 0; i < args.size(); i++)
        {
            if (args.get(i).isChangeable())
            {
                return false;
            }
        }
        return true;
    }
}
