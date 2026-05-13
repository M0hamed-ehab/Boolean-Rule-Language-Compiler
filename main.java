
import Scanner.Scanner;
import Scanner.Token;
import ast.AstNode;
import ast.ProgramNode;
import ast.visualization.AstVisualizer;
import java.util.*;

public class main {

    public static void main(String[] args) {

        while (true) {
            System.out.println("Enter Code:");
            java.util.Scanner input = new java.util.Scanner(System.in);
            StringBuilder builder = new StringBuilder();
            while (true) {
                String line = input.nextLine();
                if (line.contentEquals("0")) {
                    System.exit(1);
                }
                if (line.isEmpty()) {
                    break;
                }
                builder.append(line).append("\n");
            }

            String source = builder.toString();

            try {
                Scanner scanner = new Scanner(source);
                List<Token> tokens = scanner.scanTokens();

                System.out.println("Tokens:");
                for (Token token : tokens) {
                    System.out.println(token);
                }
                System.out.println();

                Parser parser = new Parser(tokens);
                ProgramNode program = parser.parseProgram();

                System.out.println("Parse successful.");
                System.out.println(toText(program));

                System.out.println("Opening ast GUI");
                AstVisualizer.showAndWait(program, "Boolean Rule Language - AST");

                System.out.println("Traversal:");
                Traversal traversal = new Traversal();
                traversal.traverse(program);

            } catch (RuntimeException ex) {
                System.err.println(ex.getMessage());
                // System.exit(1);
            }
        }
    }

    private static String toText(AstNode root) {
        StringBuilder builder = new StringBuilder();
        appendNode(builder, root, 0);
        return builder.toString();
    }

    private static void appendNode(StringBuilder builder, AstNode node, int depth) {
        builder.append("  ".repeat(Math.max(0, depth)));
        builder.append(node.label());
        builder.append('\n');

        for (AstNode child : node.children()) {
            appendNode(builder, child, depth + 1);
        }
    }
}
