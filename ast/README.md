# AST Module

This folder is a dedicated AST module for the Boolean Rule Language.

Requires JavaFX runtime (`javafx.controls`).

## Structure

- `AstNode`: base interface (`label()` + `children()`)
- `ProgramNode`: root node for a full program
- `StatementNode`: base for statements
  - `AssignmentStatementNode`
  - `PrintStatementNode`
- `ExpressionNode`: base for expressions
  - `IdentifierNode`
  - `BooleanLiteralNode`
  - `NumberLiteralNode`
  - `UnaryExpressionNode`
  - `BinaryExpressionNode`
  - `GroupingExpressionNode`
- `UnaryOperator` and `BinaryOperator`: normalized operators

## GUI Visualization

- `visualization/AstVisualizer.java` renders any AST with JavaFX (`TreeView` + textual pane).
- `AstDemo.java` builds a sample program AST and opens the GUI visualizer.

## Compile and run

```bash
PATH_TO_FX="/path/to/javafx/lib"

javac --module-path "$PATH_TO_FX" --add-modules javafx.controls -d out ast/*.java ast/visualization/*.java
java --module-path "$PATH_TO_FX" --add-modules javafx.controls -cp out ast.AstDemo
```
