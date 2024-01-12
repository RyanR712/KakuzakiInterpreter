/**
 * Creates and stores a series of Tokens in an ArrayList.
 */

package LexerTools;

import CrossStageTools.Token;
import CrossStageTools.tokenType;
import Exceptions.SecondDecimalPointException;
import Exceptions.SyntaxErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.time.ZonedDateTime;

public class Lexer
{
    private final ArrayList<Token> tokenList;
    private final HashMap<String, tokenType> keywordMap;
    private final HashMap<String, tokenType> punctuationMap;

    private int lineNumber;
    private int currentIndentLevel;
    private boolean isComment;
    private boolean indentsHandled;

    /**
     * Instantiates the Lexer with an empty Token list, empty maps and a line number.
     */
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
     * @throws Exception If syntax errors are detected.
     */
    public void lex(ArrayList<String> incomingLines) throws Exception
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
                    else if (Character.isLetter(currentChar))
                    {
                        j = handleToken(currentLine, j, tokenType.IDENTIFIER);
                    }
                    else if (isPunctuation(currentChar))
                    {
                        j = handlePunctuation(currentLine, j, currentLine.charAt(j));
                    }
                    else if (currentChar == '\"')
                    {
                        j = handleStringLiteral(currentLine, j);
                    }
                    else if (currentChar == '\'')
                    {
                        j = handleCharacterLiteral(currentLine, j);
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
        handleTrailingDedents();
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
     * Runs the lexing of the incoming line Strings and returns the created Token list in the same method.
     *
     * @param incomingLines Incoming line Strings.
     * @return Created Token list.
     * @throws Exception If syntax errors are detected.
     */
    public ArrayList<Token> lexAndReturnTokenList(ArrayList<String> incomingLines) throws Exception
    {
        return getTokenList();
    }

    /**
     * Writes a formatted String of this Lexer's Tokens to a .txt File in /LexerDumps.
     *
     * @throws IOException If a File cannot be created.
     */
    public void writeDebugOutput(ArrayList<Token> localTokenList) throws IOException
    {
        ZonedDateTime zdt = ZonedDateTime.now();
        String formattedOutput = zdt.getDayOfMonth() + "-" + zdt.getMonthValue() + "-" + zdt.getYear() + "@" +
                                zdt.getHour() + "_" + zdt.getMinute() + "_" + zdt.getSecond();
        File file = new File("./src/Debug/LexerDumps", "lexer_" + formattedOutput + ".txt");
        if (file.createNewFile())
        {
            FileWriter fw = new FileWriter(file);

            fw.write(formatTokens(localTokenList));

            fw.close();
        }
    }

    /**
     * Writes this Lexer's Token list to a dump file.
     *
     * @throws IOException If there is an issue with writing to a file.
     */
    public void writeDebugOutput() throws IOException
    {
        writeDebugOutput(tokenList);
    }

    /**
     * Formats this Lexer's token list in a sensible format.
     */
    private String formatTokens()
    {
        return formatTokens(tokenList);
    }

    private String formatTokens(ArrayList<Token> localTokenList)
    {
        String dumpString = "";
        boolean isLineNumberPrinted = false;
        for (int i = 0; i < localTokenList.size(); i++)
        {
            if (!isLineNumberPrinted)
            {
                dumpString += localTokenList.get(i).getLineNumber() + "\t";
                isLineNumberPrinted = true;
            }
            dumpString += localTokenList.get(i) + " ";
            if (localTokenList.get(i).getType() == tokenType.EOL)
            {
                dumpString += "\n";
                isLineNumberPrinted = false;
            }
        }
        return dumpString;
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
            else if (currentTokenType == tokenType.IDENTIFIER && !Character.isLetterOrDigit(currentChar))
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
        return returnIndex - 1;
    }

    /**
     * Creates both multi character and single character punctuation Tokens from the incoming line string,
     * starting at the incoming index and with the incoming character.
     * <p></p>
     * Also determines the number of characters the created Token takes up.
     *
     * @param currentLine Incoming line String.
     * @param currentIndex Incoming index.
     * @param currentChar Incoming character.
     * @return Number of characters the created Token takes up.
     */
    private int handlePunctuation(String currentLine, int currentIndex, char currentChar)
    {
        if (currentChar == ':' || currentChar == '<' || currentChar == '>')
        {
            handleMultiCharacterPunctuation(currentLine, currentIndex, currentChar);
            return 1 + currentIndex;
        }
        else
        {
            tokenList.add(new Token(punctuationMap.get("" + currentChar), lineNumber));
            return currentIndex;
        }
    }

    /**
     * Creates multi character punctuation Tokens from the incoming line String, starting at the incoming index
     * and with the incoming character.
     *
     * @param currentLine Incoming line String.
     * @param currentIndex Incoming index.
     * @param currentChar Incoming character.
     */
    private void handleMultiCharacterPunctuation(String currentLine, int currentIndex, char currentChar)
    {
        char nextChar = ' ';
        try
        {
            nextChar = peekAhead(currentLine, currentIndex + 1);
        }
        catch (Exception ignored)
        {}

        if (currentChar == ':' || currentChar == '>' || currentChar == '<')
        {
            boolean isLoneLessThanCharacter = currentChar == '<' && !(nextChar == '=' || nextChar == '>');
            if (isLoneLessThanCharacter)
            {
                addPunctuationToken(currentChar);
            }
            else
            {
                addPunctuationToken(currentChar, nextChar);
            }
        }
    }

    /**
     * Adds a punctuation Token with the incoming character followed by the space character.
     * Since the String inside the other addPunctuationToken method is trimmed,
     * this is like adding an empty character.
     *
     * @param currentChar Incoming character.
     */
    private void addPunctuationToken(char currentChar)
    {
        addPunctuationToken(currentChar, ' ');
    }

    /**
     * Adds a punctuation Token with the first incoming character followed by the next incoming character.
     *
     * @param currentChar First incoming character.
     * @param nextChar Next incoming character.
     */
    private void addPunctuationToken(char currentChar, char nextChar)
    {
        tokenList.add(new Token(punctuationMap.get((currentChar + "" + nextChar).trim()), lineNumber));
    }

    /**
     * Creates a Token of type STRINGLITERAL starting in the incoming line String at the incoming index.
     * Determines and returns how large the STRINGLITERAL Token is.
     *
     * @param currentLine Incoming line String.
     * @param currentIndex Incoming index.
     * @return How large the STRINGLITERAL Token is.
     * @throws SyntaxErrorException If the string literal was not closed.
     */
    private int handleStringLiteral(String currentLine, int currentIndex) throws SyntaxErrorException
    {
        char currentChar = ' ';
        String literalValue = "";
        for (int i = currentIndex + 1; i < currentLine.length() && currentChar != '\"'; i++)
        {
            currentChar = currentLine.charAt(i);
            if (currentChar != '\"')
            {
                literalValue += currentChar;
            }
            currentIndex = i;
        }
        if (currentLine.charAt(currentIndex) != '\"')
        {
            throw new SyntaxErrorException("Unclosed string literal on line " + lineNumber + ".");
        }
        tokenList.add(new Token(tokenType.STRINGLITERAL, literalValue, lineNumber));
        return currentIndex;
    }

    /**
     * Creates a Token of type CHARLITERAL starting in the incoming line String at the incoming index.
     * Determines and returns how large the CHARLITERAL Token is.
     * As of 1/7/2024, the maximum length for a CHARLITERAL is 3, both single quotes included.
     * If and when escape characters are built in, this must change.
     *
     * @param currentLine Incoming line String.
     * @param currentIndex Incoming index.
     * @return How large the CHARLITERAL Token is.
     * @throws Exception If the character literal was not closed.
     */
    private int handleCharacterLiteral(String currentLine, int currentIndex) throws Exception
    {
        final int TOTAL_CHARACTER_LITERAL_LENGTH = 3;
        char currentChar = currentLine.charAt(currentIndex + 1);
        if (peekAhead(currentLine, currentIndex + TOTAL_CHARACTER_LITERAL_LENGTH - 1) != '\'')
        {
            throw new SyntaxErrorException("Unenclosed or too large character literal on line " + lineNumber + ".");
        }
        else
        {
            tokenList.add(new Token(tokenType.CHARLITERAL, currentChar + "", lineNumber));
            return currentIndex + TOTAL_CHARACTER_LITERAL_LENGTH;
        }
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
     * Gets and returns the character in the incoming line String that is ahead by the incoming int.
     *
     * @param currentLine Incoming line String.
     * @param nextIndex Incoming int.
     * @return Character in currentLine that is nextIndex characters ahead.
     * @throws Exception If nextIndex is greater than currentLine's length.
     */
    private char peekAhead(String currentLine, int nextIndex) throws Exception
    {
        if (nextIndex > currentLine.length())
        {
            throw new Exception("Peeked too far ahead.");
        }
        return currentLine.charAt(nextIndex);
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
     * Checks and returns if the incoming character is contained in the punctuation HashMap.
     *
     * @param currentChar Incoming character.
     * @return True if currentChar is contained in the punctuation HashMap.
     */
    private boolean isPunctuation(char currentChar)
    {
        return punctuationMap.containsKey(currentChar + "");
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
     * Adds trailing dedents to the end of a program according to how many indents are left at the final line.
     */
    private void handleTrailingDedents()
    {
        while (currentIndentLevel > 0)
        {
            tokenList.add(new Token(tokenType.DEDENT, lineNumber));
            currentIndentLevel--;
        }
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
        punctuationMap.put("~", tokenType.NEGATE);

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
