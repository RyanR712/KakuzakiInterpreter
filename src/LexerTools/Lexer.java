/**
 * Creates and stores a series of Tokens in an ArrayList.
 */

package LexerTools;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer
{
    private final ArrayList<Token> tokenList;
    private final HashMap<String, tokenType> keywordMap;
    private final HashMap<String, tokenType> punctuationMap;

    private int lineNumber;
    private int currentIndentLevel;
    private boolean isComment;
    private boolean indentsHandled;

    public Lexer()
    {
        tokenList = new ArrayList<>();
        keywordMap = new HashMap<>();
        punctuationMap = new HashMap<>();
        initializeMaps();

        lineNumber = 1;
        currentIndentLevel = 0;
        isComment = false;
        indentsHandled = false;
    }

    /**
     * Iterates over all the incoming lines and populates the Token ArrayList with the Tokens created from them.
     *
     * @param incomingLines Incoming lines.
     * @throws SecondDecimalPointException If a second decimal point is detected in a real number.
     */
    public void lex(ArrayList<String> incomingLines) throws SecondDecimalPointException
    {
        String currentLine;
        char currentChar;
        for (int i = 0; i < incomingLines.size(); i++)
        {
            currentLine = incomingLines.get(i);

            for (int j = 0; j < currentLine.length(); j++)
            {
                if (!isComment)
                {
                    if(!indentsHandled)
                    {
                        iterateOverIndents(currentLine);
                    }

                    if (indentsHandled && Character.isSpaceChar(currentLine.charAt(j)))
                    {
                        j = iterateOverWhitespace(currentLine, j);
                    }

                    currentChar = currentLine.charAt(j);

                    if (Character.isDigit(currentChar) || currentChar == '.')
                    {
                        j = handleToken(currentLine, j, tokenType.NUMBER);
                    }
                    else if (Character.isLetter(currentChar) || punctuationMap.containsKey("" + currentChar))
                    {
                        j = handleToken(currentLine, j, tokenType.WORD);
                    }
                    else if (currentChar == '{')
                    {
                        isComment = true;
                    }
                }
                else if (currentLine.charAt(j) == '}')
                {
                    isComment = false;
                }
            }
            handleEOL();
        }
    }

    /**
     * Returns the Lexer's token list.
     * @return The Lexer's token list.
     */
    public ArrayList<Token> getTokenList()
    {
        return tokenList;
    }

    /**
     * Creates one Token, starting in the incoming line, at the incoming index, with the incoming tokenType.
     *
     * @param currentLine Incoming line.
     * @param currentIndex Incoming index.
     * @param currentTokenType Incoming tokenType.
     * @return Number of indices iterated over.
     * @throws SecondDecimalPointException If a second decimal point is detected in a real number.
     */
    private int handleToken(String currentLine, int currentIndex, tokenType currentTokenType) throws SecondDecimalPointException
    {
        String currentValue = "" + currentLine.charAt(currentIndex);
        char currentChar;
        int returnIndex = currentIndex + 1;
        boolean hasDecimalPoint = isRealNumber(currentTokenType, currentLine.charAt(0));

        for (int i = currentIndex + 1; i < currentLine.length(); i++)
        {
            currentChar = currentLine.charAt(i);

            if (!hasDecimalPoint && isRealNumber(currentTokenType, currentChar))
            {
                hasDecimalPoint = true;
            }
            else if (currentTokenType == tokenType.NUMBER && !isNumberValid(currentChar, hasDecimalPoint))
            {
                break;
            }
            else if (currentTokenType == tokenType.WORD && !Character.isLetterOrDigit(currentChar))
            {
                break;
            }
            returnIndex++;
            currentValue += currentChar;
        }
        if (anyMapContains(currentValue))
        {
            tokenList.add(new Token(getFromSomeMap(currentValue), lineNumber));
        }
        else
        {
            tokenList.add(new Token(currentTokenType, currentValue, lineNumber));
        }
        return returnIndex;
    }

    /**
     * Adds an EOL Token to the Token ArrayList.
     */
    private void handleEOL()
    {
        if (!isComment)
        {
            tokenList.add(new Token(lineNumber));
        }
        lineNumber++;
        indentsHandled = false;
    }

    /**
     * Checks and returns if the incoming character is a decimal point.
     * If a token of tokenType number is written with one decimal point inside, then it must be a real number.
     *
     * @param currentTokenType Incoming tokenType.
     * @param currentChar Incoming character.
     * @return If the Token being handled is a real number.
     */
    private boolean isRealNumber(tokenType currentTokenType, char currentChar)
    {
        return currentTokenType == tokenType.NUMBER && currentChar == '.';
    }

    /**
     * Checks and returns if the number Token being handled is valid with the addition of the incoming character.
     * If the Token in question already has a decimal point, given by the incoming boolean,
     * and the incoming character is a decimal point, then a SecondDecimalPointException is thrown.
     * <p></p>
     * If the Token in question does not have a second decimal point,
     * then checks and returns if the incoming character is a digit.
     * @param currentChar Incoming character.
     * @param hasDecimalPoint Incoming boolean.
     * @return If this is a well-formed number.
     * @throws SecondDecimalPointException If a real number with more than one decimal point has been detected.
     */
    private boolean isNumberValid(char currentChar, boolean hasDecimalPoint) throws SecondDecimalPointException
    {
        if (hasDecimalPoint && currentChar == '.')
        {
            throw new SecondDecimalPointException();
        }

        else return Character.isDigit(currentChar);
    }

    /**
     * Traverses all whitespace characters in the incoming string starting at the incoming index onward.
     * @param currentLine Incoming string.
     * @param currentIndex Incoming index.
     * @return Number of indices iterated over.
     */
    private int iterateOverWhitespace(String currentLine, int currentIndex)
    {
        char currentChar = currentLine.charAt(currentIndex);

        for (int i = currentIndex; i < currentLine.length() && Character.isSpaceChar(currentChar); i++)
        {
            currentChar = currentLine.charAt(i);
            currentIndex = i;
        }
        return currentIndex;
    }

    /**
     * Iterates over indents in the incoming line String and updates globals accordingly.
     *
     * @param currentLine Incoming line String.
     */
    private void iterateOverIndents(String currentLine)
    {
        final int SPACES_PER_TAB = 4;

        int spaceLevel;
        int newIndentLevel = 0;
        char currentChar = currentLine.charAt(0);

        for (int i = 0; i < currentLine.length() && (Character.isWhitespace(currentChar)); i++)
        {
            if (currentChar == '\t')
            {
                newIndentLevel++;
            }
            else if (Character.isSpaceChar(currentChar))
            {
                spaceLevel = iterateOverWhitespace(currentLine, i);
                i += spaceLevel;
                newIndentLevel += spaceLevel / SPACES_PER_TAB;
            }
            currentChar = currentLine.charAt(i);
        }

        if(newIndentLevel != currentIndentLevel)
        {
            tokenType dentType = newIndentLevel > currentIndentLevel ? tokenType.INDENT : tokenType.DEDENT;
            int numberOfPrints = dentType == tokenType.INDENT ? newIndentLevel - currentIndentLevel : Math.abs(newIndentLevel - currentIndentLevel);

            for (int j = 0; j < numberOfPrints; j++)
            {
                tokenList.add(new Token(dentType, lineNumber));
            }
            currentIndentLevel = newIndentLevel;
        }
        indentsHandled = true;
    }

    /**
     * Checks if any map contains the incoming value String.
     *
     * @param currentValue Incoming value string.
     * @return True if any maps contain currentValue.
     */
    private boolean anyMapContains(String currentValue)
    {
        return keywordMap.containsKey(currentValue) || punctuationMap.containsKey(currentValue);
    }

    /**
     * Returns the appropriate tokenType for the incoming String from some map.
     *
     * @param currentValue Incoming String.
     * @return Appropriate tokenType.
     */
    private tokenType getFromSomeMap(String currentValue)
    {
        return keywordMap.get(currentValue) == null ? punctuationMap.get(currentValue) : keywordMap.get(currentValue);
    }

    /**
     * Loads all tokenTypes into memory.
     */
    private void initializeMaps()
    {
        initializeKeywordMap();
        initializePunctuationMap();
    }

    /**
     * Maps each Kakuzaki keyword to a tokenType as written in tokenType.java.
     */
    private void initializeKeywordMap()
    {
        keywordMap.put("define", tokenType.DEFINE);
        keywordMap.put("constants", tokenType.CONSTANTS);
        keywordMap.put("variables", tokenType.VARIABLES);

        keywordMap.put("if", tokenType.IF);
        keywordMap.put("elsif", tokenType.ELSIF);
        keywordMap.put("else", tokenType.ELSE);
        keywordMap.put("then", tokenType.THEN);

        keywordMap.put("while", tokenType.WHILE);
        keywordMap.put("repeat", tokenType.REPEAT);
        keywordMap.put("until", tokenType.UNTIL);
        keywordMap.put("for", tokenType.FOR);
        keywordMap.put("from", tokenType.FROM);
        keywordMap.put("to", tokenType.TO);

        keywordMap.put("integer", tokenType.INTEGER);
        keywordMap.put("real", tokenType.REAL);
        keywordMap.put("boolean", tokenType.BOOLEAN);
        keywordMap.put("character", tokenType.CHARACTER);
        keywordMap.put("string", tokenType.STRING);
        keywordMap.put("array", tokenType.ARRAY);

        keywordMap.put("var", tokenType.VAR);
    }

    /**
     * Maps each Kakuzaki punctuation to a tokenType as written in tokenType.java.
     */
    private void initializePunctuationMap()
    {
        punctuationMap.put("+", tokenType.ADD);
        punctuationMap.put("-", tokenType.MINUS);
        punctuationMap.put("*", tokenType.MULT);
        punctuationMap.put("/", tokenType.DIV);
        punctuationMap.put("mod", tokenType.MOD);

        punctuationMap.put(">", tokenType.GTHAN);
        punctuationMap.put("<", tokenType.LTHAN);
        punctuationMap.put(">=", tokenType.GETO);
        punctuationMap.put("<=", tokenType.LETO);
        punctuationMap.put("=", tokenType.EQUAL);
        punctuationMap.put("<>", tokenType.NEQUAL);
        punctuationMap.put("not", tokenType.NOT);
        punctuationMap.put("and", tokenType.AND);
        punctuationMap.put("or", tokenType.OR);

        punctuationMap.put(":=", tokenType.ASSIGN);
        punctuationMap.put(":", tokenType.COLON);
        punctuationMap.put(";", tokenType.SEMICOLON);
        punctuationMap.put(",", tokenType.COMMA);
        punctuationMap.put("(", tokenType.LPAREN);
        punctuationMap.put(")", tokenType.RPAREN);
    }
}
