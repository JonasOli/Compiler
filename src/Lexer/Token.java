package Lexer;

class Token {
    private int line;
    private int column;
    private String lexema;
    private Tags class_tag;

    Token(Tags class_tag, String lexema, int line, int column){
        this.class_tag = class_tag;
        this.lexema = lexema;
        this.line = line;
        this.column = column;
    }

    public String getLexema() {
        return lexema;
    }

    public Tags getClass_tag() {
        return class_tag;
    }

    public void PrintLexema (){
        System.out.println("<" + this.class_tag + ", \"" + this.lexema + "\" linha: " + this.line + " e coluna: " + this.column + ">");
    }
}