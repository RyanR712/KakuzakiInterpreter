/**
 * Tests for Lexer.
 */

package test;

import CrossStageTools.tokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.nio.file.Files;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import LexerTools.Lexer;
import CrossStageTools.Token;

public class LexerTest
{
    @Test
    void keywordSoupTest()
    {
        attemptToReadAndAssertTokenListsAreEqual("keywordSoup.zki");
    }

    @Test
    void punctuationSoupTest()
    {
        attemptToReadAndAssertTokenListsAreEqual("punctuationSoup.zki");
    }

    @Test
    void numberSoupTest()
    {
        attemptToReadAndAssertTokenListsAreEqual("numberSoup.zki");
    }

    @Test
    void identifierSoupTest()
    {
        attemptToReadAndAssertTokenListsAreEqual("identifierSoup.zki");
    }

    @Test
    void stringLiteralTest()
    {
        attemptToReadAndAssertTokenListsAreEqual("stringLiteralSoup.zki");
    }

    @Test
    void characterLiteralTest()
    {
        attemptToReadAndAssertTokenListsAreEqual("characterLiteralSoup.zki");
    }

    @Test
    void indentSoupTest()
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
        Lexer lexer = new Lexer();

        HashMap<String, ArrayList<Token>> tokenListMap = new HashMap<>();

        ArrayList<String> lines = attemptToRead(filename);

