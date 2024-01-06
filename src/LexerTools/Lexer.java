/**
 * Creates and stores a series of Tokens in an ArrayList.
 */

package LexerTools;

import java.util.ArrayList;

public class Lexer
{
    private final ArrayList<Token> tokenList;

    public Lexer()
    {
        tokenList = new ArrayList<>();
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
            int startIndex = 0;
            currentLine = incomingLines.get(i);

            for (int j = 0; j < currentLine.length(); j++)
            {
                currentChar = currentLine.charAt(j);

                if (Character.isWhitespace(currentChar))
                {
                    j += iterateOverWhitespace(currentLine, startIndex);
                }

                startIndex = j;

                if (Character.isDigit(currentChar) || currentChar == '.')
                {
                    j = handleToken(currentLine, startIndex, tokenType.NUMBER);
                }
                else if (Character.isLetter(currentChar))
                {
                    j = handleToken(currentLine, startIndex, tokenType.WORD);
                }
            }
            handleEOL();
        }
    }

    /**
     * Creates one Token, starting in the incoming line, at the incoming index, with the incoming tokenType.
     *
     * @param currentLine Incoming line.
     * @param startIndex Incoming index.
     * @param currentTokenType Incoming tokenType.
     * @return Number of indices iterated over.
     * @throws SecondDecimalPointException If a second decimal point is detected in a real number.
     */
    private int handleToken(String currentLine, int startIndex, tokenType currentTokenType) throws SecondDecimalPointException
    {
        String currentValue = "" + currentLine.charAt(startIndex);
        char currentChar;
        int returnIndex = startIndex + 1;
        boolean hasDecimalPoint = isRealNumber(currentTokenType, currentLine.charAt(0));

        for (int i = startIndex + 1; i < currentLine.length(); i++)
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
        tokenList.add(new Token(currentTokenType, currentValue));
        return returnIndex;
    }

    /**
     * Adds an EOL Token to the Token ArrayList.
     */
    private void handleEOL()
    {
        tokenList.add(new Token());
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
            currentIndex += i;
        }
        return currentIndex;
    }

    /**
     * Returns the Lexer's token list.
     * @return The Lexer's token list.
     */
    public ArrayList<Token> getTokenList()
    {
        return tokenList;
    }
}
