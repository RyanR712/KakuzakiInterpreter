/**
 * Parses an ArrayList of Tokens into a series of Nodes in an abstract syntax tree.
 */

package ParserTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import ParserTools.Nodes.ASTNode;
import CrossStageTools.Token;
import CrossStageTools.tokenType;
import Exceptions.SyntaxErrorException;
import ParserTools.Nodes.DataTypeNodes.IntegerNode;
import ParserTools.Nodes.DataTypeNodes.RealNode;
import ParserTools.Nodes.MathOpNode;
import ParserTools.Nodes.MathOpNode.operationType;

public class Parser
{
    private ArrayList<Token> tokenList;
    private ArrayList<ASTNode> nodeList;

    private int lineNumber;

    public Parser(ArrayList<Token> incomingTokenList)
    {
        tokenList = incomingTokenList;
        nodeList = new ArrayList<>();

        lineNumber = 1;
    }

    /**
     * Produces and adds ASTNodes to this Parser's ASTNode list so long as there are ASTNodes to add.
     *
     * @throws SyntaxErrorException If any methods in the call stack throw Exceptions.
     */
    public void parse() throws SyntaxErrorException
    {
        ASTNode nodeToAdd;
        while (!tokenList.isEmpty() && (nodeToAdd = expression()) != null)
        {
            nodeList.add(nodeToAdd);
            expectOneOrMoreEOLs();
        }
    }

    /**
     * Returns the Token list.
     *
     * @return Token list.
     */
    public ArrayList<ASTNode> getNodeList()
    {
        return nodeList;
    }

    /**
     * Writes a formatted String of this Parser's ASTNodes to a .txt File in /ParserDumps.
     *
     * @throws IOException If a File cannot be created.
     */
    public void writeDebugOutput() throws IOException
    {
        ZonedDateTime zdt = ZonedDateTime.now();
        String formattedOutput = zdt.getDayOfMonth() + "-" + zdt.getMonthValue() + "-" + zdt.getYear() + "@" +
                zdt.getHour() + "_" + zdt.getMinute() + "_" + zdt.getSecond();
        File file = new File("./src/Debug/ParserDumps", "parser_" + formattedOutput + ".txt");
        if (file.createNewFile())
        {
            FileWriter fw = new FileWriter(file);

            fw.write(formatNodes());

            fw.close();
        }
    }

    /**
     * Formats and returns this Parser's ASTNodes in a sensible manner.
     *
     * @return Formatted ASTNodes in one String.
     */
    private String formatNodes()
    {
        String dumpString = "";
        boolean isLineNumberPrinted = false;

        for (int i = 0; i < nodeList.size(); i++)
        {
            if (!isLineNumberPrinted)
            {
                dumpString += nodeList.get(i).getLineNumber() + "\t";
                isLineNumberPrinted = true;
            }
            dumpString += nodeList.get(i) + " ";
            if (nodeList.size() > 1 && nodeList.get(i).getLineNumber() < nodeList.get(i + 1).getLineNumber())
            {
                isLineNumberPrinted = false;
            }
        }
        return dumpString;
    }

    /**
     * Removes and returns an expression ASTNode.
     * An expression is a number or a MathOpNode with addition or subtraction as its operation type.
     *
     * @return Expression ASTNode.
     */
    private ASTNode expression() throws SyntaxErrorException
    {
        ASTNode leftOperand = term();

        if (leftOperand == null)
        {
            return null;
        }
        if (peek(0).getType() == tokenType.EOL)
        {
            return leftOperand;
        }

        operationType opType = handleExpressionOperator();
        ASTNode rightOperand = expression();

        if (opType == null || rightOperand == null)
        {
            return leftOperand;
        }
        else
        {
            return new MathOpNode(leftOperand, opType, rightOperand, lineNumber);
        }
    }

