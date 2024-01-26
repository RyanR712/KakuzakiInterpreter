/**
 * Describes the builtin function for getting a random number.
 */

package Interpreter.BuiltInFunctions.Numbers;

import java.util.ArrayList;
import java.util.Random;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.IntegerDataType;
import Interpreter.InterpreterDataTypes.InterpreterDataType;

public class GetRandom extends BuiltInFunctionNode
{
    private final int VALID_ARGUMENT_LIST_SIZE = 1;

    /**
     * Creates the builtin function getRandom, which takes 1 argument: var integer.
     */
    public GetRandom()
    {
        super("getRandom", false);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        if (isArgListValid(args))
        {
            Random random = new Random();
            args.get(0).fromString(random.nextInt() + "");
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        return args.size() == VALID_ARGUMENT_LIST_SIZE
                && args.get(0).isChangeable() && args.get(0) instanceof IntegerDataType;
    }
}
