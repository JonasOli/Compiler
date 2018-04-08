package Lexer;

/**
 * @author Jonas Oliveira da Silva Filho
*/

import java.io.IOException;
import java.io.RandomAccessFile;

class RetornaToken {
    private RandomAccessFile file;/** * Variavel local do arquivo */
    private static final int file_end = -1; /** * Contante para verificar se é fim do arquivo */
    private static int lookahead; /** * Armazena o último caractere lido do arquivo */
    private static int line = 1; /** * Contador de linhas */
    private static int column = 1; /** * Contador de colunas */
    private TabelaSimbolos TS = new TabelaSimbolos(); /** * Criando um objeto para a tabela de símbolos */

    /** * Construtor da classe RetornaToken() que recebe o arquivo ja aberto pela classe FileFunctions() */
    RetornaToken (RandomAccessFile file){
        this.file = file;
    }

    /** * Recebe uma String com um erro e reporta para o usuário */
    private void sinalizaErro(String mensagem) {
        System.err.println(mensagem);
    }

    /** * Volta uma posição do buffer de leitura */
    private void returnPointer() {
        try {
            /** * Se o buffer de leitura for igual ao final do arquivo não é necessario o retorno do ponteiro */
            if (lookahead != file_end) {
                file.seek(file.getFilePointer() - 1);
            }
        } catch (IOException err) {
            System.err.println("Falha ao retornar a leitura\n" + err);
        }
    }

