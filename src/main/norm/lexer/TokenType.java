package norm.lexer;

public enum TokenType {
    /**
     * < | > | <= | >=
     */
    COMPARE_SIGN,

    /**
     * neq | and | or | eq
     */
    BOOL_SIGN,

    /**
     * int
     */
    TYPE,

    /**
     * true | false
     */
    BOOL_CONSTANT,

    /**
     * + | - | * | / | %
     */
    ARIPHMETIC_CONSTANT,

    /**
     * =
     */
    ASSIGNMENT_CONST,

    /**
     * (
     */
    ROUND_BRAKED_OPEN,

    /**
     * )
     */
    ROUND_BRAKED_CLOSE,

    /**
     * {
     */
    FIGURED_BRAKED_OPEN,

    /**
     * }
     */
    FIGURED_BRAKED_CLOSE,

    /**
     * array | if | while | else | sc | out
     */
    KEYWORD,

    /**
     * :
     */
    TWO_DOTS,

    /**
     * ;
     */
    COMMA_DOT,

    /**
     * ,
     */
    COMMA,

    /**
     * идентификатор переменной
     */
    VAR_IDENTIFIER,

    /**
     * Целое число
     */
    NUMBER,

    /**
     * Конец файла
     */
    EOF
}
