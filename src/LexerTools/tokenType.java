/**
 * All tokens created from lexing must have a specified type.
 */

package LexerTools;

public enum tokenType
{
    IDENTIFIER, NUMBER, EOL,
    INDENT, DEDENT,
    CHARLITERAL, STRINGLITERAL,

    DEFINE, CONSTANTS, VARIABLES,
    IF, ELSIF, ELSE, THEN,
    WHILE, REPEAT, UNTIL, FOR, FROM, TO,
    INTEGER, REAL, BOOLEAN, CHARACTER, STRING, ARRAY,
    VAR,

    ADD, MINUS, MULT, DIV, MOD,
    GTHAN, LTHAN, GETO, LETO, EQUAL, NEQUAL, NOT, AND, OR,
    ASSIGN, COLON, SEMICOLON, COMMA, LPAREN, RPAREN
}
