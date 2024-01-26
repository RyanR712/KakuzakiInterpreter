/**
 * Describes the builtin function for converting a real to an integer.
 */

package Interpreter.BuiltInFunctions.Numbers;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.IntegerDataType;
import Interpreter.InterpreterDataTypes.InterpreterDataType;
import Interpreter.InterpreterDataTypes.RealDataType;

import java.util.ArrayList;

public class RealToInteger extends BuiltInFunctionNode
{
    private final int VALID_ARGUMENT_LIST_SIZE = 2;

    /**
     * Creates the builtin function getRandom, which takes 2 argument: real, var integer.
     */
    public RealToInteger()
    {
        super("realToInteger", false);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        if (isArgListValid(args))
        {
            args.get(1).fromString((int)Float.parseFloat(args.get(0).toString()) + "");
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        return args.size() == VALID_ARGUMENT_LIST_SIZE
                && !args.get(0).isChangeable() && args.get(0) instanceof RealDataType
                &&  args.get(1).isChangeable() && args.get(1) instanceof IntegerDataType;
    }
}
