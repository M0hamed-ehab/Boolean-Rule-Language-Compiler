package Scanner;

public enum TokenType {

    // Keywords
    PRINT,
    AND,
    OR,
    NOT,
    TRUE,
    FALSE,

    // Identifiers & literals
    ID,
    NUMBER,

    // Operators
    ASSIGN,     // :=
    PLUS,       // +
    MINUS,      // -
    MUL,        // *
    DIV,        // /
    LT,         // <
    GT,         // >
    LE,         // <=
    GE,         // >=
    EQ,         // =
    NE,         // !=

    // Delimiters
    LPAREN,     // (
    RPAREN,     // )
    SEMICOLON,  // ;

    // End of input
    EOF
}
