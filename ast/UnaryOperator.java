package ast;

public enum UnaryOperator {
    NOT("not");

    private final String text;

    UnaryOperator(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
