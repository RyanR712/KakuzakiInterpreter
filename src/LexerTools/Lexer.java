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

    private boolean isComment;

    public Lexer()
    {
        tokenList = new ArrayList<>();
        keywordMap = new HashMap<>();
        initializeKeywordMap();

        isComment = false;
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
                    if (Character.isSpaceChar(currentLine.charAt(j)))
                    {
                        j = iterateOverWhitespace(currentLine, j);
                    }

                    currentChar = currentLine.charAt(j);

                    if (Character.isDigit(currentChar) || currentChar == '.')
                    {
                        j = handleToken(currentLine, j, tokenType.NUMBER);
                    }
                    else if (Character.isLetter(currentChar))
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
        if (keywordMap.containsKey(currentValue))
        {
            tokenList.add(new Token(keywordMap.get(currentValue)));
        }
        else
        {
            tokenList.add(new Token(currentTokenType, currentValue));
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
            tokenList.add(new Token());
        }
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
        keywordMap.put("mod", tokenType.MOD);
    }
}
