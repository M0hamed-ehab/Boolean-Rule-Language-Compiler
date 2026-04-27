package ast;

import java.util.List;
import java.util.Objects;

public class UnaryExpressionNode extends ExpressionNode {
    private final UnaryOperator operator;
    private final ExpressionNode operand;

    public UnaryExpressionNode(UnaryOperator operator, ExpressionNode operand) {
        this.operator = Objects.requireNonNull(operator, "operator cannot be null");
        this.operand = Objects.requireNonNull(operand, "operand cannot be null");
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public ExpressionNode getOperand() {
        return operand;
    }

    @Override
    public String label() {
        return "Unary: " + operator.getText();
    }

    @Override
    public List<AstNode> children() {
        return List.of(operand);
    }
}
