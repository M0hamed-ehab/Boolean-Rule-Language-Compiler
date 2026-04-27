package ast;

import java.util.List;
import java.util.Objects;

public class BinaryExpressionNode extends ExpressionNode {
    private final ExpressionNode left;
    private final BinaryOperator operator;
    private final ExpressionNode right;

    public BinaryExpressionNode(ExpressionNode left, BinaryOperator operator, ExpressionNode right) {
        this.left = Objects.requireNonNull(left, "left cannot be null");
        this.operator = Objects.requireNonNull(operator, "operator cannot be null");
        this.right = Objects.requireNonNull(right, "right cannot be null");
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public ExpressionNode getRight() {
        return right;
    }

    @Override
    public String label() {
        return "Binary: " + operator.getText();
    }

    @Override
    public List<AstNode> children() {
        return List.of(left, right);
    }
}
