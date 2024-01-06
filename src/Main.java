import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import LexerTools.Lexer;
import LexerTools.Token;
import LexerTools.tokenType;
public class Main
{
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1 || !args[0].contains(".zki"))
        {
            throw new Exception("Please submit a valid filename as the only argument to Kakuzaki," +
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

    private static void printTokens(ArrayList<Token> tokenList)
    {
        for (int i = 0; i < tokenList.size(); i++)
        {
            System.out.print(tokenList.get(i) + " ");
            if (tokenList.get(i).getType() == tokenType.EOL)
            {
                System.out.println();
            }
        }
    }
}
