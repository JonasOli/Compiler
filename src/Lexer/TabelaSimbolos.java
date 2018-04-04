package Lexer;

import java.util.ArrayList;

class TabelaSimbolos {
    private ArrayList<Token> tabela_simbolos = new ArrayList<>();

    TabelaSimbolos () {
        Token word;

        word = new Token(Tags.KW, "program", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "if", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "else", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "while", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "write", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "read", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "num", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "char", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "not", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "or", 0, 0);
        this.tabela_simbolos.add(word);

        word = new Token(Tags.KW, "and", 0, 0);
        this.tabela_simbolos.add(word);
    }

    public Token retornaToken(String lexema) {
        for (Token token : tabela_simbolos) {
            if (token.getLexema().equals(lexema)) {
                return token;
            }
        }
        return null;
    }
}