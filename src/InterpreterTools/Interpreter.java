/**
 * Processes a program and produces an output.
 * Terminology: You may substitute "InterpreterDataType" when you see "IDT" in doc comments. Use that noggin!
 */

package InterpreterTools;

import java.util.ArrayList;
import java.util.HashMap;

import CrossStageTools.CrossStageNodes.*;
import CrossStageTools.tokenType;
import Exceptions.InvalidArgumentsException;
import Exceptions.NonexistantVariableException;
import Exceptions.SyntaxErrorException;
import Exceptions.UnchangeableVariableException;
import InterpreterTools.BuiltInFunctions.BuiltInFunctionNode;
import InterpreterTools.InterpreterDataTypes.*;
import ParserTools.Nodes.DataTypeNodes.*;
import ParserTools.Nodes.MathOpNode;
import ParserTools.Nodes.MathOpNode.operationType;
import ParserTools.Nodes.StructureNodes.ArgumentNode;
import ParserTools.Nodes.StructureNodes.FunctionCallNode;
import ParserTools.Nodes.StructureNodes.IfNode;
import ParserTools.Nodes.StructureNodes.LoopNodes.*;
import ParserTools.Nodes.StructureNodes.StatementNodes.AssignmentNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.BooleanCompareNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.BooleanCompareNode.comparisonType;
import ParserTools.Nodes.StructureNodes.StatementNodes.VariableReferenceNode;

public class Interpreter
{
    private enum typeCheckResult {FAILURE, INTEGER, REAL, STRING, CHARACTER, BOOLEAN}

    private final ProgramNode program;

    private final HashMap<String, FunctionNode> functionMap;

    /**
     * Creates an Interpreter with the incoming ProgramNode as the program already parsed and to be interpreted.
     *
     * @param parsedProgram Incoming ProgramNode.
     */
    public Interpreter(ProgramNode parsedProgram)
    {
        program = parsedProgram;
        functionMap = program.getFunctionMap();
    }

    /**
     * Interprets the function called "start" as the Kakuzaki program's entry point.
     *
     * @throws SyntaxErrorException If there is any error during interpreting.
     */
    public void interpret() throws SyntaxErrorException
    {
        interpretFunction(functionMap.get("start"), new ArrayList<>());
    }

    /**
     * Interprets the contents of the incoming FunctionNode with the incoming IDT ArrayList as its arguments.
     *
     * @param function Incoming FunctionNode.
     * @param arguments Incoming IDT ArrayList.
     * @return HashMap created after this function's execution is done.
     * @throws SyntaxErrorException If there is an error in interpreting this FunctionNode.
     */
    private HashMap<String, InterpreterDataType> interpretFunction(
            FunctionNode function, ArrayList<InterpreterDataType> arguments) throws SyntaxErrorException
    {
        HashMap<String, InterpreterDataType> localVariables = handleVariables(function);

        if (function instanceof BuiltInFunctionNode)
        {
            ((BuiltInFunctionNode)function).execute(arguments);
        }
        else
        {
            handleParameters(function, arguments, localVariables);
            interpretStatements(function.getStatementList(), localVariables);
        }

        return localVariables;
    }

    /**
     * Adds the parameters of the incoming FunctionNode, informed by the incoming IDT ArrayList,
     * to the incoming HashMap.
     *
     * @param function Incoming FunctionNode.
     * @param arguments Incoming IDT ArrayList.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If there is an error in creating the IDTs from function's parameter list.
     */
    private void handleParameters(
            FunctionNode function, ArrayList<InterpreterDataType> arguments,
            HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        ArrayList<VariableNode> parameters = function.getParameterList();

        for (int i = 0; i < parameters.size(); i++)
        {
            arguments.add(makeInterpreterDataTypeFromVariableNode(parameters.get(i)));
            variables.put(parameters.get(i).getName(), arguments.get(i));
        }
    }

