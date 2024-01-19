/**
 * Describes the builtin function for taking a portion of a string from one index to another.
 */

package InterpreterTools.BuiltInFunctions.Strings;

import InterpreterTools.BuiltInFunctions.BuiltInFunctionNode;
import InterpreterTools.InterpreterDataTypes.IntegerDataType;
import InterpreterTools.InterpreterDataTypes.InterpreterDataType;
import InterpreterTools.InterpreterDataTypes.StringDataType;

import java.util.ArrayList;

public class Substring extends BuiltInFunctionNode
{
    final int VALID_ARGUMENT_LIST_SIZE = 4;

    /**
     * Creates the builtin function substring, which takes 4 arguments: string, integer, integer, var string.
     */
    public Substring()
    {
        super("substring", false);
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        if (isArgListValid(args))
        {
            String stringToChop = args.get(0).toString();
            int from = Integer.parseInt(args.get(1).toString());
            int to = Integer.parseInt(args.get(2).toString());
            args.get(3).fromString(stringToChop.substring(from, to));
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        return args.size() == VALID_ARGUMENT_LIST_SIZE
                && !args.get(0).isChangeable() && args.get(0) instanceof StringDataType
                && !args.get(1).isChangeable() && args.get(1) instanceof IntegerDataType
                && !args.get(2).isChangeable() && args.get(2) instanceof IntegerDataType
                &&  args.get(3).isChangeable() && args.get(3) instanceof StringDataType;
    }
}
