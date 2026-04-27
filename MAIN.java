import Scanner.Scanner;
import Scanner.Token;
import ast.AstNode;
import ast.ProgramNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MAIN {

    public static void main(String[] args) {
        String[] filePath = new String[]{"tests/valid/sample.brl"};
        String source = loadSource(filePath);
        if (source == null) {
            System.exit(1);
            return;
        }

        boolean printTokens = hasFlag(args, "--tokens");

        try {
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scanTokens();

            if (printTokens) {
                for (Token token : tokens) {
                    System.out.println(token);
                }
                System.out.println();
            }

            Parser parser = new Parser(tokens);
            ProgramNode program = parser.parseProgram();

            System.out.println("Parse successful.");
            System.out.println(toText(program));
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    private static String loadSource(String[] args) {
        String filePath = firstNonFlag(args);

        if (filePath == null) {
            return """
                        adult := age >= 18;
                        print adult;
                        approved := income > 5000 and not blocked;
                        print approved;
                        valid := (score + bonus) >= 60 and attempts < 3;
                        print valid;
                    """;
        }

        Path sourcePath = Path.of(filePath);

        try {
            return Files.readString(sourcePath);
        } catch (IOException ex) {
            System.err.println("Could not read source file: " + sourcePath);
            return null;
        }
    }

    private static boolean hasFlag(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equals(flag)) {
                return true;
            }
        }

        return false;
    }

    private static String firstNonFlag(String[] args) {
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                return arg;
            }
        }

        return null;
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