    /**
     * Adds the variables and constants of the incoming FunctionNode to its variable HashMap.
     *
     * @param function Incoming FunctionNode.
     * @return HashMap with declared variables and constants added.
     */
    private HashMap<String, InterpreterDataType> handleVariables(FunctionNode function)
    {
        if (function instanceof BuiltInFunctionNode)
        {
            return null;
        }

        String functionName = function.getName();

        HashMap<String, InterpreterDataType> localVariables = new HashMap<>();

        ArrayList<VariableNode> variables = functionMap.get(functionName).getVariableList();

        for (int i = 0; i < variables.size(); i++)
        {
            VariableNode currentVariable = variables.get(i);
            localVariables.put(
                    currentVariable.getName(), handleDataType(currentVariable, currentVariable.isChangeable()));
        }

        return localVariables;
    }

    /**
     * Creates a new IDT according to the type of the incoming VariableNode
     * and determines if the IDT should have a default value based on the incoming boolean.
     *
     * @param currentVariable Incoming VariableNode.
     * @param isInitializer Incoming boolean.
     * @return IDT created according to VariableNode and isInitializer.
     */
    private InterpreterDataType handleDataType(VariableNode currentVariable, boolean isInitializer)
    {
        switch (currentVariable.getType())
        {
            case INTEGER   : return new IntegerDataType(currentVariable, isInitializer);
            case REAL      : return new RealDataType(currentVariable, isInitializer);
            case STRING    : return new StringDataType(currentVariable, isInitializer);
            case CHARACTER : return new CharacterDataType(currentVariable, isInitializer);
            case BOOLEAN   : return new BooleanDataType(currentVariable, isInitializer);
            case ARRAY     : return new ArrayDataType(currentVariable, isInitializer);
            default        : return null;
        }
    }

    /**
     * Loops over the incoming StatementNode ArrayList and interprets each according to the incoming HashMap.
     *
     * @param statements Incoming StatementNode ArrayList.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If there is an error in interpreting any of statements.
     */
    private void interpretStatements(ArrayList<StatementNode> statements,
                                     HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        StatementNode currentStatement;
        for (int i = 0; i < statements.size(); i++)
        {
            currentStatement = statements.get(i);
            if (currentStatement instanceof IfNode)
            {
                interpretIf((IfNode)currentStatement, variables);
            }
            else if (currentStatement instanceof ForNode)
            {
                interpretFor((ForNode)currentStatement, variables);
            }
            else if (currentStatement instanceof RepeatNode)
            {
                interpretRepeat((RepeatNode)currentStatement, variables);
            }
            else if (currentStatement instanceof WhileNode)
            {
                interpretWhile((WhileNode)currentStatement, variables);
            }
            else if (currentStatement instanceof AssignmentNode)
            {
                interpretAssignment((AssignmentNode)currentStatement, variables);
            }
            else if (currentStatement instanceof FunctionCallNode)
            {
                interpretFunctionCall((FunctionCallNode)currentStatement, variables);
            }
        }
    }

    /**
     * Executes the statements of the incoming IfNode using the values given by the incoming HashMap.
     *
     * @param ifBlock Incoming IfNode.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If there is an error in interpreting ifBlock's statements.
     */
    private void interpretIf(IfNode ifBlock, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        if (interpretBooleanCompare(ifBlock.getConditional(), variables))
        {
            interpretStatements(ifBlock.getStatements(), variables);
        }
        else if (ifBlock.hasNext())
        {
            interpretIf(ifBlock.getNext(), variables);
        }
    }

    /**
     * Executes the statements of the incoming ForNode using the values given by the incoming HashMap.
     * Note that the meta for loop runs while the iterator is less than or equal to the cap, not less than.
     *
     * @param forBlock Incoming ForNode.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If there is an error in interpreting forBlock's statements.
     */
    private void interpretFor(ForNode forBlock, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        String iteratorName = forBlock.getIterator().getName();

        variables.put(iteratorName,
                makeInterpreterDataTypeFromNode(interpretExpression(forBlock.getFromNode(), variables)));

        int cap = Integer.parseInt(interpretExpression(forBlock.getToNode(), variables).toString());

        for (int iterator = Integer.parseInt(variables.get(iteratorName).toString());
             iterator <= cap; iterator++)
        {
            interpretStatements(forBlock.getStatements(), variables);
            ((IntegerDataType)variables.get(iteratorName)).increment();
        }

        variables.remove(iteratorName);
    }

