package ast;

import java.util.List;
import java.util.Objects;

public class GroupingExpressionNode extends ExpressionNode {
    private final ExpressionNode expression;

    public GroupingExpressionNode(ExpressionNode expression) {
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String label() {
        return "Group";
    }

    @Override
    public List<AstNode> children() {
        return List.of(expression);
    }
}
