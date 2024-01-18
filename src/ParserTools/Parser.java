/**
 * Parses an ArrayList of Tokens into a series of Nodes in an abstract syntax tree.
 */

//TODO: Extract line numbers in structure constructors from the comparison?

package ParserTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import CrossStageTools.Token;
import CrossStageTools.tokenType;
import Exceptions.SyntaxErrorException;
import ParserTools.Nodes.ASTNode;
import ParserTools.Nodes.DataTypeNodes.*;
import ParserTools.Nodes.MathOpNode;
import ParserTools.Nodes.StructureNodes.*;
import ParserTools.Nodes.StructureNodes.LoopNodes.*;
import ParserTools.Nodes.StructureNodes.StatementNodes.*;

public class Parser
{
    private final ArrayList<Token> tokenList;
    private final ArrayList<ASTNode> nodeList;

    private ProgramNode program;

    private int lineNumber;

    /**
     * Initializes a Parser with the incoming Token ArrayList, a new ASTNode ArrayList and a lineNumber of 1.
     *
     * @param incomingTokenList incoming Token ArrayList.
     */
    public Parser(ArrayList<Token> incomingTokenList)
    {
        tokenList = incomingTokenList;
        nodeList = new ArrayList<>();

        lineNumber = 1;
    }

    /**
     * Creates and returns a ProgramNode.
     *
     * @throws SyntaxErrorException If any methods in the call stack throw Exceptions.
     * @see "https://github.com/RyanR712/KakuzakiInterpreter/wiki for detailed language definition."
     */
    public ProgramNode parse() throws SyntaxErrorException
    {
        program = new ProgramNode(new HashMap<>());
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

            fw.write(program.toString());

            fw.close();
        }
    }

    /**
     * Creates and returns one FunctionNode with parameters, variables, constants and statements in its scope.
     *
     * @return FunctionNode with parameters, variables, constants and statements in its scope.
     * @throws SyntaxErrorException If the Kakuzaki syntax for functions is violated.
     */
    private FunctionNode function() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.DEFINE) == null)
        {
            return null;
        }

        ArrayList<VariableNode> parameters, variables = new ArrayList<>();

        String functionName = matchAndRemoveAndGetValueAndTestForException();

        int definitionLineNumber = lineNumber;

        parameters = handleParameters();

        while (peek(0).getType() != tokenType.INDENT)
        {
            variables.addAll(peek(0).getType() == tokenType.CONSTANTS ? handleConstants() : handleVariables());
        }

        expectZeroOrMoreEOLs();

        ArrayList<StatementNode> statements = handleStatements();

        return new FunctionNode(statements, parameters, variables, functionName, definitionLineNumber);
    }

    /**
     * Returns all the parameters, if any, in a function's definition.
     *
     * @return Parameters in a function's definition.
     * @throws SyntaxErrorException If the Kakuzaki syntax for parameters is violated.
     */
    private ArrayList<VariableNode> handleParameters() throws SyntaxErrorException
    {
        matchAndRemoveAndTestForException(tokenType.LPAREN,
                "Left parenthesis in function definition expected on line " + lineNumber);

        ArrayList<VariableNode> parameters = new ArrayList<>();

        while (peek(0).getType() == tokenType.IDENTIFIER)
        {
            parameters.add(handleParameter());
        }

        matchAndRemoveAndTestForException(tokenType.RPAREN,
                "Right parenthesis in function definition expected on line " + lineNumber);

        expectOneOrMoreEOLs();

        return parameters;
    }

    /**
     * Returns one parameter.
     *
     * @return One parameter.
     * @throws SyntaxErrorException If the Kakuzaki syntax is violated for semicolons in a function definition.
     */
    private VariableNode handleParameter() throws SyntaxErrorException
    {
        String name = matchAndRemove(tokenType.IDENTIFIER).getValue();

        matchAndRemoveAndTestForException(tokenType.COLON,
                "Expected COLON Token between parameter identifier and type declaration on line " + lineNumber);

        tokenType variableType = matchAndRemoveAndGetDataTypeAndTestForException();

        if (peek(0).getType() == tokenType.SEMICOLON && peek(1).getType() != tokenType.IDENTIFIER)
        {
            throw new SyntaxErrorException("Expected IDENTIFIER Token after SEMICOLON Token on " + lineNumber);
        } else
        {
            matchAndRemove(tokenType.SEMICOLON);
            return new VariableNode(name, variableType, lineNumber, true);
        }
    }

    /**
     * Returns all the constant declarations on one line.
     *
     * @return ArrayList containing all the constant declarations on one line.
     * @throws SyntaxErrorException If the Kakuzaki syntax for constants is violated.
     */
    private ArrayList<VariableNode> handleConstants() throws SyntaxErrorException
    {
        matchAndRemoveAndTestForException(tokenType.CONSTANTS, "Expected CONSTANTS Token on line " + lineNumber);

        ArrayList<VariableNode> constants = new ArrayList<>();

        while (peek(1).getType() == tokenType.COMMA)
        {
            constants.add(handleConstantOrVariable(false));
            matchAndRemove(tokenType.COMMA);
        }

        constants.add(handleConstantOrVariable(false));

        matchAndRemoveAndTestForException(tokenType.EQUAL,
                "Expected EQUAL Token after IDENTIFIER on line " + lineNumber);

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

    /**
     * Returns all the variable declarations on one line.
     *
     * @return ArrayList containing all the variable declarations on one line.
     * @throws SyntaxErrorException If the Kakuzaki syntax for variables is violated.
     */
    private ArrayList<VariableNode> handleVariables() throws SyntaxErrorException
    {
        matchAndRemoveAndTestForException(tokenType.VARIABLES, "Expected VARIABLES Token on line " + lineNumber);

        ArrayList<VariableNode> variables = new ArrayList<>();

        tokenType dataType;

        while (!tokenList.isEmpty() && peek(1).getType() == tokenType.COMMA)
        {
            variables.add(handleConstantOrVariable(true));
            matchAndRemove(tokenType.COMMA);
        }

        variables.add(handleConstantOrVariable(true));

        matchAndRemoveAndTestForException(tokenType.COLON,
                "Expected COLON after all variables on line " + lineNumber);

        dataType = matchAndRemoveAndGetDataTypeAndTestForException();

        boolean hasRange = false;

        ASTNode lowerRange = null, higherRange = null;

        if (dataType == tokenType.INTEGER || dataType == tokenType.REAL || dataType == tokenType.STRING)
        {
            if (matchAndRemove(tokenType.FROM) != null)
            {
                hasRange = true;

                lowerRange = expression();

                matchAndRemoveAndTestForException(tokenType.TO, "Expected TO Token after FROM Token");

                higherRange = expression();
            }
        }

        VariableNode currentVariable;

        for (int i = 0; i < variables.size(); i++)
        {
            currentVariable = variables.get(i);

            currentVariable.setType(dataType);

            if (hasRange)
            {
                currentVariable.setRangedAsTrue();
                currentVariable.setLowerRange(lowerRange);
                currentVariable.setHigherRange(higherRange);
            }
        }

        expectOneOrMoreEOLs();

        return variables;
    }

    /**
     * Creates and returns a VariableNode, which is variable or constant depending on the incoming boolean.
     * If the boolean is true, this is a variable. If the boolean is false, this is a constant.
     *
     * @param isChangeable Incoming boolean.
     * @return VariableNode with appropriate changeability.
     * @throws SyntaxErrorException If matchAndRemoveAndGetValueAndTestForException() throws a SyntaxErrorException.
     */
    private VariableNode handleConstantOrVariable(boolean isChangeable) throws SyntaxErrorException
    {
        return new VariableNode(matchAndRemoveAndGetValueAndTestForException(), null, lineNumber, isChangeable);
    }

    /**
     * Expects an INDENT Token, adds a list of StatementNodes to a function, and expects a DEDENT Token.
     *
     * @return A list of StatementNodes.
     * @throws SyntaxErrorException If either expected indentation is incorrect.
     */
    private ArrayList<StatementNode> handleStatements() throws SyntaxErrorException
    {
        matchAndRemoveAndTestForException(tokenType.INDENT, "Indent expected on line " + lineNumber);

        ArrayList<StatementNode> statements = new ArrayList<>();

        while (peek(0).getType() != tokenType.DEDENT)
        {
            statements.add(handleStatement());
            expectZeroOrMoreEOLs();
        }

        matchAndRemoveAndTestForException(tokenType.DEDENT, "Dedent expected on line " + lineNumber);

        return statements;
    }

    /**
     * Handles the processing of one StatementNode.
     *
     * @return One StatementNode.
     * @throws SyntaxErrorException If the parsing of some particular StatementNode fails.
     */
    private StatementNode handleStatement() throws SyntaxErrorException
    {
        if (peek(0).getType() == tokenType.IF)
        {
            return handleIf(false);
        }
        else if (peek(0).getType() == tokenType.WHILE)
        {
            return handleWhile();
        }
        else if (peek(0).getType() == tokenType.REPEAT)
        {
            return handleRepeat();
        }
        else if (peek(0).getType() == tokenType.FOR)
        {
            return handleFor();
        }
        else if (peek(0).getType() == tokenType.IDENTIFIER)
        {
            if (peek(1).getType() == tokenType.ASSIGN)
            {
                return handleAssignment();
            }
            else
            {
                return handleFunctionCall();
            }
        }
        return null;
    }

    /**
     * Creates and returns one IfNode with a conditional, a list of statements
     * and a potential ELSIF/ELSE chain based on the incoming boolean,
     * which determines if this particular call is in a chained IF statement already.
     *
     * @param isChained Incoming boolean.
     * @return IfNode with the above items.
     * @throws SyntaxErrorException If the IF, ELSIF and ELSE Tokens are not ordered appropriately.
     */
    private IfNode handleIf(boolean isChained) throws SyntaxErrorException
    {
        int statedLineNumber = lineNumber;

        if (matchAndRemove(tokenType.ELSE) == null)
        {
            if (!isChained)
            {
                matchAndRemoveAndTestForException(tokenType.IF, "Expected IF Token on line " + statedLineNumber);
            }
            else
            {
                matchAndRemoveAndTestForException(tokenType.ELSIF, "Expected ELSIF Token on line " + statedLineNumber);
            }

            BooleanCompareNode conditional = (BooleanCompareNode)booleanCompare();

            expectOneOrMoreEOLs();

            ArrayList<StatementNode> statements = handleStatements();

            return new IfNode(conditional, statements,
                    (peekAndGetType(0) == tokenType.ELSIF || peekAndGetType(0) == tokenType.ELSE
                            ? handleIf(true) : null), statedLineNumber);
        }

        expectOneOrMoreEOLs();

        return new IfNode(handleStatements(), statedLineNumber);
    }

    /**
     * Creates and returns a WhileNode with a conditional statement and a list of statements.
     *
     * @return WhileNode with above items.
     * @throws SyntaxErrorException If a WHILE Token is not found.
     */
    private WhileNode handleWhile() throws SyntaxErrorException
    {
        int statedLineNumber = lineNumber;

        matchAndRemoveAndTestForException(tokenType.WHILE, "Expected WHILE Token on line " + statedLineNumber);

        BooleanCompareNode conditional = (BooleanCompareNode)booleanCompare();

        expectOneOrMoreEOLs();

        ArrayList<StatementNode> statements = handleStatements();

        return new WhileNode(conditional, statements, statedLineNumber);
    }

    /**
     * Creates and returns a RepeatNode with a conditional statement and a list of statements.
     *
     * @return RepeatNode with above items.
     * @throws SyntaxErrorException If a REPEAT Token and then an UNTIL Token are not found in that order.
     */
    private RepeatNode handleRepeat() throws SyntaxErrorException
    {
        int statedLineNumber = lineNumber;

        matchAndRemoveAndTestForException(tokenType.REPEAT, "Expected REPEAT Token on line " + statedLineNumber);

        matchAndRemoveAndTestForException(tokenType.UNTIL, "Expected UNTIL Token on line " + statedLineNumber);

        BooleanCompareNode conditional = (BooleanCompareNode)booleanCompare();

        expectOneOrMoreEOLs();

        ArrayList<StatementNode> statements = handleStatements();

        return new RepeatNode(conditional, statements, statedLineNumber);
    }

    /**
     * Creates and returns a ForNode with an iterator variable, a lower bound on that iterator,
     * an upper bound on that iterator and a list of statements.
     *
     * @return ForNode with above items.
     * @throws SyntaxErrorException If a FOR Token, a FROM Token, and a TO Token are not found in that order.
     */
    private ForNode handleFor() throws SyntaxErrorException
    {
        int statedLineNumber = lineNumber;

        matchAndRemoveAndTestForException(tokenType.FOR, "Expected FOR Token on line " + statedLineNumber);

        VariableReferenceNode iterator = handleVariableReferenceNode(); //TODO: this means you could put an array here

        matchAndRemoveAndTestForException(tokenType.FROM, "Expected FROM Token on line " + statedLineNumber);

        ASTNode fromNode = expression();

        matchAndRemoveAndTestForException(tokenType.TO, "Expected TO Token on line " + statedLineNumber);

        ASTNode toNode = expression();

        expectOneOrMoreEOLs();

        ArrayList<StatementNode> statements = handleStatements();

        return new ForNode(iterator, fromNode, toNode, statements, lineNumber);
    }

    /**
     * Creates and returns a FunctionCallNode with a String identifier and a list of arguments.
     *
     * @return FunctionCallNode with above items.
     * @throws SyntaxErrorException If a COMMA Token does not separate each argument in a list of arguments.
     */
    private FunctionCallNode handleFunctionCall() throws SyntaxErrorException
    {
        String calledName = matchAndRemoveAndGetValueAndTestForException();

        ArrayList<ArgumentNode> arguments = new ArrayList<>();

        while (!isLineEmpty())
        {
            arguments.add(handleArgument());

            if (peekAndGetType(0) != tokenType.EOL)
            {
                matchAndRemoveAndTestForException(tokenType.COMMA,
                        "COMMA Token expected for multiple arguments on line " + lineNumber);
            }
        }

        expectOneOrMoreEOLs();

        return new FunctionCallNode(calledName, arguments, lineNumber);
    }

    /**
     * Creates and returns one ArgumentNode, either variable or constant, used in a FunctionCallNode.
     *
     * @return Either variable or constant ArgumentNode.
     * @throws SyntaxErrorException If a VAR Token was found but no following VariableReferenceNode.
     */
    private ArgumentNode handleArgument() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.VAR) != null)
        {
            if (peekAndGetType(0) != tokenType.IDENTIFIER)
            {
                throw new SyntaxErrorException(
                        "VAR Token found on line " + lineNumber + " but was followed by a constant.");
            }
            return new ArgumentNode(handleVariableReferenceNode(), lineNumber);
        }
        else
        {
            return new ArgumentNode(booleanCompare(), lineNumber);
        }
    }

    /**
     * Creates and returns an AssignmentNode with a target VariableReferenceNode and an assignment value.
     *
     * @return AssignmentNode with appropriate target VariableReferenceNode and assignment value.
     * @throws SyntaxErrorException If no ASSIGN Token was found between the target and value.
     */
    private AssignmentNode handleAssignment() throws SyntaxErrorException
    {
        VariableReferenceNode referencedNode = handleVariableReferenceNode();

        matchAndRemoveAndTestForException(tokenType.ASSIGN, "ASSIGN Token expected after variable reference on line " +
                                            lineNumber);

        ASTNode assignmentValue = booleanCompare();

        expectOneOrMoreEOLs();

        return new AssignmentNode(referencedNode, assignmentValue, lineNumber);
    }

    /**
     * Matches and removes the incoming tokenType, and if that tokenType is not found,
     * throws a SyntaxErrorException with the incoming String as its message.
     *
     * @param type Incoming tokenType.
     * @param message Incoming String.
     * @throws SyntaxErrorException If type was not found.
     */
    private void matchAndRemoveAndTestForException(tokenType type, String message) throws SyntaxErrorException
    {
        if (matchAndRemove(type) == null)
        {
            throw new SyntaxErrorException(message + ", but found " + peek(0) + " on line " + lineNumber + ".");
        }
    }

    /**
     * Matches and removes an IDENTIFIER Token and returns its value.
     *
     * @return Removed IDENTIFIER Token's value.
     * @throws SyntaxErrorException If no IDENTIFIER Token was found.
     */
    private String matchAndRemoveAndGetValueAndTestForException() throws SyntaxErrorException
    {
        String name;

        if ((name = matchAndRemove(tokenType.IDENTIFIER).getValue()) == null)
        {
            throw new SyntaxErrorException("Expected IDENTIFIER Token on line " + lineNumber + " but found " + peek(0)
                    + ".");
        }

        return name;
    }

    /**
     * Matches, removes and returns a data type tokenType.
     *
     * @return Data type tokenType.
     * @throws SyntaxErrorException If no such tokenType was found.
     */
    private tokenType matchAndRemoveAndGetDataTypeAndTestForException() throws SyntaxErrorException
    {
        tokenType dataType;

        if ((dataType = matchAndRemoveDataTypeToken()) == null)
        {
            throw new SyntaxErrorException("Expected data type tokenType on line " + lineNumber);
        }

        return dataType;
    }

    /**
     * Matches, removes and returns one of the following tokenTypes and returns that type.
     * INTEGER, REAL, CHARACTER, STRING, BOOLEAN
     *
     * @return One of the above tokenTypes. Null if none of them were found.
     */
    private tokenType matchAndRemoveDataTypeToken()
    {
        Token dataTypeToken;
        if ((dataTypeToken = matchAndRemove(tokenType.INTEGER)) != null ||
                (dataTypeToken = matchAndRemove(tokenType.REAL)) != null ||
                (dataTypeToken = matchAndRemove(tokenType.CHARACTER)) != null ||
                (dataTypeToken = matchAndRemove(tokenType.STRING)) != null ||
                (dataTypeToken = matchAndRemove(tokenType.BOOLEAN)) != null)
        {
            return dataTypeToken.getType();
        }
        else return null;
    }

    /**
     * Creates and returns a BooleanCompareNode with two comparands and a comparison operand.
     *
     * @return A BooleanCompareNode with appropriately matched and removed comparands and operand.
     * @throws SyntaxErrorException If retrieving either comparand fails.
     */
    private ASTNode booleanCompare() throws SyntaxErrorException
    {
        ASTNode leftComparand = expression();

        if (isLineEmpty() || peekAndGetType(0) == tokenType.COMMA)
        {
            return leftComparand;
        }

        tokenType compType = matchAndRemoveBooleanComparison();

        ASTNode rightComparand = expression();

        return new BooleanCompareNode(leftComparand, compType, rightComparand, lineNumber);
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
        if (peekAndGetType(0) == tokenType.EOL)
        {
            return leftOperand;
        }

        tokenType opType;

        if ((opType = matchAndRemoveBooleanComparison()) != null)
        {
            return new BooleanCompareNode(leftOperand, opType, expression(), lineNumber);
        }
        else
        {
            opType = handleExpressionOperator();
        }

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
    private tokenType handleExpressionOperator() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.ADD) != null)
        {
            return tokenType.ADD;
        }
        else if (matchAndRemove(tokenType.MINUS) != null)
        {
            return tokenType.MINUS;
        }
        else if (matchAndRemove(tokenType.NEGATE) != null)
        {
            throw new SyntaxErrorException("Stray negation operator found on line " + lineNumber + ".");
        }
        else
        {
            return matchAndRemoveBooleanComparison();
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
        tokenType opType = handleTermOperator();
        ASTNode rightOperand = factor();

        if (leftOperand == null)
        {
            return null;
        }
        else if (opType == null)
        {
            return leftOperand;
        }
        else if (peekAndGetType(0) == tokenType.MULT || peekAndGetType(0) == tokenType.DIV || peekAndGetType(0) == tokenType.MOD)
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
    private tokenType handleTermOperator() throws SyntaxErrorException
    {
        if (matchAndRemove(tokenType.MULT) != null)
        {
            return tokenType.MULT;
        }
        else if (matchAndRemove(tokenType.DIV) != null)
        {
            return tokenType.DIV;
        }
        else if (matchAndRemove(tokenType.MOD) != null)
        {
            return tokenType.MOD;
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
                                                + lineNumber);
            }

            return parenthesizedExpressionNode;
        }

        int negativeMultiplier = matchAndRemoveNegation();

        if (peekAndGetType(0) == tokenType.NUMBER)
        {
            return determineAndCreateNumberNode(negativeMultiplier);
        }
        else if (peekAndGetType(0) == tokenType.IDENTIFIER)
        {
            return handleVariableReferenceNode();
        }
        else return null;
    }

    /**
     * Creates and returns a VariableReferenceNode with an optional array expression and following bracket removal.
     *
     * @return VariableReferenceNode with a name and optional array expression.
     * @throws SyntaxErrorException If no RBRACK Token is removed after an LBRACK Token is found.
     */
    private VariableReferenceNode handleVariableReferenceNode() throws SyntaxErrorException
    {
        String referenceName = matchAndRemoveAndGetValueAndTestForException();

        ASTNode arrayExpression = null;

        if (matchAndRemove(tokenType.LBRACK) != null)
        {
            arrayExpression = expression();
            matchAndRemoveAndTestForException(tokenType.RBRACK,
                                "Expected RBRACK Token after array indexing on line " + lineNumber);
        }

        return new VariableReferenceNode(arrayExpression, referenceName, lineNumber);
    }

    /**
     * Matches and removes one of the following tokenTypes and returns that type.
     * GTHAN, LTHAN, GETO, LETO, EQUAL, NEQUAL, NOT, AND, OR
     *
     * @return One of the above tokenTypes. Null if none of them were found.
     */
    private tokenType matchAndRemoveBooleanComparison()
    {
        Token comparisonTypeToken;
        if ((comparisonTypeToken = matchAndRemove(tokenType.GTHAN)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.LTHAN)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.GETO)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.LETO)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.EQUAL)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.NEQUAL)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.NOT)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.AND)) != null ||
            (comparisonTypeToken = matchAndRemove(tokenType.OR)) != null)
        {
            return comparisonTypeToken.getType();
        }
        else return null;
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
        if (peekAndGetType(0) != tokenType.EOL)
        {
            throw new SyntaxErrorException(
                    "Expected EOL on line " + lineNumber + ", but found " + peek(0).getType() + ".");
        }
        expectZeroOrMoreEOLs();
    }

    /**
     * Matches and removes zero or more EOL Tokens.
     * Used where there could be optional whitespace.
     */
    private void expectZeroOrMoreEOLs()
    {
        while (!tokenList.isEmpty() && matchAndRemove(tokenType.EOL) != null)
        {
            lineNumber++;
        }
    }

    /**
     * Checks and returns if the current Token is of type EOL.
     * Since the list of Tokens is not ordered in terms of lines, each EOL Token must designate this.
     * Therefore, in this model, this is the last token in an empty line.
     *
     * @return If the current Token is of type EOL.
     */
    private boolean isLineEmpty()
    {
        return peekAndGetType(0) == tokenType.EOL;
    }

    /**
     * Returns the tokenType at the incoming index.
     *
     * @param index Incoming index.
     * @return tokenType at index.
     */
    private tokenType peekAndGetType(int index)
    {
        return peek(index).getType();
    }

    /**
     * Returns the Token at the incoming index.
     *
     * @param index Incoming index.
     * @return Token at index.
     */
    private Token peek(int index)
    {
        return !tokenList.isEmpty() ? tokenList.get(index) : new Token(tokenType.NONE, -1);
    }

    /**
     * Formats and returns this Parser's ASTNodes in a sensible manner.
     *
     * @return Formatted ASTNodes in one String.
     * @deprecated As Parser.java is no longer based on an ASTNode ArrayList.
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
}