    /**
     * Executes the statements of the incoming RepeatNode using the values given by the incoming HashMap.
     *
     * @param repeatBlock Incoming RepeatNode.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If there is an error in interpreting repeatBlock's statements.
     */
    private void interpretRepeat(RepeatNode repeatBlock, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        BooleanCompareNode cond = repeatBlock.getConditional();
        while (!interpretBooleanCompare(cond, variables))
        {
            interpretStatements(repeatBlock.getStatements(), variables);
        }
    }

    /**
     * Executes the statements of the incoming WhileNode using the values given by the incoming HashMap.
     *
     * @param whileBlock Incoming WhileNode.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If there is an error in interpreting whileBlock's statements.
     */
    private void interpretWhile(WhileNode whileBlock, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        BooleanCompareNode cond = whileBlock.getConditional();
        while (interpretBooleanCompare(cond, variables))
        {
            interpretStatements(whileBlock.getStatements(), variables);
        }
    }

    /**
     * Processes and updates the value given by the incoming AssignmentNode to the incoming HashMap.
     *
     * @param assignment Incoming AssignmentNode.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If the requested variable was not initialized or is constant.
     */
    private void interpretAssignment(AssignmentNode assignment, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        String targetName = assignment.getTarget().getName();

        if (!variables.containsKey(targetName))
        {
            throw new NonexistantVariableException(targetName, assignment.getLineNumber());
        }
        if (!variables.get(targetName).isChangeable())
        {
            throw new UnchangeableVariableException(targetName, assignment.getLineNumber());
        }
        else
        {
            variables.replace(targetName,
            makeInterpreterDataTypeFromNode(interpretExpression(assignment.getValue(), variables)));
        }
    }

    /**
     * Calls the function referenced by the incoming FunctionCallNode
     * with arguments given by data from the incoming HashMap.
     *
     * @param calledFunction Incoming FunctionCallNode.
     * @param variables Incoming HashMap.
     * @throws SyntaxErrorException If there is a syntax error in interpreting the called function's contents.
     */
    private void interpretFunctionCall(FunctionCallNode calledFunction, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        FunctionNode function = functionMap.get(calledFunction.getName());

        if (function.isVariadic() || (function.getNumberOfParameters() == calledFunction.getNumberOfArguments()))
        {
            ArrayList<ArgumentNode> arguments = calledFunction.getArguments();
            ArrayList<VariableNode> parameters = function.getParameterList();

            ArrayList<InterpreterDataType> values = handleArguments(arguments, variables);
            HashMap<String, InterpreterDataType> returnedVariables = interpretFunction(function, values);

            for (int i = 0; i < arguments.size(); i++)
            {
                if (!arguments.get(i).isConstant() && parameters.get(i).isChangeable())
                {
                    variables.replace(arguments.get(i).getVariableReference().getName(),
                            returnedVariables.get(parameters.get(i).getName()));
                }
            }
        }
        else throw new InvalidArgumentsException(calledFunction.getName(), calledFunction.getLineNumber());
    }

    /**
     * Adds the incoming ArgumentNode ArrayList and generated IDTs from those ArgumentNodes to the incoming HashMap.
     *
     * @param arguments Incoming ArgumentNode ArrayList.
     * @param variables Incoming HashMap.
     * @return ArrayList of IDTs added.
     * @throws SyntaxErrorException If there is a syntax error in interpreting the ArgumentNodes.
     */
    private ArrayList<InterpreterDataType> handleArguments(
            ArrayList<ArgumentNode> arguments, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        ArrayList<InterpreterDataType> argumentDataTypeList = new ArrayList<>();

        ArgumentNode currentArgument;

        ASTNode argumentContent;

        for (int i = 0; i < arguments.size(); i++)
        {
            currentArgument = arguments.get(i);

            argumentContent = currentArgument.isConstant() ?
                    currentArgument.getConstant() : currentArgument.getVariableReference();

            argumentDataTypeList.add(makeInterpreterDataTypeFromNode(interpretExpression(argumentContent, variables)));
        }

        return argumentDataTypeList;
    }

