/**
 * Processes a program and produces an output.
 */

package InterpreterTools;

import java.util.ArrayList;
import java.util.HashMap;

import CrossStageTools.CrossStageNodes.*;
import Exceptions.NonexistantVariableException;
import Exceptions.SyntaxErrorException;
import Exceptions.UnchangeableVariableException;
import InterpreterTools.InterpreterDataTypes.*;
import ParserTools.Nodes.DataTypeNodes.*;
import ParserTools.Nodes.MathOpNode;
import ParserTools.Nodes.MathOpNode.operationType;
import ParserTools.Nodes.StructureNodes.IfNode;
import ParserTools.Nodes.StructureNodes.LoopNodes.*;
import ParserTools.Nodes.StructureNodes.StatementNodes.AssignmentNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.BooleanCompareNode;
import ParserTools.Nodes.StructureNodes.StatementNodes.BooleanCompareNode.comparisonType;
import ParserTools.Nodes.StructureNodes.StatementNodes.VariableReferenceNode;

public class Interpreter
{
    private enum typeCheckResult {FAILURE, INTEGER, REAL, STRING, CHARACTER, BOOLEAN}

    private ProgramNode program;

    private HashMap<String, FunctionNode> functionMap;

    public Interpreter(ProgramNode parsedProgram)
    {
        program = parsedProgram;
        functionMap = program.getFunctionMap();
    }

    public void interpret() throws SyntaxErrorException
    {
        interpretFunction("start");
    }

    private void interpretFunction(String functionName) throws SyntaxErrorException
    {
        HashMap<String, InterpreterDataType> localVariables = handleVariables(functionName);

        interpretStatements(functionMap.get(functionName).getStatementList(), localVariables);
    }

    private HashMap<String, InterpreterDataType> handleVariables(String functionName)
    {
        HashMap<String, InterpreterDataType> localVariables = new HashMap<>();

        ArrayList<VariableNode> variables = functionMap.get(functionName).getVariableList();

        for (int i = 0; i < variables.size(); i++)
        {
            VariableNode currentVariable = variables.get(i);
            localVariables.put(currentVariable.getName(), handleDataType(currentVariable, currentVariable.isChangeable()));
        }

        return localVariables;
    }

    private InterpreterDataType handleDataType(VariableNode currentVariable, boolean isInitializer)
    {
        switch (currentVariable.getType())
        {
            case INTEGER   : return new IntegerDataType(currentVariable, isInitializer);
            case REAL      : return new RealDataType(currentVariable, isInitializer);
            case STRING    : return new StringDataType(currentVariable, isInitializer);
            case CHARACTER : return new CharacterDataType(currentVariable, isInitializer);
            case BOOLEAN   : return new BooleanDataType(currentVariable, isInitializer);
            default        : return null;
        }
    }

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
        }
    }

    private void interpretIf(IfNode ifBlock, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        if (interpretBooleanCompare(ifBlock.getConditional(), variables))
        {
            interpretStatements(ifBlock.getStatements(), variables);
        }
        else
        {
            interpretIf(ifBlock.getNext(), variables);
        }
    }

    private void interpretFor(ForNode forBlock, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
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

    private void interpretRepeat(RepeatNode repeatBlock, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        BooleanCompareNode cond = repeatBlock.getConditional();
        while (!interpretBooleanCompare(cond, variables))
        {
            interpretStatements(repeatBlock.getStatements(), variables);
        }
    }

    private void interpretWhile(WhileNode whileBlock, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
    {
        BooleanCompareNode cond = whileBlock.getConditional();
        while (interpretBooleanCompare(cond, variables))
        {
            interpretStatements(whileBlock.getStatements(), variables);
        }
    }

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

    private ASTNode interpretExpression(ASTNode operand, HashMap<String, InterpreterDataType> variables)
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
        else return null;
    }

    private boolean interpretBooleanCompare(BooleanCompareNode comparison,
                                            HashMap<String, InterpreterDataType> variables)
    {
        if (comparison == null) //else statements
        {
            return true;
        }

        ASTNode leftComparand = interpretExpression(comparison.getLeftComparand(), variables);
        ASTNode rightComparand = interpretExpression(comparison.getRightComparand(), variables);

        return performComparison(leftComparand, rightComparand,
                                 checkTypes(leftComparand, rightComparand), comparison.getCompType());
    }

    private ASTNode interpretMathOp(MathOpNode mathOp, HashMap<String, InterpreterDataType> variables)
    {
        ASTNode leftOperand = interpretExpression(mathOp.getLeftOperand(), variables);
        ASTNode rightOperand = interpretExpression(mathOp.getRightOperand(), variables);

        return performOperation(leftOperand, rightOperand, checkTypes(leftOperand, rightOperand), mathOp.getOpType());
    }

    private ASTNode makeNodeFromVariableReference(VariableReferenceNode reference,
                                                  HashMap<String, InterpreterDataType> variables)
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
        else return null;
    }

    private InterpreterDataType makeInterpreterDataTypeFromNode(ASTNode incomingNode)
    {
        InterpreterDataType idt;

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
        return null;
    }

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

    private ASTNode performOperation(ASTNode left, ASTNode right, typeCheckResult typeCheck, operationType opType)
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
        }

        return null;
    }

    private boolean performComparison(ASTNode left, ASTNode right, typeCheckResult typeCheck, comparisonType compType)
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
            return result;
        }

        return false;
    }
}
