/**
 * All tokens created from lexing must have a specified type.
 */

package LexerTools;

public enum tokenType
{
    WORD, NUMBER, EOL,
    INDENT, DEDENT,
    DEFINE, CONSTANTS, VARIABLES,
    IF, ELSIF, ELSE, THEN,
    WHILE, REPEAT, UNTIL, FOR, FROM, TO,
    INTEGER, REAL, BOOLEAN, CHARACTER, STRING, ARRAY,
    CHARLITERAL, STRINGLITERAL,
    VAR,

    ADD, MINUS, MULT, DIV, MOD,
    GTHAN, LTHAN, GETO, LETO, EQUAL, NEQUAL, NOT, AND, OR,
    ASSIGN, COLON, SEMICOLON, COMMA, LPAREN, RPAREN
}