    /**
     * Returns the appropriate interpretation of the incoming ASTNode
     * according to the IDTs received from the incoming HashMap.
     *
     * @param operand Incoming ASTNode.
     * @param variables Incoming HashMap.
     * @return Appropriate interpretation of operand.
     * @throws SyntaxErrorException If there is no valid expression ASTNode found.
     */
    private ASTNode interpretExpression(ASTNode operand, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        if (operand instanceof VariableReferenceNode)
        {
            return makeNodeFromVariableReference((VariableReferenceNode)operand, variables);
        }
        else if (operand instanceof MathOpNode)
        {
            return interpretMathOp((MathOpNode)operand, variables);
        }
        else if (operand instanceof DataTypeNode) //handles number constants
        {
            return operand;
        }
        else throw new SyntaxErrorException("No valid expression on line " + operand.getLineNumber() + ".");
    }

    /**
     * Returns the result of the incoming BooleanCompareNode based on the IDTs received from the incoming HashMap.
     *
     * @param comparison Incoming BooleanCompareNode.
     * @param variables Incoming HashMap.
     * @return Result of the comparison performed in comparison.
     * @throws SyntaxErrorException If there is a syntax error in checking and getting types.
     */
    private boolean interpretBooleanCompare(BooleanCompareNode comparison,
                                            HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        if (comparison == null) //returns else statements, since they have no conditional
        {
            return true;
        }

        ASTNode leftComparand = interpretExpression(comparison.getLeftComparand(), variables);
        ASTNode rightComparand = interpretExpression(comparison.getRightComparand(), variables);

        return performComparison(leftComparand, rightComparand,
                                 checkTypes(leftComparand, rightComparand), comparison.getCompType());
    }

    /**
     * Returns the result of the incoming MathOpNode based on the IDTs received from the incoming HashMap.
     *
     * @param mathOp Incoming MathOpNode.
     * @param variables Incoming HashMap.
     * @return Result of the operation performed in mathOp.
     * @throws SyntaxErrorException If there is a syntax error in checking and getting types.
     */
    private ASTNode interpretMathOp(MathOpNode mathOp, HashMap<String, InterpreterDataType> variables)
            throws SyntaxErrorException
    {
        ASTNode leftOperand = interpretExpression(mathOp.getLeftOperand(), variables);
        ASTNode rightOperand = interpretExpression(mathOp.getRightOperand(), variables);

        return performOperation(leftOperand, rightOperand, checkTypes(leftOperand, rightOperand), mathOp.getOpType());
    }

    /**
     * Creates an ASTNode informed by the incoming VariableReferenceNode,
     * whose value is extracted from the incoming HashMap.
     *
     * @param reference Incoming VariableReferenceNode.
     * @param variables Incoming HashMap.
     * @return ASTNode informed by reference and variables.
     * @throws SyntaxErrorException If no valid IDT in variables is found for reference.
     */
    private ASTNode makeNodeFromVariableReference(
            VariableReferenceNode reference, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        InterpreterDataType idt = variables.get(reference.getName());

        if (idt instanceof IntegerDataType)
        {
            return new IntegerNode((IntegerDataType)idt);
        }
        else if (idt instanceof RealDataType)
        {
            return new RealNode((RealDataType)idt);
        }
        else if (idt instanceof StringDataType)
        {
            return new StringNode((StringDataType)idt);
        }
        else if (idt instanceof CharacterDataType)
        {
            return new CharacterNode((CharacterDataType)idt);
        }
        else if (idt instanceof BooleanDataType)
        {
            return new BooleanNode((BooleanDataType)idt);
        }
        else throw new SyntaxErrorException("No valid data type found on line " + reference.getLineNumber() + ".");
    }

    /**
     * Creates an IDT informed by the incoming data type ASTNode.
     *
     * @param incomingNode Incoming ASTNode.
     * @return Appropriate InterpreterDataType informed by incomingNode.
     * @throws SyntaxErrorException If no valid data type ASTNode is found.
     */
    private InterpreterDataType makeInterpreterDataTypeFromNode(ASTNode incomingNode) throws SyntaxErrorException
    {
        if (incomingNode instanceof IntegerNode)
        {
            return new IntegerDataType((IntegerNode)incomingNode);
        }
        else if (incomingNode instanceof RealNode)
        {
            return new RealDataType((RealNode)incomingNode);
        }
        else if (incomingNode instanceof StringNode)
        {
            return new StringDataType((StringNode)incomingNode);
        }
        else if (incomingNode instanceof CharacterNode)
        {
            return new CharacterDataType((CharacterNode)incomingNode);
        }
        else if (incomingNode instanceof BooleanNode)
        {
            return new BooleanDataType((BooleanNode)incomingNode);
        }
        else throw new SyntaxErrorException("No valid data type found on line " + incomingNode.getLineNumber() + ".");
    }

