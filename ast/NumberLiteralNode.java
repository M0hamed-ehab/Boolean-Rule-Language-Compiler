package ast;

public class NumberLiteralNode extends ExpressionNode {
    private final double value;

    public NumberLiteralNode(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String label() {
        if (value == (long) value) {
            return "Number: " + (long) value;
        }
        return "Number: " + value;
    }
}
