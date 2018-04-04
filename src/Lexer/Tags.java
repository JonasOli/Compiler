package Lexer;

enum Tags {

    //operadores

    OP_EQ,
    OP_NE,
    OP_GT,
    OP_LT,

    //Símbolos

    SMB_OBC,
    SMB_CBC,
    SMB_OPA,
    SMB_CPA,
    OP_GE,
    OP_LE,
    OP_AD,
    OP_MIN,
    OP_MUL,
    OP_DIV,
    OP_ASS,
    SMB_COM,
    SMB_SEM,

    //Palavras-chave:

    KW,

    //Identificadores:

    ID,

    //Literal:

    LIT,

    //Constantes:

    CON_NUM,
    CON_CHAR,

    EOF
}