    /**
     * Removes and returns an operationType on the expression priority.
     * The expression priority operators include addition and subtraction.
     *
     * @return operationType on the expression priority or null if no such operator is found.
     */
    private operationType handleExpressionOperator()
    {
        if (matchAndRemove(tokenType.ADD) != null)
        {
            return operationType.ADD;
        }
        else if (matchAndRemove(tokenType.MINUS) != null)
        {
            return operationType.SUB;
        }
        else
        {
            return null;
        }
    }

    /**
     * Removes and returns a term ASTNode.
     * A term is a number or a MathOpNode with multiplication, division or modularity as its operation type.
     *
     * @return Term ASTNode.
     */
    private ASTNode term() throws SyntaxErrorException
    {
        ASTNode leftOperand = factor();
        operationType opType = handleTermOperator();
        ASTNode rightOperand = factor();

        if (leftOperand == null)
        {
            return null;
        }
        else if (opType == null)
        {
            return leftOperand;
        }
        else if (peek(0).getType() == tokenType.MULT || peek(0).getType() == tokenType.DIV || peek(0).getType() == tokenType.MOD)
        {
            //fix this style when you are less tired please
            return new MathOpNode(new MathOpNode(leftOperand, opType, rightOperand, lineNumber), handleTermOperator(), term(), lineNumber);
        }

        return new MathOpNode(leftOperand, opType, rightOperand, lineNumber);
    }

    /**
     * Removes and returns an operationType on the term priority.
     * The term priority operators include multiplication, division and modularity.
     *
     * @return operationType on the term priority or null if no such operator is found.
     */
    private operationType handleTermOperator()
    {
        if (matchAndRemove(tokenType.MULT) != null)
        {
            return operationType.MULT;
        }
        else if (matchAndRemove(tokenType.DIV) != null)
        {
            return operationType.DIV;
        }
        else if (matchAndRemove(tokenType.MOD) != null)
        {
            return operationType.MOD;
        }
        else
        {
            return null;
        }
    }

    /**
     * Removes and returns a factor ASTNode.
     * A factor is a number or a parenthesized expression.
     *
     * @return Factor ASTNode.
     */
    private ASTNode factor() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.LPAREN) != null)
        {
            ASTNode parenthesizedExpressionNode = expression();

            if (matchAndRemove(tokenType.RPAREN) == null)
            {
                throw new SyntaxErrorException("Unenclosed parenthesized expression while parsing on line "
                                                + lineNumber + "."
                                                );
            }

            return parenthesizedExpressionNode;
        }

        int negativeMultiplier = 1;

        if (matchAndRemove(tokenType.NEGATE) != null)
        {
            negativeMultiplier = -1;
        }

        if (peek(0).getType() == tokenType.NUMBER)
        {
            return determineAndCreateNumberNode(negativeMultiplier);
        }
        else
        {
            return null;
        }
    }

    /**
     * Creates and returns a RealNode or IntegerNode depending on the next Token's value
     * and negates its value according to the incoming multiplier.
     *
     * @param negativeMultiplier Incoming multiplier.
     * @return RealNode or IntegerNode depending on the Token's value.
     */
    private ASTNode determineAndCreateNumberNode(int negativeMultiplier)
    {
        Token numberToken;
        try
        {
            numberToken = matchAndRemove(tokenType.NUMBER);
        }
        catch (Exception e)
        {
            System.out.println("No NUMBER token found as factor on line " + lineNumber + ".");
            return null;
        }

        return numberToken.isRealNumber() ? new RealNode(Float.parseFloat(
                                                    numberToken.getValue()) * negativeMultiplier, lineNumber
                                            ) :
                                            new IntegerNode(Integer.parseInt(
                                                    numberToken.getValue()) * negativeMultiplier, lineNumber
                                            );
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
        if (peek(0).getType() != tokenType.EOL)
        {
            throw new SyntaxErrorException(
                    "Expected EOL on line " + lineNumber + " but found " + peek(0).getType() + ".");
        }
        while (!tokenList.isEmpty() && matchAndRemove(tokenType.EOL) != null)
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
