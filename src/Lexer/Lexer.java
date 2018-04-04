package Lexer;

import java.io.RandomAccessFile;

public class Lexer {
    public static void main(String[] args) {
        FileFunctions ff = new FileFunctions();
        RandomAccessFile file = ff.openFile("test.txt");
        RetornaToken tokens = new RetornaToken(file);
        Token token;

        // Enquanto não houver erros ou não for fim de arquivo:
        do {
            token = tokens.proxToken();

            // Imprime token
            if (token != null && token.getClass_tag() != Tags.EOF) {
                token.PrintLexema();
                // Verificar se existe o lexema na tabela de símbolos
            }

        } while (token != null && token.getClass_tag() != Tags.EOF);
        ff.closeFile(file);
    }
}