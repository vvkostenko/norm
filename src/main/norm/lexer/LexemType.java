package norm.lexer;

public enum LexemType {
    /**
     * < | > | <= | >= | neq | and | or | eq
     */
    BOOL_SIGN,

    /**
     * <space_symbol> | \n | \t
     */
    SPACE,

    /**
     * int
     */
    TYPE,

    /**
     * true | false
     */
    BOOL_CONSTANT,

    /**
     * + | - | * | /
     */
    ARIPHMETIC_CONSTANT,

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
     * array | if | while | else
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
