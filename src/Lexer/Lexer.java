package Lexer;

/**
 * @author Jonas Oliveira da Silva Filho
 */

import java.io.RandomAccessFile;

public class Lexer {
    public static void main(String[] args) {
        FileFunctions ff = new FileFunctions(); /* * Classe com funções de abertura e fechamento do arquivo */
        RandomAccessFile file = ff.openFile("teste_erro1.pasC"); /* * Criando um ramdomAccessFile */
        RetornaToken tokens = new RetornaToken(file); /* * Criando um objeto da classe que reconhece tokens */
        Token token;
        TabelaSimbolos TS = new TabelaSimbolos();

        /* * Enquanto o retorno do Token não for null e não for fim de arquivo: */
        do {
            token = tokens.proxToken();

            /* * Imprime os tokens retornados da classe RetornaToken() */
            if (token != null && token.getClass_tag() != Tags.EOF) {
                token.PrintLexema();
                if (token.getClass_tag() == Tags.ID) {
                    TS.setTabela_simbolos(token);
                }
            }
        } while (token != null && token.getClass_tag() != Tags.EOF);

        /* * Fechamento do arquivo */

        System.out.println("\n\nTabela de Simbolos:\n");
        TS.printTabelaSimbolos();

        ff.closeFile(file);
    }
}