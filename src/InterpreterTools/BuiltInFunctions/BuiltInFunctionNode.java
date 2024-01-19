/**
 * Describes one builtin Kakuzaki function.
 */

package InterpreterTools.BuiltInFunctions;

import java.util.ArrayList;

import CrossStageTools.CrossStageNodes.FunctionNode;
import InterpreterTools.InterpreterDataTypes.InterpreterDataType;

public abstract class BuiltInFunctionNode extends FunctionNode
{
    /**
     * Calls the two arg constructor for FunctionNode with the incoming String as a name
     * and the incoming boolean as whether this builtin function is variadic.
     *
     * @param name Incoming String.
     * @param variadic Incoming boolean.
     * @see "For definitions: https://github.com/RyanR712/KakuzakiInterpreter/wiki/Built%E2%80%90in-Functions"
     */
    public BuiltInFunctionNode(String name, boolean variadic)
    {
        super(name, variadic);
    }

    /**
     * Follows the predefined algorithm for this builtin function with the incoming InterpreterDataTypes as arguments.
     *
     * @param args Incoming InterpreterDataTypes.
     */
    public abstract void execute(ArrayList<InterpreterDataType> args);

    /**
     * Checks and returns whether the incoming InterpreterDataTypes are of the correct size in the correct order.
     *
     * @param args Incoming InterpreterDataTypes.
     * @return Whether args is sized and ordered correctly.
     */
    public abstract boolean isArgListValid(ArrayList<InterpreterDataType> args);
}
