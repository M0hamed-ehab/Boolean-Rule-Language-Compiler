package ast;

import java.util.List;
import java.util.Objects;

public class ProgramNode extends AbstractAstNode {
    private final List<StatementNode> statements;

    public ProgramNode(List<StatementNode> statements) {
        Objects.requireNonNull(statements, "statements cannot be null");
        this.statements = List.copyOf(statements);
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String label() {
        return "Program";
    }

    @Override
    public List<AstNode> children() {
        return List.copyOf(statements);
    }
}
