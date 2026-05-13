package ast;

public enum BinaryOperator {
    OR("or"),
    AND("and"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">="),
    EQUAL("="),
    NOT_EQUAL("!="),
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String text;

    BinaryOperator(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