    /** * Função que implementa um automato para reconhecer Tokens a partir dos caracteres lidos no arquivo */
    public Token proxToken() {

        StringBuilder lexema = new StringBuilder(); /** * Cria um objeto que controi uma string */
        int estado = 1; /** * variável caminhar pelas fases do automato */
        char c = '\u0000'; /** * Variável que recebe cada caractere do arquivo lido e é inicializada com um caractere null*/

        while (true) {
            column++;
            // Avança cada caractere enquanto não é o fim do arquivo
            try {
                lookahead = file.read();
                if (lookahead != file_end) {
                    c = (char) lookahead; // Atribui cada caractere do arquivo à variável 'c'
                }
            } catch (IOException err) {
                System.out.println("Erro na leitura do arquivo" + err); // Imprime um erro caso de erro na leitura do aquivo
            }

            switch (estado) {
                case 1:
                    if (lookahead == file_end) {
                        return new Token(Tags.EOF, "EOF", 1, 1);
                    } else if (c == '\t' || c == ' ') {
                        // estado 1
                    } else if (c == '\n' || c == '\r') {
                        column = 1;
                        line++;
                        estado = 1;
                    } else if (Character.isLetter(c)) {
                        lexema.append(c);
                        estado = 35;
                    } else if (Character.isDigit(c)) {
                        lexema.append(c);
                        estado = 25;
                    } else if (c == '+') {
                        // estado 14
                        return new Token(Tags.OP_AD, "+", line, column);
                    } else if (c == '-') {
                        // estado 13
                        return new Token(Tags.OP_MIN, "-", line, column);
                    } else if (c == '*') {
                        // estado 15
                        return new Token(Tags.OP_MUL, "*", line, column);
                    } else if (c == '/') {
                        estado = 16;
                    } else if (c == '(') {
                        // estado 21
                        return new Token(Tags.SMB_OPA, "(", line, column);
                    } else if (c == ')') {
                        // estado 22
                        return new Token(Tags.SMB_CPA, ")", line, column);
                    } else if (c == '{') {
                        // estado 19
                        return new Token(Tags.SMB_OBC, "{", line, column);
                    } else if (c == '}') {
                        // estado 20
                        return new Token(Tags.SMB_CBC, "}", line, column);
                    } else if (c == ',') {
                        // estado 23
                        return new Token(Tags.SMB_COM, ",", line, column);
                    } else if (c == ';') {
                        // estado 24
                        return new Token(Tags.SMB_SEM, ";", line, column);
                    } else if (c == '=') {
                        estado = 5;
                    } else if (c == '"') {
                        estado = 37;
                    } else if (c == '>') {
                        estado = 8;
                    } else if (c == '<') {
                        estado = 2;
                    } else if (c == '!') {
                        estado = 11;
                    } else if (c == '\'') {
                        estado = 40;
                    } else {
                        sinalizaErro("Caractere invalido " + c + " na linha " + line + " e coluna " + column);
                        estado = 1;
                    }
                break;

                case 2:
                    if (c == '=') {
                        // estado 3
                        return new Token(Tags.OP_LE, "<=", line, column);
                    } else {
                        // estado 4
                        returnPointer();
                        return new Token(Tags.OP_LT, "<", line, column);
                    }

                case 5:
                    if (c == '=') {
                        // estado 6
                        return new Token(Tags.OP_EQ, "==", line, column);
                    } else {
                        // estado 7
                        returnPointer();
                        return new Token(Tags.OP_ASS, "=", line, column);
                    }

                case 8:
                    if (c == '=') {
                        // estado 9
                        return new Token(Tags.OP_GE, ">=", line, column);
                    } else {
                        // estado 10
                        returnPointer();
                        return new Token(Tags.OP_GT, ">", line, column);
                    }

                case 11:
                    if (c == '=') {
                        // estado 12
                        return new Token(Tags.OP_NE, "!=", line, column);
                    } else {
                        returnPointer();
                        estado = 1;
                        sinalizaErro("Caractere \"=\" esperado na linha " + line + " e coluna " + column);
                    }
                break;

                case 16:
                    if (c == '*') {
                        estado = 17;
                    } else if (c == '/') {
                        estado = 21;
                    } else {
                        return new Token(Tags.OP_DIV, "/", line, column);
                    }
                    break;

                case 17:
                    if (c != '*') {
                        estado = 17;
                        if (lookahead == file_end) {
                            sinalizaErro("Esperado fechamento de comentario com */");
                            estado = 1;
                        }
                    } else {
                        estado = 19;
                    }
                    break;

                case 19:
                    if (c == '/') {
                        estado = 1;
                    } else {
                        if (lookahead == file_end) {
                            sinalizaErro("Esperado fechamento de comentario com */");
                            estado = 1;
                        } else {
                            estado = 17;
                        }
                    }
                    break;

                case 21:
                    if (Character.isLetterOrDigit(c)) {
                        // estado 22
                    } else if (c == '\n') {
                        returnPointer();
                        estado = 1;
                    }
                    break;

                case 25:
                    if (Character.isDigit(c)) {
                        // estado 25
                        lexema.append(c);
                    } else {
                        if (c == '.'){
                            lexema.append(c);
                            estado = 32;
                        } else {
                            // estado 26
                            returnPointer();
                            return new Token(Tags.CON_NUM, lexema.toString(), line, (column - 1));
                        }
                    }
                break;

                case 32:
                    if (Character.isDigit(c) || lookahead != file_end){
                        lexema.append(c);
                        estado = 25;
                    } else {
                        sinalizaErro("Erro");
                    }

                case 35:
                    if (Character.isLetterOrDigit(c) && lookahead != file_end) {
                        // estado 35
                        lexema.append(c);
                    } else {
                        returnPointer();
                        Token tn = TS.retornaToken(lexema.toString());
                        if (tn == null) {
                            return new Token(Tags.ID, lexema.toString(), line, column);
                        } else {
                            // estado 36
                            return new Token(Tags.KW, lexema.toString(), line, column);
                        }
                    }
                    break;

                case 37:
                    if (c == '"') {
                        // estado 32
                        if (lexema.length() == 0) {
                            lexema.setLength(0);
                            sinalizaErro("String não pode ser vazia na linha " + line + " e coluna " + column);
                            estado = 1;
                        } else {
                            lexema.append(c);
                            return new Token(Tags.LIT, lexema.toString().split("\"")[0], line, column);
                        }
                    } else if (lookahead == file_end) {
                        estado = 1;
                        sinalizaErro("String não fechada na linha " + line + " e coluna " + column);
                    } else {
                        if (c == '\n') {
                            // estado 31
                            line++;
                            column = 1;
                            sinalizaErro("String não deve ser fechada em outra linha!! Linha " + line + " e coluna " + column);
                        } else {
                            lexema.append(c);
                        }
                    }
                    break;

                case 40:
                    if (c == '\'') {
                        if (lexema.length() > 1) {
                            sinalizaErro("Caractere deve ter apenas um digito!! Linha: " + line + " e coluna: " + column);
                            estado = 1;
                            lexema.setLength(0);
                        } else {
                            lexema.append(c);
                            return new Token(Tags.CON_CHAR, lexema.toString().split("\'")[0], line, column);
                        }
                    } else if (lookahead == file_end) {
                        lexema.setLength(0);
                        sinalizaErro("Caractere não fechado na linha: " + line + " e coluna: " + (column - 1));
                        estado = 1;
                    } else {
                        if (c == '\n') {
                            line++;
                            column = 1;
                        } else {
                            lexema.append(c);
                        }
                    }
                    break;
            }
        }
    }
}