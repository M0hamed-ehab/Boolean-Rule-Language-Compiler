import Scanner.Scanner;
import Scanner.Token;
import ast.ProgramNode;
import ast.visualization.AstVisualizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ParseAndVisualize {

    public static void main(String[] args) {
        String filePath = firstNonFlag(args);
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

            String title = filePath == null
                    ? "Boolean Rule Language - AST"
                    : "AST - " + Path.of(filePath).getFileName();

            AstVisualizer.showAndWait(program, title);
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    private static String loadSource(String filePath) {
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
}
