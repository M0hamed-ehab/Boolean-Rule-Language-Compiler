package ast;

import ast.visualization.AstVisualizer;
import java.util.List;

public final class AstDemo {
    private AstDemo() {
    }

    public static void main(String[] args) {
        ProgramNode program = new ProgramNode(List.of(
                new AssignmentStatementNode(
                        new IdentifierNode("adult"),
                        new BinaryExpressionNode(
                                new IdentifierNode("age"),
                                BinaryOperator.GREATER_OR_EQUAL,
                                new NumberLiteralNode(18))),
                new PrintStatementNode(new IdentifierNode("adult")),
                new AssignmentStatementNode(
                        new IdentifierNode("approved"),
                        new BinaryExpressionNode(
                                new BinaryExpressionNode(
                                        new IdentifierNode("income"),
                                        BinaryOperator.GREATER_THAN,
                                        new NumberLiteralNode(5000)),
                                BinaryOperator.AND,
                                new UnaryExpressionNode(
                                        UnaryOperator.NOT,
                                        new IdentifierNode("blocked")))),
                new PrintStatementNode(new IdentifierNode("approved")),
                new AssignmentStatementNode(
                        new IdentifierNode("valid"),
                        new BinaryExpressionNode(
                                new BinaryExpressionNode(
                                        new GroupingExpressionNode(
                                                new BinaryExpressionNode(
                                                        new IdentifierNode("score"),
                                                        BinaryOperator.ADD,
                                                        new IdentifierNode("bonus"))),
                                        BinaryOperator.GREATER_OR_EQUAL,
                                        new NumberLiteralNode(60)),
                                BinaryOperator.AND,
                                new BinaryExpressionNode(
                                        new IdentifierNode("attempts"),
                                        BinaryOperator.LESS_THAN,
                                        new NumberLiteralNode(3)))),
                new PrintStatementNode(new IdentifierNode("valid"))));

        AstVisualizer.showAndWait(program, "Boolean Rule Language - AST");
    }
}
