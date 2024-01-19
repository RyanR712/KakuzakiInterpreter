/**
 * Describes the builtin function for writing data to std out.
 */

package InterpreterTools.BuiltInFunctions.IO;

import java.util.ArrayList;

import InterpreterTools.BuiltInFunctions.BuiltInFunctionNode;
import InterpreterTools.InterpreterDataTypes.InterpreterDataType;

public class Write extends BuiltInFunctionNode
{
    /**
     * Creates the builtin function write, which takes <i>n</i> arguments and writes them to STDOut.
     */
    public Write()
    {
        super("write", true);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        for (int i = 0; i < args.size(); i++)
        {
            System.out.print(args.get(i));
            if (i != args.size() - 1)
            {
                System.out.print(" ");
            }
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
