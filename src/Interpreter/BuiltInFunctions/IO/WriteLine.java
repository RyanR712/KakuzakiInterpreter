/**
 * Describes the builtin function for writing data to STDOut with \n formatting.
 */

package Interpreter.BuiltInFunctions.IO;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.InterpreterDataType;

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
