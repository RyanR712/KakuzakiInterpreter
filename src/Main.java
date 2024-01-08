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

import LexerTools.Lexer;
import CrossStageTools.Token;
import CrossStageTools.tokenType;

public class Main
{
    /**
     * Runs each of the stages of the interpreter, in this order:
     * Lexing, Parsing, Semantic Analysis, Interpreting
     * @param args File name sent to Main.java.
     * @throws IOException If no file ending in ".zki" is found.
     */
    public static void main(String[] args) throws IOException
    {
        if (args.length != 1 || !args[0].contains(".zki"))
        {
            throw new FileNotFoundException("Please submit a valid filename as the only argument to Kakuzaki," +
                    "and include, specifically, a .zki file extension.");
        }
        else
        {
            Path path = Paths.get(args[0]);
            ArrayList<String> lines = (ArrayList<String>)(Files.readAllLines(path, StandardCharsets.UTF_8));

            Lexer lexer = new Lexer();

            try
            {
                lexer.lex(lines);
            }
            catch (Exception e)
            {
                System.out.println("The following error was found while lexing your program: " + e.getMessage());
            }

            printTokens(lexer.getTokenList());
        }
    }

    /**
     * Prints the incoming token list to console in a sensible format.
     * <p></p>
     * Since this method will not be useful past the lexing stage, it will be marked deprecated then.
     * @param tokenList Incoming token list.
     */
    private static void printTokens(ArrayList<Token> tokenList)
    {
        boolean isLineNumberPrinted = false;
        for (int i = 0; i < tokenList.size(); i++)
        {
            if (!isLineNumberPrinted)
            {
                System.out.print(tokenList.get(i).getLineNumber() + "\t");
                isLineNumberPrinted = true;
            }
            System.out.print(tokenList.get(i) + " ");
            if (tokenList.get(i).getType() == tokenType.EOL)
            {
                System.out.println();
                isLineNumberPrinted = false;
            }
        }
    }
}
