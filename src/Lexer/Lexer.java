package Lexer;

/**
 * @author Jonas Oliveira da Silva Filho
 */

import java.io.RandomAccessFile;

public class Lexer {
    public static void main(String[] args) {
        FileFunctions ff = new FileFunctions(); /* * Classe com funções de abertura e fechamento do arquivo */
        RandomAccessFile file = ff.openFile("test.txt"); /* * Criando um ramdomAccessFile */
        RetornaToken tokens = new RetornaToken(file); /* * Criando um objeto da classe que reconhece tokens */
        Token token;

        /* * Enquanto o retorno do Token não for null e não for fim de arquivo: */
        do {
            token = tokens.proxToken();

            /* * Imprime os tokens retornados da classe RetornaToken() */
            if (token != null && token.getClass_tag() != Tags.EOF) {
                token.PrintLexema();
            }

        } while (token != null && token.getClass_tag() != Tags.EOF);

        /* * Fechamento do arquivo */
        ff.closeFile(file);
    }
}