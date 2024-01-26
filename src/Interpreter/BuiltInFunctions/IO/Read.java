/**
 * Describes the builtin function for reading data from std out.
 */

package Interpreter.BuiltInFunctions.IO;

import java.util.ArrayList;
import java.util.Scanner;

import Interpreter.BuiltInFunctions.BuiltInFunctionNode;
import Interpreter.InterpreterDataTypes.InterpreterDataType;

public class Read extends BuiltInFunctionNode
{
    /**
     * Creates the builtin function read, which takes <i>n</i> var arguments and reads items from STDIn into them.
     */
    public Read()
    {
        super("read", true, new ArrayList<>());
    }

    @Override
    public void execute(ArrayList<InterpreterDataType> args)
    {
        Scanner keyboardInput = new Scanner(System.in);
        String[] userInput = keyboardInput.next().split(",");

        for (int i = 0; i < userInput.length && i < args.size(); i++)
        {
            args.get(i).fromString(userInput[i]);
        }
    }

    @Override
    public boolean isArgListValid(ArrayList<InterpreterDataType> args)
    {
        for (int i = 0; i < args.size(); i++)
        {
            if (!args.get(i).isChangeable())
            {
                return false;
            }
        }
        return true;
    }
}
