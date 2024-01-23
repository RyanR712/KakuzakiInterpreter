/**
 * Interprets programs according to the supplied language definition.
 * <p></p>
 * I am the author for all the code in this repository.
 *
 * @author Ryan R
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import CrossStageTools.CrossStageNodes.FunctionNode;
import CrossStageTools.Token;
import InterpreterTools.BuiltInFunctions.Arrays.*;
import InterpreterTools.BuiltInFunctions.IO.*;
import InterpreterTools.BuiltInFunctions.Numbers.*;
import InterpreterTools.BuiltInFunctions.Strings.*;
import InterpreterTools.Interpreter;
import LexerTools.Lexer;
import CrossStageTools.CrossStageNodes.ProgramNode;
import ParserTools.Parser;

public class Main
{
    /**
     * Runs each of the stages of the interpreter, in this order:
     * Lexing, Parsing, Semantic Analysis, Interpreting
     *
     * @param args File name sent to Main.java.
     * @throws IOException If no file ending in ".zki" is found.
     * @throws Exception If one of the above stages fails.
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1 || !args[0].contains(".zki"))
        {
            throw new FileNotFoundException("Please submit a valid filename as the only argument to Kakuzaki," +
                    "and include, specifically, a .zki file extension.");
        }
        else
        {
            ArrayList<Token> tokenList = new ArrayList<Token>();
            Path path = Paths.get(args[0]);
            ArrayList<String> lines = (ArrayList<String>)(Files.readAllLines(path, StandardCharsets.UTF_8));

            Lexer lexer = new Lexer();

            try
            {
                lexer.lex(lines);
                tokenList = lexer.getTokenList();
            }
            catch (Exception e)
            {
                System.out.println("The following error was found while lexing your program: " + e.getMessage() +
                        " Lexer's working output written to /src/Debug/LexerDumps.txt.");
                lexer.writeDebugOutput();
                throw new Exception("Lexing failed.");
            }

            lexer.writeDebugOutput();

            Parser parser = new Parser(lexer.getTokenList());

            ProgramNode program;

            try
            {
                program = parser.parse();
            }
            catch (Exception e)
            {
                System.out.println("The following error was found while parsing your program: " + e.getMessage() +
                        " Parser's output written to /src/Debug/ParserDumps.txt.");
                lexer.writeDebugOutput(tokenList);
                parser.writeDebugOutput();
                throw new Exception("Parsing failed.");
            }

            program.addMap(loadBuiltInFunctions());

            Interpreter interpreter = new Interpreter(program);

            try
            {
                interpreter.interpret();
            }
            catch (Exception e)
            {
                System.out.println("The following error was found while interpreting your program: " + e.getMessage());
                lexer.writeDebugOutput(tokenList);
                parser.writeDebugOutput();
                throw new Exception("Interpreting failed.");
            }

        }
    }

    /**
     * Returns a HashMap with all builtin functions.
     *
     * @return HashMap with all builtin functions.
     */
    private static HashMap<String, FunctionNode> loadBuiltInFunctions()
    {
        HashMap<String, FunctionNode> builtInMap = new HashMap<>();

        builtInMap.put("first", new First());
        builtInMap.put("last", new Last());

        builtInMap.put("read", new Read());
        builtInMap.put("write", new Write());
        builtInMap.put("writeLine", new WriteLine());

        builtInMap.put("getRandom", new GetRandom());
        builtInMap.put("integerToReal", new IntegerToReal());
        builtInMap.put("realToInteger", new RealToInteger());
        builtInMap.put("squareRoot", new SquareRoot());

        builtInMap.put("chopLeft", new ChopLeft());
        builtInMap.put("chopRight", new ChopRight());
        builtInMap.put("substring", new Substring());

        return builtInMap;
    }
}
