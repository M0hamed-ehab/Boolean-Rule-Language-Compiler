import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String input;
    private int pos = 0;

    private static final Map<String, TokenType> keywords = Map.of(
            "print", TokenType.PRINT,
            "and", TokenType.AND,
            "or", TokenType.OR,
            "not", TokenType.NOT,
            "true", TokenType.TRUE,
            "false", TokenType.FALSE);

    public Scanner(String input) {
        this.input = input;
    }

    public List<Token> scanTokens() {
        List<Token> tokens = new ArrayList<>();
        Token token;

        do {
            token = nextToken();
            tokens.add(token);
        } while (token.type != TokenType.EOF);

        return tokens;
    }

    private Token nextToken() {
        skipWhitespace();

        if (isAtEnd()) {
            return new Token(TokenType.EOF, "", pos);
        }

        char c = advance();

        if (Character.isLetter(c)) {
            return scanIdentifier();
        }

        if (Character.isDigit(c)) {
            return scanNumber();
        }

        switch (c) {
            case ':':
                if (match('='))
                    return token(TokenType.ASSIGN, ":=");
                break;

            case '<':
                if (match('='))
                    return token(TokenType.LE, "<=");
                return token(TokenType.LT, "<");

            case '>':
                if (match('='))
                    return token(TokenType.GE, ">=");
                return token(TokenType.GT, ">");

            case '=':
                return token(TokenType.EQ, "=");

            case '!':
                if (match('='))
                    return token(TokenType.NE, "!=");
                break;

            case '+':
                return token(TokenType.PLUS, "+");
            case '-':
                return token(TokenType.MINUS, "-");
            case '*':
                return token(TokenType.MUL, "*");
            case '/':
                return token(TokenType.DIV, "/");

            case '(':
                return token(TokenType.LPAREN, "(");
            case ')':
                return token(TokenType.RPAREN, ")");
            case ';':
                return token(TokenType.SEMICOLON, ";");
        }

        throw new RuntimeException("Lexical error at position " + (pos - 1));
    }

    private Token scanIdentifier() {
        int start = pos - 1;
        while (!isAtEnd() && Character.isLetterOrDigit(peek()))
            advance();
        String text = input.substring(start, pos);
        TokenType type = keywords.getOrDefault(text, TokenType.ID);
        return new Token(type, text, start);
    }

    private Token scanNumber() {
        int start = pos - 1;
        while (!isAtEnd() && Character.isDigit(peek()))
            advance();
        return new Token(TokenType.NUMBER, input.substring(start, pos), start);
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek()))
            advance();
    }

    private boolean match(char expected) {
        if (isAtEnd() || input.charAt(pos) != expected)
            return false;
        pos++;
        return true;
    }

    private char advance() {
        return input.charAt(pos++);
    }

    private char peek() {
        return input.charAt(pos);
    }

    private boolean isAtEnd() {
        return pos >= input.length();
    }

    private Token token(TokenType type, String lexeme) {
        return new Token(type, lexeme, pos - lexeme.length());
    }
}