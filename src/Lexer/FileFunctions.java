package Lexer;

/*
 * @author Jonas Oliveira da Silva Filho
*/

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileFunctions {
    public RandomAccessFile openFile(String fileName){
        try {
            return new RandomAccessFile(fileName, "r");
        } catch (IOException e) {
            System.err.println("Erro na abertura do arquivo!!" + e.getMessage());
            return null;
        }
    }

    public void closeFile(RandomAccessFile fileName) {
        try{
            fileName.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar o arquivo!!");
        }
    }
}

