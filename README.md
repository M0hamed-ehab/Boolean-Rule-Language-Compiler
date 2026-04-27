# Boolean-Rule-Language-Compiler
A small compiler front end in Java using a handwritten scanner and a handwritten recursive-descent parser for a rule-oriented language centered on boolean logic and comparison expressions. This variant tests precedence, associativity, and recursive parsing in a language that differs from standard arithmetic calculators.

## AST Module

The AST module is available in `ast/` with a simple node hierarchy for:
- program and statement nodes
- boolean/arithmetic expression nodes
- unary and binary operators

GUI visualization is available in `ast/visualization/AstVisualizer.java` using JavaFX (`TreeView` + `TextArea`).
JavaFX runtime (`javafx.controls`) is required.

## Parser Integration

- `Parser.java` builds AST nodes from scanner tokens using recursive descent.
- `ParseException.java` reports syntax errors with token positions.
- `MAIN.java` runs scanner + parser and prints a text AST.
- `ParseAndVisualize.java` runs scanner + parser and opens the JavaFX AST window.

## Compile and Run

Do not use source-file mode (`java MAIN.java ...`) for this project. Compile first, then run with classpath.

Set JavaFX path first:

```bash
PATH_TO_FX="/path/to/javafx/lib"
```

Compile core scanner + parser + AST model (no JavaFX required):

```bash
javac -d out MAIN.java Parser.java ParseException.java Scanner/*.java ast/AstNode.java ast/AbstractAstNode.java ast/StatementNode.java ast/ExpressionNode.java ast/ProgramNode.java ast/AssignmentStatementNode.java ast/PrintStatementNode.java ast/BinaryOperator.java ast/UnaryOperator.java ast/IdentifierNode.java ast/BooleanLiteralNode.java ast/NumberLiteralNode.java ast/UnaryExpressionNode.java ast/BinaryExpressionNode.java ast/GroupingExpressionNode.java
```

Run scanner + parser (text AST):

```bash
java -cp out MAIN tests/valid/08_complex_expression.brl --tokens
```

Compile with JavaFX visualizer support:

```bash
javac --module-path "$PATH_TO_FX" --add-modules javafx.controls -d out ParseAndVisualize.java MAIN.java Parser.java ParseException.java Scanner/*.java ast/*.java ast/visualization/*.java
```

Run scanner + parser + GUI AST:

```bash
java --module-path "$PATH_TO_FX" --add-modules javafx.controls -cp out ParseAndVisualize tests/valid/08_complex_expression.brl --tokens
```

Run manual AST demo:

```bash
java --module-path "$PATH_TO_FX" --add-modules javafx.controls -cp out ast.AstDemo
```
