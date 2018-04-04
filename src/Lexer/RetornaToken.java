package Lexer;

import java.io.IOException;
import java.io.RandomAccessFile;

class RetornaToken {
    // Fecha instance_file de input_data
    private RandomAccessFile file;
    private static final int file_end = -1; // contante para fim do arquivo
    private static int lookahead; // armazena o último caractere lido do arquivo
    private static int line = 1; // contador de linhas
    private static int column = 1; // contador de linhas
    private TabelaSimbolos TS = new TabelaSimbolos(); // tabela de simbolos

    RetornaToken (RandomAccessFile file){
        this.file = file;
    }

    //Reporta erro para o usuário
    private void sinalizaErro(String mensagem) {
        System.err.println(mensagem);
    }

    //Volta uma posição do buffer de leitura
    private void returnPointer() {
        try {
            // Não é necessário retornar o ponteiro em caso de Fim de Arquivo
            if (lookahead != file_end) {
                file.seek(file.getFilePointer() - 1);
            }
        } catch (IOException err) {
            System.err.println("Falha ao retornar a leitura\n" + err);
        }
    }
    public Token proxToken() {

        StringBuilder lexema = new StringBuilder();
        int estado = 1;
        char c;

        while (true) {
            c = '\u0000'; // null char
            column++;
            // avanca caractere
            try {
                lookahead = file.read();
                if (lookahead != file_end) {
                    c = (char) lookahead;
                }
            } catch (IOException err) {
                System.out.println("Erro na leitura do arquivo");
            }
            switch (estado) {
                case 1:
                    if (lookahead == file_end) {
                        return new Token(Tags.EOF, "EOF", 1, 1);
                    } else if (c == ' ' || c == '\t' || c == '\r') {
                        estado = 1;
                    } else if (c == '\n') {
                        column = 1;
                        line++;
                    } else if (Character.isLetter(c)) {
                        lexema.append(c);
                        estado = 2;
                    } else if (Character.isDigit(c)) {
                        lexema.append(c);
                        estado = 3;
                    } else if (c == '+') {
                        return new Token(Tags.OP_AD, "+", line, column);
                    } else if (c == '-') {
                        return new Token(Tags.OP_MIN, "-", line, column);
                    } else if (c == '*') {
                        return new Token(Tags.OP_MUL, "*", line, column);
                    } else if (c == '/') {
                        return new Token(Tags.OP_DIV, "/", line, column);
                    } else if (c == '(') {
                        return new Token(Tags.SMB_OPA, "(", line, column);
                    } else if (c == ')') {
                        return new Token(Tags.SMB_CPA, ")", line, column);
                    } else if (c == '{') {
                        return new Token(Tags.SMB_OBC, "{", line, column);
                    } else if (c == '}') {
                        return new Token(Tags.SMB_CBC, "}", line, column);
                    } else if (c == ',') {
                        return new Token(Tags.SMB_COM, ",", line, column);
                    } else if (c == ';') {
                        return new Token(Tags.SMB_SEM, ";", line, column);
                    } else if (c == '=') {
                        estado = 4;
                    } else if (c == '"') {
                        estado = 5;
                    } else if (c == '>') {
                        estado = 6;
                    } else if (c == '<') {
                        estado = 7;
                    } else if (c == '!') {
                        estado = 8;
                    } else if (c == '\'') {
                        estado = 9;
                    } else {
                        sinalizaErro("Caractere invalido " + c + " na linha " + line + " e coluna " + column);
                        return null;
                    }
                    break;
                case 2:
                    if (Character.isLetterOrDigit(c)) {
                        lexema.append(c);
                        Token tn = TS.retornaToken(lexema.toString());
                        if (tn != null) {
                            return new Token(Tags.KW, lexema.toString(), line, column);
                        }
                    } else {
                        returnPointer();
                        return new Token(Tags.ID, lexema.toString(), line, column);
                    }
                    break;
                case 3:
                    if (Character.isDigit(c) || c == '.') {
                        lexema.append(c);
                    } else {
                        returnPointer();
                        return new Token(Tags.CON_NUM, lexema.toString(), line, column - 1);
                    }
                    break;
                case 4:
                    if (c == '=') {
                        return new Token(Tags.OP_EQ, "==", line, column);
                    } else {
                        returnPointer();
                        return new Token(Tags.OP_ASS, "=", line, column);
                    }
                case 5:
                    if (c == '"') {
                        return new Token(Tags.LIT, lexema.toString(), line, column);
                    } else if (lookahead == file_end) {
                        sinalizaErro("String não fechada na linha " + line + " e coluna " + column);
                        estado = 1;
                    } else {
                        lexema.append(c);
                    }
                    break;
                case 6:
                    if (c == '=') {
                        return new Token(Tags.OP_GE, ">=", line, column);
                    } else {
                        returnPointer();
                        return new Token(Tags.OP_GT, ">", line, column);
                    }
                case 7:
                    if (c == '=') {
                        return new Token(Tags.OP_LE, "<=", line, column);
                    } else {
                        returnPointer();
                        return new Token(Tags.OP_LT, "<", line, column);
                    }
                case 8:
                    if (c == '=') {
                        return new Token(Tags.OP_NE, "!=", line, column);
                    } else {
                        sinalizaErro("Caractere \"=\" esperado na linha " + line + " e coluna " + column);
                        estado = 1;
                    }
                    break;
                case 9:
                    if (c == '\'') {
                        return new Token(Tags.CON_CHAR, lexema.toString(), line, column);
                    }
            }
        }
    }
}
