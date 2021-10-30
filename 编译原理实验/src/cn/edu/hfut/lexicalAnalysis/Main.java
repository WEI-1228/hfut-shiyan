package cn.edu.hfut.lexicalAnalysis;

import java.io.*;
import java.util.*;

public class Main {
    Map<String, Integer> KeySet;
    Map<String, Integer> sepSet;
    Map<String, Integer> opSet;
    Map<String, Integer> relSet;
    Map<String, Integer> idSet;
    Map<Double, Integer> numSet;
    List<String> result;
    int ptr;

    Map<Integer, String> typeMap;

    /*初始化程序*/
    public void init() throws IOException {
        /*符号类别初始化*/
        typeMap = new HashMap<>();
        typeMap.put(1, "关键字");
        typeMap.put(2, "分隔符");
        typeMap.put(3, "算数运算符");
        typeMap.put(4, "关系运算符");
        typeMap.put(5, "无符号数");
        typeMap.put(6, "标识符");
        typeMap.put(0, "Error");
        /*初始化指针*/
        ptr = 0;
        /*读取关键字文件*/
        KeySet = new HashMap<>();
        String file = "L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\lexicalAnalysis\\key.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String s = "";
        while ((s = bufferedReader.readLine()) != null) KeySet.put(s, ptr++);
        bufferedReader.close();

        /*读取分隔符文件*/
        sepSet = new HashMap<>();
        file = "L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\lexicalAnalysis\\sep.txt";
        bufferedReader = new BufferedReader(new FileReader(file));
        while ((s = bufferedReader.readLine()) != null) sepSet.put(s, ptr++);
        bufferedReader.close();

        /*读取运算符文件*/
        opSet = new HashMap<>();
        file = "L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\lexicalAnalysis\\op.txt";
        bufferedReader = new BufferedReader(new FileReader(file));
        while ((s = bufferedReader.readLine()) != null) opSet.put(s, ptr++);
        bufferedReader.close();

        /*读取关系运算符文件*/
        relSet = new HashMap<>();
        file = "L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\lexicalAnalysis\\rel.txt";
        bufferedReader = new BufferedReader(new FileReader(file));
        while ((s = bufferedReader.readLine()) != null) relSet.put(s, ptr++);
        bufferedReader.close();

        /*标识符表与常数表初始化*/
        idSet = new HashMap<>();
        numSet = new HashMap<>();

        /*输出结果*/
        result = new LinkedList<>();
    }

    /*主分析程序*/
    public void analysis() throws IOException {
        init();
        String file = "L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\lexicalAnalysis\\src.txt";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s;
        String tmp;
        int row = -1;
        char c;
        /*读取每一行进行分析*/
        while ((s = reader.readLine()) != null) {
            ++row;
            char[] sentence = s.toCharArray();
            int cur = 0;
            /*分析一行中所有字符*/
            while (cur < sentence.length) {
                c = sentence[cur];
                if (c == ' ') ++cur;//空字符丢弃
                else if (Character.isAlphabetic(c)) {    //接受一个关键字或标识符
                    tmp = "";
                    int col = cur;
                    while (cur < sentence.length && sentence[cur] != ' ') {   //接受代码
                        if (Character.isAlphabetic(sentence[cur]) || Character.isDigit(sentence[cur])) {
                            tmp += sentence[cur++];
                        } else break;
                    }
                    if (KeySet.containsKey(tmp)) {   //如果是关键字就直接接受
                        insert(tmp, 1, row, col);
                    } else {
                        if (!idSet.containsKey(tmp)) idSet.put(tmp, ptr++);    //如果原先不存在则先插入表中
                        insert(tmp, 6, row, col);
                    }
                } else if (Character.isDigit(c)) {    //接受一个数字
                    tmp = "";
                    int col = cur;
                    boolean flag = true;
                    boolean hasDot = false;
                    while (cur < sentence.length && sentence[cur] != ' ') {    //接受代码
                        if (Character.isDigit(sentence[cur]) || sentence[cur] == '.'){
                            if(sentence[cur]=='.'){
                                if(!hasDot)hasDot = true;
                                else {
                                    flag = false;
                                    break;
                                }
                            }
                            tmp += sentence[cur++];
                        }
                        else if(Character.isAlphabetic(sentence[cur])) {
                            flag = false;
                            break;
                        }else break;
                    }
                    if (flag) {
                        if (!numSet.containsKey(Double.parseDouble(tmp)))
                            numSet.put(Double.parseDouble(tmp), ptr++);   //如果不存在先插入表中
                        insert(tmp, 5, row, col);
                    }else {
                        while (cur<sentence.length&&(Character.isAlphabetic(sentence[cur])||Character.isDigit(sentence[cur]))){
                            tmp += sentence[cur++];
                        }
                        insert(tmp, 0, row, cur);
                    }
                } else if (sepSet.containsKey(c + "")) {         //接受分隔符
                    insert(c + "", 2, row, cur++);
                } else if (opSet.containsKey(c + "")) {     //接受算数运算符
                    if (c == '/') {        //如果是/的情况要特殊处理
                        if (cur + 1 < sentence.length && sentence[cur + 1] == c) {     //如果是//注释，就丢弃
                            cur = sentence.length;
                        } else if (cur + 1 < sentence.length && sentence[cur + 1] == '*') {  //如果找到的是/*，就需要继续向下寻找*/
                            cur = cur + 3;
                            boolean flag = true;
                            while (flag) {
                                if (cur >= sentence.length) {
                                    sentence = reader.readLine().toCharArray();
                                    row++;
                                    cur = 1;
                                    continue;
                                }
                                for (; cur < sentence.length; cur++) {
                                    if (sentence[cur - 1] == '*' && sentence[cur] == '/') {
                                        flag = false;
                                        cur++;
                                        break;
                                    }
                                }
                            }
                        } else {  //否则接受/
                            insert("" + c, 3, row, cur++);
                        }
                    } else if (cur + 1 < sentence.length && sentence[cur + 1] == c) {   //接受其他运算符
                        insert("" + c + c, 3, row, cur++);
                        cur++;
                    } else {
                        insert("" + c, 3, row, cur++);
                    }
                } else if (relSet.containsKey(c + "")) {     //接受关系运算符
                    if (cur + 1 < sentence.length && sentence[cur + 1] == '=') {
                        insert(c + "=", 4, row, cur++);
                        cur++;
                    } else {
                        insert(c + "", 4, row, cur++);
                    }
                } else if (c == '!') {      //！=的情况要特殊处理
                    if (cur + 1 < sentence.length && sentence[cur + 1] == '=') {
                        insert(c + "=", 4, row, cur++);
                        cur++;
                    } else {
                        insert("Error", 0, row, cur++);
                    }
                } else {            //接受失败输出错误
                    insert("Error", 0, row, cur++);
                }
            }

        }
        reader.close();
    }


    /*插入输出表*/
    public void insert(String name, int type, int row, int col) {
        row++;
        col++;
        result.add(name);
        if(type==0)name = "Error";
        String s = "(" + type + "," + name + ")";
        result.add(s);
        result.add(typeMap.get(type));
        s = "(" + row + "," + col + ")";
        result.add(s);
    }

    public String[][] createTable(){
        String[][] tables = new String[result.size()/4][];
        for(int i = 0;i<tables.length;i++)tables[i] = new String[4];
        int count = -1;
        for(String s:result){
            count++;
            tables[count/4][count%4] = s;
        }

        return tables;
    }

    /*输出结果*/


}
