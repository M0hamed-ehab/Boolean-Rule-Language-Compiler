import java.util.HashMap;
import java.util.Map;

import ast.AssignmentStatementNode;
import ast.AstNode;
import ast.ExpressionNode;
import ast.PrintStatementNode;
import ast.ProgramNode;

public class Traversal {
    public final Map<String, Object> var = new HashMap<>();

    public void traverse(ProgramNode pnode) {
        for (AstNode stmt : pnode.children()) {
            if (stmt instanceof AssignmentStatementNode assign) {
                String varName = assign.getTarget().getName();
                Object value = evaluate(assign.getValue());
                var.put(varName, value);

                System.out.println("Assigned " + varName + " = " + value);
            } else if (stmt instanceof PrintStatementNode print) {
                Object value = evaluate(print.getValue());
                System.out.println("Print: " + value);
            }

        }

    }

    public Object evaluate(ExpressionNode expr) {

        if (expr instanceof ast.NumberLiteralNode num) {
            return num.getValue();
        }
        if (expr instanceof ast.BooleanLiteralNode bool) {
            return bool.getValue();
        }
        if (expr instanceof ast.IdentifierNode id) {
            String name = id.getName();
            if (var.containsKey(name)) {
                return var.get(name);
            } else {
                throw new RuntimeException("Undefined variable: " + name);
            }
        }

        if (expr instanceof ast.BinaryExpressionNode bin) {
            Object leftVal = evaluate(bin.getLeft());
            Object rightVal = evaluate(bin.getRight());

            return switch (bin.getOperator()) {
                case ADD -> (Double) leftVal + (Double) rightVal;
                case SUBTRACT -> (Double) leftVal - (Double) rightVal;
                case MULTIPLY -> (Double) leftVal * (Double) rightVal;
                case DIVIDE -> (Double) leftVal / (Double) rightVal;
                case GREATER_THAN -> (Double) leftVal > (Double) rightVal;
                case LESS_THAN -> (Double) leftVal < (Double) rightVal;
                case EQUAL -> leftVal.equals(rightVal);
                case NOT_EQUAL -> !leftVal.equals(rightVal);
                case GREATER_OR_EQUAL -> (Double) leftVal >= (Double) rightVal;
                case LESS_OR_EQUAL -> (Double) leftVal <= (Double) rightVal;
                case AND -> (Boolean) leftVal && (Boolean) rightVal;
                case OR -> (Boolean) leftVal || (Boolean) rightVal;
                default -> throw new RuntimeException("Unknown operator: " + bin.getOperator());
            };
        }

        if (expr instanceof ast.UnaryExpressionNode unary) {
            Object operandVal = evaluate(unary.getOperand());
            if (unary.getOperator() == ast.UnaryOperator.NOT) {

                return !(Boolean) operandVal;
            }
        }

        return null;
    }

}
