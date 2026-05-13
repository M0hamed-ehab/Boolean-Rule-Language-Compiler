package ast;

import java.util.List;
import java.util.Objects;

public class PrintStatementNode extends StatementNode {
    private final ExpressionNode value;

    public PrintStatementNode(ExpressionNode value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public ExpressionNode getValue() {
        return value;
    }

    @Override
    public String label() {
        return "Print";
    }

    @Override
    public List<AstNode> children() {
        return List.of(value);
    }
}
