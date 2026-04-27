package ast;

import java.util.List;
import java.util.Objects;

public class AssignmentStatementNode extends StatementNode {
    private final IdentifierNode target;
    private final ExpressionNode value;

    public AssignmentStatementNode(IdentifierNode target, ExpressionNode value) {
        this.target = Objects.requireNonNull(target, "target cannot be null");
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public IdentifierNode getTarget() {
        return target;
    }

    public ExpressionNode getValue() {
        return value;
    }

    @Override
    public String label() {
        return "Assign";
    }

    @Override
    public List<AstNode> children() {
        return List.of(target, value);
    }
}
