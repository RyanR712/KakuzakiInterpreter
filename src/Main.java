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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import CrossStageTools.Token;
import LexerTools.Lexer;
import ParserTools.Nodes.StructureNodes.ProgramNode;
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

            System.out.println(program);
        }
    }
}
