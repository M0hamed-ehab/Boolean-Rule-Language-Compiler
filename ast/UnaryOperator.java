package ast;

public enum UnaryOperator {
    NOT("not"),
    NEGATE("-");

    private final String text;

    UnaryOperator(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