    /**
     * Creates and returns an IDT informed by the type of the incoming VariableNode.
     *
     * @param incomingNode Incoming VariableNode.
     * @return Appropriate IDT informed by incomingNode.
     * @throws SyntaxErrorException If no valid data type tokenType was extracted from incomingNode.
     */
    private InterpreterDataType makeInterpreterDataTypeFromVariableNode(VariableNode incomingNode)
            throws SyntaxErrorException
    {
        tokenType variableType = incomingNode.getType();

        if (variableType == tokenType.INTEGER)
        {
            return new IntegerDataType(incomingNode, true);
        }
        else if (variableType == tokenType.REAL)
        {
            return new RealDataType(incomingNode, true);
        }
        else if (variableType == tokenType.STRING)
        {
            return new StringDataType(incomingNode, true);
        }
        else if (variableType == tokenType.CHARACTER)
        {
            return new CharacterDataType(incomingNode, true);
        }
        else if (variableType == tokenType.BOOLEAN)
        {
            return new BooleanDataType(incomingNode, true);
        }
        else if (variableType == tokenType.ARRAY)
        {
            return new ArrayDataType(incomingNode, true);
        }
        else throw new SyntaxErrorException("No valid data type found on line " + incomingNode.getLineNumber() + ".");
    }

    /**
     * Returns an appropriate typeCheckResult if the former and latter ASTNodes are the same kind of ASTNode.
     * Returns FAILURE if the ASTNodes are not of the same type, or both not of a valid type.
     *
     * @param left Former incoming ASTNode.
     * @param right Latter incoming ASTNode.
     * @return Appropriate typeCheckResult if left and right are of the same kind of ASTNode.
     */
    private typeCheckResult checkTypes(ASTNode left, ASTNode right)
    {
        if (left instanceof IntegerNode && right instanceof IntegerNode)
        {
            return typeCheckResult.INTEGER;
        }
        else if (left instanceof RealNode && right instanceof RealNode)
        {
            return typeCheckResult.REAL;
        }
        else if (left instanceof StringNode && right instanceof StringNode)
        {
            return typeCheckResult.STRING;
        }
        else if (left instanceof CharacterNode && right instanceof CharacterNode)
        {
            return typeCheckResult.CHARACTER;
        }
        else if (left instanceof BooleanNode && right instanceof BooleanNode)
        {
            return typeCheckResult.BOOLEAN;
        }
        else return typeCheckResult.FAILURE;
    }

    /**
     * Performs an operation between the former and latter ASTNodes, using the incoming typeCheckResult to determine
     * which type both the ASTNodes are and the incoming operationType to determine which operation to perform.
     *
     * @param left Former incoming ASTNode.
     * @param right Latter incoming ASTNode.
     * @param typeCheck Incoming typeCheckResult.
     * @param opType Incoming operationType.
     * @return Result of the operation performed between left and right according to typeCheck and opType.
     * @throws SyntaxErrorException If no valid operation for typeCheck is found.
     */
    private ASTNode performOperation(ASTNode left, ASTNode right, typeCheckResult typeCheck, operationType opType)
            throws SyntaxErrorException
    {
        String leftString = left.toString(), rightString = right.toString();

        if (typeCheck == typeCheckResult.INTEGER || typeCheck == typeCheckResult.REAL)
        {
            float leftNumber, rightNumber, result = 0.0F;

            if (typeCheck == typeCheckResult.INTEGER)
            {
                leftNumber = Integer.parseInt(leftString);
                rightNumber = Integer.parseInt(rightString);
            }
            else
            {
                leftNumber = Float.parseFloat(leftString);
                rightNumber = Float.parseFloat(rightString);
            }

            if (opType == operationType.ADD)
            {
                result = leftNumber + rightNumber;
            }
            else if (opType == operationType.SUB)
            {
                result = leftNumber - rightNumber;
            }
            else if (opType == operationType.MULT)
            {
                result = leftNumber * rightNumber;
            }
            else if (opType == operationType.DIV)
            {
                result = leftNumber / rightNumber;
            }
            else if (opType == operationType.MOD)
            {
                result = leftNumber % rightNumber;
            }

            return typeCheck == typeCheckResult.INTEGER ? new IntegerNode((int)result, left.getLineNumber()) :
                                                          new RealNode(result, left.getLineNumber());
        }
        else if (typeCheck == typeCheckResult.STRING)
        {
            if (opType == operationType.ADD)
            {
                return new StringNode(leftString + rightString, left.getLineNumber());
            }
            else throw new SyntaxErrorException(
                    "No valid String operation found on line " + left.getLineNumber() + ".");
        }
        else throw new SyntaxErrorException("No valid operation found on line " + left.getLineNumber() + ".");
    }

