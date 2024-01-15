/**
 * Parses an ArrayList of Tokens into a series of Nodes in an abstract syntax tree.
 */

package ParserTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import ParserTools.Nodes.ASTNode;
import CrossStageTools.Token;
import CrossStageTools.tokenType;
import Exceptions.SyntaxErrorException;
import ParserTools.Nodes.DataTypeNodes.IntegerNode;
import ParserTools.Nodes.DataTypeNodes.RealNode;
import ParserTools.Nodes.MathOpNode;
import ParserTools.Nodes.MathOpNode.operationType;
import ParserTools.Nodes.StructureNodes.FunctionNode;
import ParserTools.Nodes.StructureNodes.ProgramNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.StatementNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.VariableNode;

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
    public ProgramNode parse() throws SyntaxErrorException
    {
        ProgramNode program = new ProgramNode(new HashMap<>());
        FunctionNode functionToAdd;
        while (!tokenList.isEmpty() && (functionToAdd = function()) != null)
        {
            program.addFunction(functionToAdd);
        }

        return program;
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

    private FunctionNode function() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.DEFINE) == null)
        {
            return null;
        }

        ArrayList<StatementNode> statements = null;
        ArrayList<VariableNode> parameters = null, variables = new ArrayList<>();

        String functionName;

        functionName = matchAndRemove(tokenType.IDENTIFIER).getValue();

        parameters = handleParameters();

        while (peek(0).getType() != tokenType.INDENT)
        {
            variables.addAll(peek(0).getType() == tokenType.CONSTANTS ? handleConstants() : handleVariables());
        }

        if (matchAndRemove(tokenType.INDENT) == null)
        {
            throw new SyntaxErrorException("Indent expected on line " + lineNumber + ".");
        }

        ASTNode expressionNode;

        while ((expressionNode = expression()) != null)
        {
            System.out.println(expressionNode);
            expectOneOrMoreEOLs();
        }

        if (matchAndRemove(tokenType.DEDENT) == null)
        {
            throw new SyntaxErrorException("Dedent expected on line " + lineNumber + ".");
        }

        return new FunctionNode(null, parameters, variables, functionName, lineNumber);
    }

    //private ASTNode executeSeriesOfMatchAndRemoves(variadic?)

    private ArrayList<VariableNode> handleParameters() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.LPAREN) == null)
        {
            throw new SyntaxErrorException("Left parenthesis in function definition expected on line " + lineNumber
                                            + ".");
        }

        ArrayList<VariableNode> parameters = new ArrayList<>();

        while (peek(0).getType() == tokenType.IDENTIFIER)
        {
            parameters.add(handleParameter());
        }

        if (matchAndRemove(tokenType.RPAREN) == null)
        {
            throw new SyntaxErrorException("Right parenthesis in function definition expected on line " + lineNumber
                                            + ".");
        }

        expectOneOrMoreEOLs();

        return parameters;
    }

    private VariableNode handleParameter() throws SyntaxErrorException
    {
        String name;

        tokenType variableType;

        name = matchAndRemove(tokenType.IDENTIFIER).getValue();

        if (matchAndRemove(tokenType.COLON) == null)
        {
            throw new SyntaxErrorException("Expected COLON Token between parameter identifier and type declaration on line " + lineNumber +
                                            ".");
        }

        if ((variableType = matchAndRemoveDataTypeToken().getType()) == null)
        {
            throw new SyntaxErrorException("Expected a data type tokenType after COLON on " + lineNumber + ".");
        }

        if (peek(0).getType() == tokenType.SEMICOLON && peek(1).getType() != tokenType.IDENTIFIER)
        {
            throw new SyntaxErrorException("Expected IDENTIFIER Token after SEMICOLON Token on " + lineNumber + ".");
        }
        else
        {
            matchAndRemove(tokenType.SEMICOLON);
            return new VariableNode(name, variableType, lineNumber, true);
        }
    }

    /**
     * Matches, removes and returns one of the following tokenTypes.
     * INTEGER, REAL, CHARACTER, STRING, BOOLEAN
     *
     * @return One of the above tokenTypes. Null if none of them were found.
     */
    private Token matchAndRemoveDataTypeToken()
    {
        Token dataTypeToken;
        if ((dataTypeToken = matchAndRemove(tokenType.INTEGER)) != null ||
            (dataTypeToken = matchAndRemove(tokenType.REAL)) != null ||
            (dataTypeToken = matchAndRemove(tokenType.CHARACTER)) != null ||
            (dataTypeToken = matchAndRemove(tokenType.STRING)) != null ||
            (dataTypeToken = matchAndRemove(tokenType.BOOLEAN)) != null)
        {
            return dataTypeToken;
        }
        else return null;
    }

    private ArrayList<VariableNode> handleConstants() throws SyntaxErrorException
    {
        ArrayList<VariableNode> constants = new ArrayList<>();

        if (matchAndRemove(tokenType.CONSTANTS) == null)
        {
            throw new SyntaxErrorException("Expected CONSTANTS Token on line " + lineNumber + ".");
        }

        while (peek(1).getType() == tokenType.COMMA)
        {
            constants.add(handleConstant());
            matchAndRemove(tokenType.COMMA);
        }

        constants.add(handleConstant());

        if (matchAndRemove(tokenType.EQUAL) == null)
        {
            throw new SyntaxErrorException("Expected EQUAL Token after IDENTIFIER on line " + lineNumber + ".");
        }

        int negationMultiplier = matchAndRemoveNegation();
        String valueString = determineAndCreateNumberNode(negationMultiplier).toString();

        for (int i = 0; i < constants.size(); i++)
        {
            constants.get(i).setType(tokenType.NUMBER);
            constants.get(i).setValue(valueString);
        }

        expectOneOrMoreEOLs();

        return constants;
    }

    private VariableNode handleConstant() throws SyntaxErrorException
    {
        String name;
        if ((name = matchAndRemove(tokenType.IDENTIFIER).getValue()) == null)
        {
            throw new SyntaxErrorException("Expected IDENTIFIER Token after CONSTANTS Token on line " + lineNumber
                    + ".");
        }

        return new VariableNode(name, null, lineNumber, false);
    }

    private ArrayList<VariableNode> handleVariables() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.VARIABLES) == null)
        {
            throw new SyntaxErrorException("Expected VARIABLES Token on line " + lineNumber + ".");
        }

        ArrayList<VariableNode> variables = new ArrayList<>();

        tokenType dataType;

        while (peek(1).getType() == tokenType.COMMA)
        {
            variables.add(handleVariable());
            matchAndRemove(tokenType.COMMA);
        }

        variables.add(handleVariable());

        if (matchAndRemove(tokenType.COLON) == null)
        {
            throw new SyntaxErrorException("Expected COLON after all variables on line " + lineNumber + ".");
        }

        if ((dataType = matchAndRemoveDataTypeToken().getType()) == null)
        {
            throw new SyntaxErrorException("Expected data type tokenType after all variables on line " + lineNumber + ".");
        }

        for (int i = 0; i < variables.size(); i++)
        {
            variables.get(i).setType(dataType);
        }

        expectOneOrMoreEOLs();

        return variables;
    }

    private VariableNode handleVariable() throws SyntaxErrorException
    {
        String name;
        if ((name = matchAndRemove(tokenType.IDENTIFIER).getValue()) == null)
        {
            throw new SyntaxErrorException("Expected IDENTIFIER Token after VARIABLES Token on line " + lineNumber
                    + ".");
        }

        return new VariableNode(name, null, lineNumber, true);
        //TODO: Combine this and HandleConstant into one method that only differs in Syntax Error Exception message
        //and VariableNode(x, y, z, true | false).
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
    private operationType handleExpressionOperator() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.ADD) != null)
        {
            return operationType.ADD;
        }
        else if (matchAndRemove(tokenType.MINUS) != null)
        {
            return operationType.SUB;
        }
        else if (matchAndRemove(tokenType.NEGATE) != null)
        {
            throw new SyntaxErrorException("Stray negation operator found on line " + lineNumber + ".");
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
    private operationType handleTermOperator() throws SyntaxErrorException
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
        else if (matchAndRemove(tokenType.NEGATE) != null)
        {
            throw new SyntaxErrorException("Stray negation operator found on line " + lineNumber + ".");
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

        int negativeMultiplier = matchAndRemoveNegation();

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
     * Matches and removes a NEGATE Token, if one exists.
     *
     * @return -1 if a NEGATE Token was removed, 1 if a NEGATE Token was not removed.
     */
    private int matchAndRemoveNegation()
    {
        return matchAndRemove(tokenType.NEGATE) != null ? -1 : 1;
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