        initializeTokenListMap(tokenListMap);

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
            System.out.println("List one's size " + listOne.size() + " and list two's size " + listTwo.size()
                                + " are not equal.");
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
    private void initializeTokenListMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        initializeKeywordSoupMap(tokenListMap);
        initializePunctuationSoupMap(tokenListMap);
        initializeNumberSoupMap(tokenListMap);
        initializeIdentifierSoupMap(tokenListMap);
        initializeStringLiteralSoupMap(tokenListMap);
        initializeCharacterLiteralSoupMap(tokenListMap);
        initializeIndentSoupMap(tokenListMap);
    }

    /**
     * Loads the expected Token list for keywordSoup.zki into the incoming HashMap.
     *
     * @param tokenListMap Incoming HashMap.
     */
    private void initializeKeywordSoupMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        ArrayList<Token> keywordSoupList = new ArrayList<>();
        keywordSoupList.add(new Token(tokenType.DEFINE, 1));
        keywordSoupList.add(new Token(tokenType.CONSTANTS, 1));
        keywordSoupList.add(new Token(tokenType.VARIABLES, 1));
        keywordSoupList.add(new Token(tokenType.IF, 1));
        keywordSoupList.add(new Token(tokenType.ELSIF, 1));
        keywordSoupList.add(new Token(tokenType.ELSE, 1));
        keywordSoupList.add(new Token(tokenType.THEN, 1));
        keywordSoupList.add(new Token(1));

        keywordSoupList.add(new Token(tokenType.WHILE, 2));
        keywordSoupList.add(new Token(tokenType.REPEAT, 2));
        keywordSoupList.add(new Token(tokenType.UNTIL, 2));
        keywordSoupList.add(new Token(tokenType.FOR, 2));
        keywordSoupList.add(new Token(tokenType.FROM, 2));
        keywordSoupList.add(new Token(tokenType.TO, 2));
        keywordSoupList.add(new Token(2));

        keywordSoupList.add(new Token(tokenType.INTEGER, 3));
        keywordSoupList.add(new Token(tokenType.REAL, 3));
        keywordSoupList.add(new Token(tokenType.BOOLEAN, 3));
        keywordSoupList.add(new Token(tokenType.CHARACTER, 3));
        keywordSoupList.add(new Token(tokenType.STRING, 3));
        keywordSoupList.add(new Token(tokenType.ARRAY, 3));
        keywordSoupList.add(new Token(tokenType.VAR, 3));
        keywordSoupList.add(new Token(3));

        tokenListMap.put("keywordSoup.zki", keywordSoupList);
    }

    /**
     * Loads the expected Token list for punctuationSoup.zki into the incoming HashMap.
     *
     * @param tokenListMap Incoming HashMap.
     */
    private void initializePunctuationSoupMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        ArrayList<Token> punctuationSoupList = new ArrayList<>();
        punctuationSoupList.add(new Token(tokenType.ADD, 1));
        punctuationSoupList.add(new Token(tokenType.MINUS, 1));
        punctuationSoupList.add(new Token(tokenType.MULT, 1));
        punctuationSoupList.add(new Token(tokenType.DIV, 1));
        punctuationSoupList.add(new Token(tokenType.MOD, 1));
        punctuationSoupList.add(new Token(1));

        punctuationSoupList.add(new Token(tokenType.GTHAN, 2));
        punctuationSoupList.add(new Token(tokenType.LTHAN, 2));
        punctuationSoupList.add(new Token(tokenType.GETO, 2));
        punctuationSoupList.add(new Token(tokenType.LETO, 2));
        punctuationSoupList.add(new Token(tokenType.EQUAL, 2));
        punctuationSoupList.add(new Token(tokenType.NEQUAL, 2));
        punctuationSoupList.add(new Token(tokenType.NOT, 2));
        punctuationSoupList.add(new Token(tokenType.AND, 2));
        punctuationSoupList.add(new Token(tokenType.OR, 2));
        punctuationSoupList.add(new Token(2));

        punctuationSoupList.add(new Token(tokenType.ASSIGN, 3));
        punctuationSoupList.add(new Token(tokenType.COLON, 3));
        punctuationSoupList.add(new Token(tokenType.SEMICOLON, 3));
        punctuationSoupList.add(new Token(tokenType.COMMA, 3));
        punctuationSoupList.add(new Token(tokenType.LPAREN, 3));
        punctuationSoupList.add(new Token(tokenType.RPAREN, 3));
        punctuationSoupList.add(new Token(3));

        tokenListMap.put("punctuationSoup.zki", punctuationSoupList);
    }

    /**
     * Loads the expected Token list for numberSoup.zki into the incoming HashMap.
     *
     * @param tokenListMap Incoming HashMap.
     */
    private void initializeNumberSoupMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        ArrayList<Token> numberSoupList = new ArrayList<>();

        numberSoupList.add(new Token(tokenType.NUMBER, "12345", 1));
        numberSoupList.add(new Token(tokenType.NUMBER, "4", 1));
        numberSoupList.add(new Token(tokenType.NUMBER, ".55", 1));
        numberSoupList.add(new Token(tokenType.NUMBER, "6.6", 1));
        numberSoupList.add(new Token(tokenType.NUMBER, "7.00000007", 1));
        numberSoupList.add(new Token(tokenType.NUMBER, "42", 1));
        numberSoupList.add(new Token(1));

        tokenListMap.put("numberSoup.zki", numberSoupList);
    }

    /**
     * Loads the expected Token list for identifierSoup.zki into the incoming HashMap.
     *
     * @param tokenListMap Incoming HashMap.
     */
    private void initializeIdentifierSoupMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        ArrayList<Token> identifierSoupList = new ArrayList<>();

        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "ONLY", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "THE", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "SOUNDS", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "OF", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "WHAT", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "MY", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "MIND", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "WANTS", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "TO", 1));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "HEAR", 1));
        identifierSoupList.add(new Token(1));

        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "BLOCK", 2));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "OUT", 2));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "THE", 2));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "RUSH", 2));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "HOUR", 2));
        identifierSoupList.add(new Token(2));

        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "BLOCK", 3));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "OUT", 3));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "THE", 3));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "TIRED", 3));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "HERDS", 3));
        identifierSoupList.add(new Token(3));

        identifierSoupList.add(new Token(4));

        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "ON", 5));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "THE", 5));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "SHADED", 5));
        identifierSoupList.add(new Token(tokenType.IDENTIFIER, "SIDE", 5));
        identifierSoupList.add(new Token(5));

        tokenListMap.put("identifierSoup.zki", identifierSoupList);
    }

    /**
     * Loads the expected Token list for stringLiteralSoup.zki into the incoming HashMap.
     *
     * @param tokenListMap Incoming HashMap.
     */
    private void initializeStringLiteralSoupMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        ArrayList<Token> stringLiteralSoupList = new ArrayList<>();

        stringLiteralSoupList.add(new Token(tokenType.STRINGLITERAL, "o", 1));
        stringLiteralSoupList.add(new Token(tokenType.STRINGLITERAL, "hello", 1));
        stringLiteralSoupList.add(new Token(tokenType.STRINGLITERAL, " w e i r d sp acing", 1));
        stringLiteralSoupList.add(new Token(tokenType.STRINGLITERAL, "123.4", 1));
        stringLiteralSoupList.add(new Token(tokenType.STRINGLITERAL, ".!()*^4'']", 1));
        stringLiteralSoupList.add(new Token(1));

        tokenListMap.put("stringLiteralSoup.zki", stringLiteralSoupList);
    }

    /**
     * Loads the expected Token list for characterLiteralSoup.zki into the incoming HashMap.
     *
     * @param tokenListMap Incoming HashMap.
     */
    private void initializeCharacterLiteralSoupMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        ArrayList<Token> characterLiteralSoupList = new ArrayList<>();

        characterLiteralSoupList.add(new Token(tokenType.CHARLITERAL, "d", 1));
        characterLiteralSoupList.add(new Token(tokenType.CHARLITERAL, "!", 1));
        characterLiteralSoupList.add(new Token(tokenType.CHARLITERAL, "ж", 1));
        characterLiteralSoupList.add(new Token(tokenType.CHARLITERAL, "\\", 1));
        characterLiteralSoupList.add(new Token(tokenType.CHARLITERAL, " ", 1));
        characterLiteralSoupList.add(new Token(1));

        tokenListMap.put("characterLiteralSoup.zki", characterLiteralSoupList);
    }

    /**
     * Loads the expected Token list for indentSoup.zki into the incoming HashMap.
     *
     * @param tokenListMap Incoming HashMap.
     */
    private void initializeIndentSoupMap(HashMap<String, ArrayList<Token>> tokenListMap)
    {
        ArrayList<Token> indentSoupList = new ArrayList<>();
        indentSoupList.add(new Token(tokenType.VAR, 1));
        indentSoupList.add(new Token(1));
        indentSoupList.add(new Token(tokenType.INDENT, 2));
        indentSoupList.add(new Token(tokenType.VAR, 2));
        indentSoupList.add(new Token(2));
        indentSoupList.add(new Token(tokenType.INDENT, 3));
        indentSoupList.add(new Token(tokenType.VAR, 3));
        indentSoupList.add(new Token(3));
        indentSoupList.add(new Token(tokenType.INDENT, 4));
        indentSoupList.add(new Token(tokenType.INDENT, 4));
        indentSoupList.add(new Token(tokenType.VAR, 4));
        indentSoupList.add(new Token(4));
        indentSoupList.add(new Token(tokenType.DEDENT, 5));
        indentSoupList.add(new Token(tokenType.DEDENT, 5));
        indentSoupList.add(new Token(tokenType.VAR, 5));
        indentSoupList.add(new Token(5));
        indentSoupList.add(new Token(tokenType.DEDENT, 6));
        indentSoupList.add(new Token(tokenType.VAR, 6));
        indentSoupList.add(new Token(6));
        indentSoupList.add(new Token(tokenType.DEDENT, 7));
        indentSoupList.add(new Token(tokenType.VAR, 7));
        indentSoupList.add(new Token(7));
        tokenListMap.put("indentSoup.zki", indentSoupList);
    }
}
