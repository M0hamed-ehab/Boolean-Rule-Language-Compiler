package ast;

import java.util.Objects;

public class IdentifierNode extends ExpressionNode {
    private final String name;

    public IdentifierNode(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public String getName() {
        return name;
    }

    @Override
    public String label() {
        return "Identifier: " + name;
    }
}
