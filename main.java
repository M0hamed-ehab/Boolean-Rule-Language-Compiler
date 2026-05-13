
import Scanner.Scanner;
import Scanner.Token;
import ast.AstNode;
import ast.ProgramNode;
import ast.visualization.AstVisualizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class main {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filePath = firstNonFlag(args);
            if (filePath == null) {
                System.err.println("Expected a file path, for example: tests\\valid\\sample.brl");
                System.exit(1);
            }
            runFile(filePath);
            return;
        }

        java.util.Scanner input = new java.util.Scanner(System.in);
        while (true) {
            System.out.println("Enter test file path, or press Enter to type code manually, or enter 0 to exit:");
            String filePath = input.nextLine().trim();

            if (filePath.contentEquals("0")) {
                System.exit(0);
            }

            if (!filePath.isEmpty()) {
                runFile(filePath);
                continue;
            }

            System.out.println("Enter Code:");
            StringBuilder builder = new StringBuilder();
            while (true) {
                String line = input.nextLine();
                if (line.contentEquals("0")) {
                    System.exit(0);
                }
                if (line.isEmpty()) {
                    break;
                }
                builder.append(line).append("\n");
            }

            runSource(builder.toString(), "Boolean Rule Language - AST");
        }
    }

    private static void runFile(String filePath) {
        Path sourcePath = resolvePath(filePath);

        try {
            String source = Files.readString(sourcePath);
            System.out.println("Running file: " + sourcePath);
            runSource(source, "AST - " + sourcePath.getFileName());
        } catch (IOException ex) {
            System.err.println("Could not read source file: " + sourcePath);
        }
    }

    private static Path resolvePath(String filePath) {
        Path path = Path.of(filePath);
        if (Files.exists(path)) {
            return path;
        }

        Path validPath = Path.of("tests", "valid", filePath);
        if (Files.exists(validPath)) {
            return validPath;
        }

        Path invalidPath = Path.of("tests", "invalid", filePath);
        if (Files.exists(invalidPath)) {
            return invalidPath;
        }

        return path;
    }

    private static void runSource(String source, String windowTitle) {
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
            AstVisualizer.showAndWait(program, windowTitle);

            System.out.println("Traversal:");
            Traversal traversal = new Traversal();
            traversal.traverse(program);

        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
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
