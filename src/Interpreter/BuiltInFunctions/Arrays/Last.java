/**
 * Describes the builtin function for getting the element at the last index of an array.
 */

package Interpreter.BuiltInFunctions.Arrays;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.ArrayDataType;
import Interpreter.InterpreterDataTypes.InterpreterDataType;
import Interpreter.InterpreterDataTypes.StringDataType;

import java.util.ArrayList;

public class Last extends BuiltInFunctionNode
{
    private final int VALID_ARGUMENT_LIST_SIZE = 2;

    /**
     * Creates the builtin function last, which takes two arguments: array, string.
     */
    public Last()
    {
        super("last", false);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        if (isArgListValid(args))
        {
            ArrayDataType array = ((ArrayDataType)args.get(0));

            args.get(1).fromString(array.getData()[array.getData().length].toString());
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        return args.size() == VALID_ARGUMENT_LIST_SIZE
                && !args.get(0).isChangeable() && args.get(0) instanceof ArrayDataType
                &&  args.get(1).isChangeable() && args.get(1) instanceof StringDataType;
    }
}
