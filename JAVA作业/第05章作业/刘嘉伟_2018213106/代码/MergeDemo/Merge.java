package JAVA_HOMEWORK.Chapter_05.MergeDemo;

import java.io.*;

public class Merge {
    public static void main(String[] args) throws IOException {
        File txt1 = new File("L:\\first\\src\\JAVA_HOMEWORK\\Chapter_05\\MergeDemo\\Mytxt01.txt");
        File txt2 = new File("L:\\first\\src\\JAVA_HOMEWORK\\Chapter_05\\MergeDemo\\Mytxt02.txt");
        File txt = new File("L:\\first\\src\\JAVA_HOMEWORK\\Chapter_05\\MergeDemo\\Merge.txt");
        BufferedReader InTxt1 = new BufferedReader(new FileReader(txt1));
        BufferedReader InTxt2 = new BufferedReader(new FileReader(txt2));
        BufferedWriter OutTxt = new BufferedWriter(new FileWriter(txt));
        String s = null;
        while((s = InTxt1.readLine()) != null){
            OutTxt.write(s);
            OutTxt.newLine();
        }
        while((s = InTxt2.readLine()) != null){
            OutTxt.write(s);
            OutTxt.newLine();
        }
        InTxt1.close();
        InTxt2.close();
        OutTxt.close();
    }
}
