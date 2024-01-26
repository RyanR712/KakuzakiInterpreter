/**
 * Describes the builtin function for writing data to STDOut.
 */

package Interpreter.BuiltInFunctions.IO;

import java.util.ArrayList;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.InterpreterDataType;

public class Write extends BuiltInFunctionNode
{
    /**
     * Creates the builtin function write, which takes <i>n</i> arguments and writes them to STDOut.
     */
    public Write()
    {
        super("write", true, new ArrayList<>());
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        for (int i = 0; i < args.size(); i++)
        {
            System.out.print(args.get(i));
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
