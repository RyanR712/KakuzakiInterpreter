/**
 * Describes the builtin function for getting the element at the first index of an array.
 */

package InterpreterTools.BuiltInFunctions.Arrays;

import InterpreterTools.BuiltInFunctions.BuiltInFunctionNode;
import InterpreterTools.InterpreterDataTypes.ArrayDataType;
import InterpreterTools.InterpreterDataTypes.InterpreterDataType;
import InterpreterTools.InterpreterDataTypes.StringDataType;

import java.util.ArrayList;

public class First extends BuiltInFunctionNode
{
    private final int VALID_ARGUMENT_LIST_SIZE = 2;

    /**
     * Creates the builtin function first, which takes two arguments: array, string.
     */
    public First()
    {
        super("first", false);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        if (isArgListValid(args))
        {
            args.get(1).fromString(((ArrayDataType)args.get(0)).getData()[0].toString());
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
