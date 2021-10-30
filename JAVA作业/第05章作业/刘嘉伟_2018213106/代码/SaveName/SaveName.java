package JAVA_HOMEWORK.Chapter_05.SaveName;

import java.io.*;

public class SaveName {
    public static void main(String[] args) throws IOException {
        File file = new File("L:\\first\\src\\JAVA_HOMEWORK\\Chapter_05\\SaveName\\Table.txt");
        BufferedReader txtRead = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter txtWrite = new BufferedWriter(new FileWriter(file));
        String tmp = txtRead.readLine();
        while (!tmp.equals("")){
            txtWrite.write(tmp);
            txtWrite.newLine();
            tmp = txtRead.readLine();
        }
        txtRead.close();
        txtWrite.close();
    }
}
