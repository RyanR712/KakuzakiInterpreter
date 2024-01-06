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
     * Iterates over all the incoming lines and creates Tokens from them.
     *
     * @param incomingLines Lines to tokenize.
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
                    j += iterateOverWhitespace(currentLine, startIndex, currentChar);
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

    private int handleToken(String currentLine, int startIndex, tokenType currentTokenType) throws SecondDecimalPointException
    {
        String currentValue = "" + currentLine.charAt(startIndex);
        char currentChar;
        int returnIndex = startIndex + 1;
        boolean hasDecimalPoint = currentLine.charAt(0) == '.';
        for (int i = startIndex + 1; i < currentLine.length(); i++)
        {
            currentChar = currentLine.charAt(i);

            if (!hasDecimalPoint && isDecimal(currentTokenType, currentChar))
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

    private void handleEOL()
    {
        tokenList.add(new Token());
    }

    private boolean isDecimal(tokenType currentTokenType, char currentChar)
    {
        return currentTokenType == tokenType.NUMBER && currentChar == '.';
    }

    private boolean isNumberValid(char currentChar, boolean hasDecimalPoint) throws SecondDecimalPointException
    {
        if (hasDecimalPoint && currentChar == '.')
        {
            throw new SecondDecimalPointException();
        }

        else return Character.isLetterOrDigit(currentChar);
    }

    private int iterateOverWhitespace(String currentLine, int currentIndex, char currentChar)
    {
        for (int i = currentIndex; i < currentLine.length() && Character.isSpaceChar(currentChar); i++)
        {
            currentChar = currentLine.charAt(i);
            currentIndex += i;
        }
        return currentIndex;
    }

    public ArrayList<Token> getTokenList()
    {
        return tokenList;
    }
}
