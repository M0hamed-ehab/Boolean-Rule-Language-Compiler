package ast;

public class BooleanLiteralNode extends ExpressionNode {
    private final boolean value;

    public BooleanLiteralNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String label() {
        return "Boolean: " + value;
    }
}
