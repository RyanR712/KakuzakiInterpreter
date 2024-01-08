/**
 * Basic testing for Lexer.
 */

package test;

import LexerTools.tokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.nio.file.Files;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import LexerTools.Lexer;
import LexerTools.Token;

public class LexerTest
{
    Lexer lexer = new Lexer();

    HashMap<String, ArrayList<Token>> tokenListMap = new HashMap<>();

    boolean mapInitialized = false;

    @Test
    void keywordSoupTests() throws Exception
    {
        attemptToReadAndAssertTokenListsAreEqual("keywordSoup.zki");
    }

    @Test
    void indentTests()
    {
        attemptToReadAndAssertTokenListsAreEqual("indentSoup.zki");
    }

    /**
     * Attempts to read the incoming filename and, if able, proceeds to assert appropriately.
     *
     * @param filename Incoming filename.
     */
    private void attemptToReadAndAssertTokenListsAreEqual(String filename)
    {
        ArrayList<String> lines = attemptToRead(filename);
        if(!mapInitialized)
        {
            initializeTokenListMap();
        }
        try
        {
            assertTrue(areTokenListsEqual(lexer.lexAndReturnTokenList(lines), tokenListMap.get(filename)));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Tries to open the incoming filename, and, if successful, returns the lines from the opened file.
     *
     * @param filename Incoming filename.
     * @return Lines from the file opened with filename.
     */
    private ArrayList<String> attemptToRead(String filename)
    {
        Path path = Paths.get("testPrograms/" + filename);

        ArrayList<String> lines = new ArrayList<>();
        try
        {
            lines = (ArrayList<String>) (Files.readAllLines(path, StandardCharsets.UTF_8));
        }
        catch (Exception e)
        {
            System.out.println("Attempted to read file " + path.getFileName() + " and failed.");
        }
        return lines;
    }

    /**
     * Checks and returns if the incoming former and latter Token lists are equivalent.
     * Equality is true iff both lists contain equal Tokens in the same order.
     *
     * @param listOne Former Token list.
     * @param listTwo Latter Token list.
     * @return True if listOne and listTwo are equal, as defined above.
     */
    private boolean areTokenListsEqual(ArrayList<Token> listOne, ArrayList<Token> listTwo)
    {
        if (listOne.size() != listTwo.size())
        {
            System.out.println(listOne.size() + " and " + listTwo.size() + " are not equal.");
            return false;
        }
        for (int i = 0; i < listOne.size(); i++)
        {
            if (!areTokensEqual(listOne.get(i), listTwo.get(i)))
            {
                System.out.println(listOne.get(i) + " and " + listTwo.get(i) + " are not equal.");
                return false;
            }
        }
        return true;
    }

    /**
     * Checks and returns if the former and latter incoming Tokens are equal.
     * Equality is true iff both Tokens share the same tokenType, lineNumber and values, if any.
     *
     * @param tokenOne Former incoming Token.
     * @param tokenTwo Latter incoming Token.
     * @return True if tokenOne and tokenTwo are equal, as defined above.
     */
    private boolean areTokensEqual(Token tokenOne, Token tokenTwo)
    {
        return (tokenOne.getType() == tokenTwo.getType()) && (tokenOne.getLineNumber() == tokenTwo.getLineNumber()) &&
                areTokenValuesEqual(tokenOne, tokenTwo);
    }

    /**
     * Checks if the values of the former and latter incoming Tokens are equal if they have the capacity to have values.
     * The tokenTypes that have the capacity to have values are IDENTIFIER, NUMBER, STRINGLITERAL and CHARLITERAL.
     *
     * @param tokenOne Former incoming Token.
     * @param tokenTwo Latter incoming Token.
     * @return Result of String equality between the values of tokenOne and tokenTwo, or true if they don't have the
     * capacity to have values as defined above.
     */
    private boolean areTokenValuesEqual(Token tokenOne, Token tokenTwo)
    {
        if (tokenOne.getType() == tokenType.IDENTIFIER || tokenOne.getType() == tokenType.NUMBER ||
            tokenOne.getType() == tokenType.STRINGLITERAL || tokenOne.getType() == tokenType.CHARLITERAL)
        {
            return tokenOne.getValue().equals(tokenTwo.getValue());
        }
        else return true;
    }

    /**
     * Loads expected Token lists into the global HashMap.
     */
    private void initializeTokenListMap()
    {
        ArrayList<Token> keywordSoupMap = new ArrayList<>();
        keywordSoupMap.add(new Token(tokenType.DEFINE, 1));
        keywordSoupMap.add(new Token(tokenType.BOOLEAN, 1));
        keywordSoupMap.add(new Token(tokenType.NOT, 1));
        keywordSoupMap.add(new Token(tokenType.OR, 1));
        keywordSoupMap.add(new Token(tokenType.AND, 1));
        keywordSoupMap.add(new Token(1));
        tokenListMap.put("keywordSoup.zki", keywordSoupMap);

        ArrayList<Token> indentSoupMap = new ArrayList<>();
        indentSoupMap.add(new Token(tokenType.IDENTIFIER, "hello", 1));
        indentSoupMap.add(new Token(1));
        indentSoupMap.add(new Token(tokenType.INDENT, 2));
        indentSoupMap.add(new Token(tokenType.IDENTIFIER, "goodbye", 2));
        indentSoupMap.add(new Token(2));
        indentSoupMap.add(new Token(tokenType.INDENT, 3));
        indentSoupMap.add(new Token(tokenType.IDENTIFIER, "howDoYouDo", 3));
        indentSoupMap.add(new Token(3));
        indentSoupMap.add(new Token(tokenType.INDENT, 4));
        indentSoupMap.add(new Token(tokenType.INDENT, 4));
        indentSoupMap.add(new Token(tokenType.IDENTIFIER, "imOverHereNow", 4));
        indentSoupMap.add(new Token(4));
        indentSoupMap.add(new Token(tokenType.DEDENT, 5));
        indentSoupMap.add(new Token(tokenType.DEDENT, 5));
        indentSoupMap.add(new Token(tokenType.IDENTIFIER, "backHereNow", 5));
        indentSoupMap.add(new Token(5));
        indentSoupMap.add(new Token(tokenType.DEDENT, 6));
        indentSoupMap.add(new Token(tokenType.IDENTIFIER, "fartherBack", 6));
        indentSoupMap.add(new Token(6));
        indentSoupMap.add(new Token(tokenType.DEDENT, 7));
        indentSoupMap.add(new Token(tokenType.IDENTIFIER, "backAtStart", 7));
        indentSoupMap.add(new Token(7));
        tokenListMap.put("indentSoup.zki", indentSoupMap);

        mapInitialized = true;
    }
}