    /**
     * Performs an operation between the former and latter incoming ASTNodes, using the incoming typeCheckResult
     * to determine which type both the ASTNodes are and the incoming comparisonType to determine which comparison to
     * perform.
     *
     * @param left Former incoming ASTNode.
     * @param right Latter incoming ASTNode.
     * @param typeCheck Incoming typeCheckResult.
     * @param compType Incoming comparisonType.
     * @return Result of the comparison between left and right according to typeCheck and compType.
     * @throws SyntaxErrorException If no valid comparison for typeCheck is found.
     */
    private boolean performComparison(ASTNode left, ASTNode right, typeCheckResult typeCheck, comparisonType compType)
            throws SyntaxErrorException
    {
        String leftString = left.toString(), rightString = right.toString();
        boolean result = false;

        if (typeCheck == typeCheckResult.INTEGER || typeCheck == typeCheckResult.REAL)
        {
            float leftNumber, rightNumber;

            if (typeCheck == typeCheckResult.INTEGER)
            {
                leftNumber = Integer.parseInt(leftString);
                rightNumber = Integer.parseInt(rightString);
            }
            else
            {
                leftNumber = Float.parseFloat(leftString);
                rightNumber = Float.parseFloat(rightString);
            }

            if (compType == comparisonType.GTHAN)
            {
                result = leftNumber > rightNumber;
            }
            else if (compType == comparisonType.LTHAN)
            {
                result = leftNumber < rightNumber;
            }
            else if (compType == comparisonType.GETO)
            {
                result = leftNumber >= rightNumber;
            }
            else if (compType == comparisonType.LETO)
            {
                result = leftNumber <= rightNumber;
            }
            else if (compType == comparisonType.EQUAL)
            {
                result = leftNumber == rightNumber;
            }
            else if (compType == comparisonType.NEQUAL)
            {
                result = leftNumber != rightNumber;
            }

            return result;
        }
        else if (typeCheck == typeCheckResult.STRING || typeCheck == typeCheckResult.CHARACTER)
        {
            if(compType == comparisonType.EQUAL)
            {
                result = leftString.equals(rightString);
            }
            if(compType == comparisonType.NEQUAL)
            {
                result = !leftString.equals(rightString);
            }

            return result;
        }
        else if (typeCheck == typeCheckResult.BOOLEAN)
        {
            boolean leftBoolean = Boolean.parseBoolean(leftString), rightBoolean = Boolean.parseBoolean(rightString);

            if (compType == comparisonType.NOT)
            {
                result = !leftBoolean; //TODO: This is probably not correct
            }
            else if (compType == comparisonType.OR)
            {
                result = leftBoolean || rightBoolean;
            }
            else if (compType == comparisonType.AND)
            {
                result = leftBoolean && rightBoolean;
            }
            else if (compType == comparisonType.EQUAL)
            {
                result = leftBoolean == rightBoolean;
            }
            return result;
        }
        else throw new SyntaxErrorException("No valid boolean comparison found on line " + left.getLineNumber() + ".");
    }
}
