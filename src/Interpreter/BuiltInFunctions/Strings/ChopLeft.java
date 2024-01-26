/**
 * Describes the builtin function for taking the first <i>n</i> characters from a String.
 */

package Interpreter.BuiltInFunctions.Strings;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.IntegerDataType;
import Interpreter.InterpreterDataTypes.InterpreterDataType;
import Interpreter.InterpreterDataTypes.StringDataType;

import java.util.ArrayList;

public class ChopLeft extends BuiltInFunctionNode
{
    private final int VALID_ARGUMENT_LIST_SIZE = 3;

    /**
     * Creates the builtin function chopLeft, which takes 3 arguments: string, integer, var string.
     */
    public ChopLeft()
    {
        super("chopLeft", false);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        if (isArgListValid(args))
        {
            String stringToChop = args.get(0).toString();
            int chopTo = Integer.parseInt(args.get(1).toString());
            args.get(2).fromString(stringToChop.substring(0, chopTo));
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        return args.size() == VALID_ARGUMENT_LIST_SIZE
                && !args.get(0).isChangeable() && args.get(0) instanceof StringDataType
                && !args.get(1).isChangeable() && args.get(1) instanceof IntegerDataType
                &&  args.get(2).isChangeable() && args.get(2) instanceof StringDataType;
    }
}
