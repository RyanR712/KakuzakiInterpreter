/**
 * Parses an ArrayList of Tokens into a series of Nodes in an abstract syntax tree.
 */

package ParserTools;

import java.util.ArrayList;

import ParserTools.Nodes.ASTNode;
import CrossStageTools.Token;
import CrossStageTools.tokenType;
import Exceptions.SyntaxErrorException;

public class Parser
{
    private ArrayList<Token> tokenList;

    private int lineNumber;

    public Parser(ArrayList<Token> incomingTokenList)
    {
        tokenList = incomingTokenList;
    }

    public ASTNode parse()
    {
        return null;
    }

    /**
     * Returns the Token list.
     *
     * @return Token list.
     */
    public ArrayList<Token> getTokenList()
    {
        return tokenList;
    }

    private ASTNode expression()
    {
        return null;
    }

    private ASTNode term()
    {
        return null;
    }

    private ASTNode factor()
    {
        return null;
    }

    /**
     * Checks the Token currently at index 0 in the Token list and removes and returns it,
     * if its tokenType matches the incoming tokenType.
     *
     * @param incomingType Incoming tokenType.
     * @return Token currently at index 0 in the Token list, null if the Token list is empty or the type doesn't match.
     */
    private Token matchAndRemove(tokenType incomingType)
    {
        if (!tokenList.isEmpty() && tokenList.get(0).getType() == incomingType)
        {
            return tokenList.remove(0);
        }
        else return null;
    }

    /**
     * Removes an arbitrarily long number of one or more EOL Tokens in a row.
     *
     * @throws SyntaxErrorException If zero EOL Tokens were found.
     */
    private void expectOneOrMoreEOLs() throws SyntaxErrorException
    {
        while (matchAndRemove(tokenType.EOL) != null)
        {
            lineNumber++;
        }
    }

    /**
     * Returns the Token at the incoming index.
     *
     * @param index Incoming index.
     * @return Token at index.
     */
    private Token peek(int index)
    {
        return tokenList.get(index);
    }
}
