import Scanner.Token;
import Scanner.TokenType;
import ast.AssignmentStatementNode;
import ast.BinaryExpressionNode;
import ast.BinaryOperator;
import ast.BooleanLiteralNode;
import ast.ExpressionNode;
import ast.GroupingExpressionNode;
import ast.IdentifierNode;
import ast.NumberLiteralNode;
import ast.PrintStatementNode;
import ast.ProgramNode;
import ast.StatementNode;
import ast.UnaryExpressionNode;
import ast.UnaryOperator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = List.copyOf(Objects.requireNonNull(tokens, "tokens cannot be null"));
    }

    public ProgramNode parseProgram() {
        if (tokens.isEmpty()) {
            throw new ParseException("Syntax error: no tokens provided to parser.");
        }

        List<StatementNode> statements = new ArrayList<>();

        if (check(TokenType.EOF)) {
            throw error(peek(), "Program must contain at least one statement.");
        }

        while (!isAtEnd()) {
            statements.add(parseStatement());
            consume(TokenType.SEMICOLON, "Expected ';' after statement.");
        }

        return new ProgramNode(statements);
    }

    private StatementNode parseStatement() {
        if (match(TokenType.PRINT)) {
            return parsePrintStatement();
        }

        if (check(TokenType.ID)) {
            return parseAssignmentStatement();
        }

        throw error(peek(), "Expected a statement.");
    }

    private StatementNode parsePrintStatement() {
        Token identifier = consume(TokenType.ID, "Expected identifier after 'print'.");
        return new PrintStatementNode(new IdentifierNode(identifier.lexeme));
    }

    private StatementNode parseAssignmentStatement() {
        Token identifier = consume(TokenType.ID, "Expected identifier at start of assignment.");
        consume(TokenType.ASSIGN, "Expected ':=' after identifier.");
        ExpressionNode value = parseOrExpression();
        return new AssignmentStatementNode(new IdentifierNode(identifier.lexeme), value);
    }

    private ExpressionNode parseOrExpression() {
        ExpressionNode expression = parseAndExpression();

        while (match(TokenType.OR)) {
            ExpressionNode right = parseAndExpression();
            expression = new BinaryExpressionNode(expression, BinaryOperator.OR, right);
        }

        return expression;
    }

    private ExpressionNode parseAndExpression() {
        ExpressionNode expression = parseNotExpression();

        while (match(TokenType.AND)) {
            ExpressionNode right = parseNotExpression();
            expression = new BinaryExpressionNode(expression, BinaryOperator.AND, right);
        }

        return expression;
    }

    private ExpressionNode parseNotExpression() {
        if (match(TokenType.NOT)) {
            ExpressionNode operand = parseNotExpression();
            return new UnaryExpressionNode(UnaryOperator.NOT, operand);
        }

        return parseComparisonExpression();
    }

    private ExpressionNode parseComparisonExpression() {
        ExpressionNode expression = parseAdditiveExpression();

        if (match(TokenType.LT, TokenType.GT, TokenType.LE, TokenType.GE, TokenType.EQ, TokenType.NE)) {
            Token operator = previous();
            ExpressionNode right = parseAdditiveExpression();
            expression = new BinaryExpressionNode(expression, mapComparisonOperator(operator.type), right);
        }

        return expression;
    }

    private ExpressionNode parseAdditiveExpression() {
        ExpressionNode expression = parseMultiplicativeExpression();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            ExpressionNode right = parseMultiplicativeExpression();
            expression = new BinaryExpressionNode(expression, mapArithmeticOperator(operator.type), right);
        }

        return expression;
    }

    private ExpressionNode parseMultiplicativeExpression() {
        ExpressionNode expression = parseFactor();

        while (match(TokenType.MUL, TokenType.DIV)) {
            Token operator = previous();
            ExpressionNode right = parseFactor();
            expression = new BinaryExpressionNode(expression, mapArithmeticOperator(operator.type), right);
        }

        return expression;
    }

    private ExpressionNode parseFactor() {
        if (match(TokenType.TRUE)) {
            return new BooleanLiteralNode(true);
        }

        if (match(TokenType.FALSE)) {
            return new BooleanLiteralNode(false);
        }

        if (match(TokenType.ID)) {
            return new IdentifierNode(previous().lexeme);
        }

        if (match(TokenType.NUMBER)) {
            return parseNumberLiteral(previous());
        }

        if (match(TokenType.LPAREN)) {
            ExpressionNode expression = parseOrExpression();
            consume(TokenType.RPAREN, "Expected ')' after expression.");
            return new GroupingExpressionNode(expression);
        }

        throw error(peek(), "Expected expression.");
    }

    private ExpressionNode parseNumberLiteral(Token token) {
        try {
            return new NumberLiteralNode(Double.parseDouble(token.lexeme));
        } catch (NumberFormatException ex) {
            throw error(token, "Invalid number literal: '" + token.lexeme + "'.");
        }
    }

    private BinaryOperator mapComparisonOperator(TokenType tokenType) {
        return switch (tokenType) {
            case LT -> BinaryOperator.LESS_THAN;
            case GT -> BinaryOperator.GREATER_THAN;
            case LE -> BinaryOperator.LESS_OR_EQUAL;
            case GE -> BinaryOperator.GREATER_OR_EQUAL;
            case EQ -> BinaryOperator.EQUAL;
            case NE -> BinaryOperator.NOT_EQUAL;
            default -> throw new IllegalStateException("Unsupported comparison operator token: " + tokenType);
        };
    }

    private BinaryOperator mapArithmeticOperator(TokenType tokenType) {
        return switch (tokenType) {
            case PLUS -> BinaryOperator.ADD;
            case MINUS -> BinaryOperator.SUBTRACT;
            case MUL -> BinaryOperator.MULTIPLY;
            case DIV -> BinaryOperator.DIVIDE;
            default -> throw new IllegalStateException("Unsupported arithmetic operator token: " + tokenType);
        };
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return type == TokenType.EOF;
        }

        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseException error(Token token, String message) {
        String found = token.type == TokenType.EOF ? "end of input" : "'" + token.lexeme + "'";
        return new ParseException("Syntax error at position " + token.position + ": " + message + " Found " + found + ".");
    }
}
