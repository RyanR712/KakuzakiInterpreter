/**
 * Describes a data type during interpretation.
 */

package Interpreter.InterpreterDataTypes;

public abstract class InterpreterDataType
{
    @Override
    public abstract String toString();

    /**
     * Sets this InterpreterDataType's data to the incoming String, which will be parsed accordingly.
     *
     * @param input Incoming String.
     */
    public abstract void fromString(String input);

    /**
     * Checks and returns if this InterpreterDataType is changeable, i.e a variable.
     *
     * @return True if this InterpreterDataType is variable. False if this InterpreterDataType is constant.
     */
    public abstract boolean isChangeable();

    /**
     * Returns this InterpreterDataType's line number.
     *
     * @return This InterpreterDataType's line number.
     */
    public abstract int getLineNumber();
}
