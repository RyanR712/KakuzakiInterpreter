/**
 * Describes the builtin function for taking the square root of a number.
 */

package Interpreter.BuiltInFunctions.Numbers;

import java.util.ArrayList;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.InterpreterDataType;
import Interpreter.InterpreterDataTypes.RealDataType;

public class SquareRoot extends BuiltInFunctionNode
{
    private final int VALID_ARGUMENT_LIST_SIZE = 2;

    /**
     * Creates the builtin function squareRoot, which takes 2 arguments: real, var real.
     */
    public SquareRoot()
    {
        super("squareRoot", false);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        if (isArgListValid(args))
        {
            args.get(3).fromString(Math.sqrt(Float.parseFloat(args.get(0).toString())) + "");
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        return args.size() == VALID_ARGUMENT_LIST_SIZE
                && !args.get(0).isChangeable() && args.get(0) instanceof RealDataType
                &&  args.get(1).isChangeable() && args.get(1) instanceof RealDataType;
    }
}
