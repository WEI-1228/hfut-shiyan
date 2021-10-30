package cn.edu.hfut.LL1;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*分析器*/
public class LL1 {
    /*first集*/
    Map<Character, HashSet<Character>> first;
    /*follow集*/
    Map<Character, HashSet<Character>> follow;
    /*产生式集*/
    Map<Character, HashSet<String>> produce;
    /*预测表*/
    Map<Pair<Character, Character>, String> predictTable;

    /*初始化程序*/
    public LL1() throws IOException {
        init();
        readProduce();
        //preProsess();
        //showPro();
        initFiestAndFollow();
        /*创建first集*/
        createFirst();
        /*创建follow集*/
        createFollow();
        /*创建预测表*/
        createPredictTable();
    }

    private void showPro() {
        for(char c:produce.keySet()){
            HashSet<String> set = produce.get(c);
            System.out.print(c+"->");
            for(String s:set){
                System.out.print(s+"|");
            }
            System.out.println();
        }
    }

    /*初始化数据结构*/
    public void init() throws IOException {
        first = new HashMap<>();
        follow = new HashMap<>();
        produce = new TreeMap<>();
        predictTable = new HashMap<>();
    }

    /*读取产生式并初始化每个集合的键*/
    public void readProduce() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\LL1\\grammer.txt"));
        String s = "";
        while ((s = reader.readLine()) != null) {
            if (produce.get(s.charAt(0)) == null) {
                produce.put(s.charAt(0), new HashSet<>());
            }
            Set t = produce.get(s.charAt(0));
            s = s.substring(3);
            String[] split = s.split("\\|");
            for (String s1 : split)
                t.add(s1);
        }
        reader.close();
    }

    public void initFiestAndFollow() {
        for (char c : produce.keySet()) {
            first.put(c, new HashSet<>());
            follow.put(c, new HashSet<>());
        }
    }

    /*通过产生式的所有左边字母创建对应的first集*/
    public void createFirst() {
        for (char c : produce.keySet()) createFirst(c);
    }

    /*创建字符c对应的first*/
    public void createFirst(char c) {
        HashSet<String> set = produce.get(c);   //字符c对应的产生式集合
        HashSet<Character> firstSet = first.get(c);  //字符c的first集
        if (!firstSet.isEmpty()) return;       //如果first集已经存在说明创建过了，直接退出
        for (String s : set) {        //遍历每条产生式
            if (Character.isUpperCase(s.charAt(0))) {     //如果第一个字符是非终结符
                int count = 0;   //对能产生空的非终结符进行计数
                boolean flag = true;  //标志
                while (flag && count < s.length()) {
                    flag = false;
                    Set<Character> first = getFirst(s.charAt(count));  //右边非终结符的first集
                    for (Character fc : first) {
                        if (fc != '^') firstSet.add(fc);     //如果不是空就加入到c的first集中
                        else {
                            flag = true; //如果能推出空，说明还需要继续将下一个非终结符的first加入c，flag标为true
                            count++;  //非终结符加一
                        }
                    }
                }
                //如果所有的产生式右边每个非终结符都能推出空，那么将空加入c的first集
                if (count == s.length()) firstSet.add('^');
            } else {
                firstSet.add(s.charAt(0));//如果第一个字符是终结符直接添加进first集
            }
        }
    }

    /*获取字符c对应的first集*/
    public Set<Character> getFirst(char c) {
        HashSet<Character> set = first.get(c);
        /*如果first集没构建，那么就进行构建，相当于一个分开的递归函数*/
        if (set.isEmpty()) {
            createFirst(c);
        }
        return set;
    }

    /*创建follow集*/
    public void createFollow() {
        /*开始符号先加入'#'*/
        follow.get('E').add('#');
        boolean flag = true; //flag用来表示一轮循环之内，是否有元素的follow集改变，如果改变了则需要继续循环，否则停止
        /*一直循环直到follow集不再改变位置*/
        while (flag) {
            flag = false;
            for (char c : produce.keySet()) {    //c是所有非终结符
                HashSet<String> set = produce.get(c);  //非终结符c的产生式
                for (String s : set) {        //遍历非终结符c的所有产生式
                    char[] chars = s.toCharArray();
                    int i = 1;      //当前指针所在位置
                    for (; i < chars.length; i++) {
                        if (Character.isUpperCase(chars[i - 1])) {//如果当前指针是非终结符
                            if (Character.isUpperCase(chars[i])) {//并且前一个也是非终结符

                                HashSet<Character> first = this.first.get(chars[i]);
                                HashSet<Character> follow = this.follow.get(chars[i - 1]);
                                for (char fc : first) {          //就把当前的非终结符的first集加入前一个的follow集
                                    if (fc != '^') {
                                        if (!follow.contains(fc)) {
                                            follow.add(fc);
                                            flag = true;      //有元素改变，需要继续循环
                                        }
                                    }
                                }
                            } else {
                                if (!follow.get(chars[i - 1]).contains(chars[i])) {  //如果前一个字符follow没有当前的终结符
                                    follow.get(chars[i - 1]).add(chars[i]);
                                    flag = true;     //有元素改变，需要继续循环
                                }
                            }
                        }
                    }
                    /*A->aB，需要将follow(A)加入follow(B)*/
                    if (Character.isUpperCase(chars[i - 1])) {
                        HashSet<Character> followSet = follow.get(c);
                        HashSet<Character> set1 = follow.get(chars[i - 1]);
                        for (char fc : followSet) {
                            if (!set1.contains(fc)) {
                                set1.add(fc);
                                flag = true;  //有元素改变，需要继续循环
                            }
                        }
                        int j = i - 1;
                        while (j - 1 >= 0 && true) {
                            /*c->aBd，c->空*，需要将follow(c)加入follow(B)/
                            /*如果最后的非终结符能推出空，需要将c，也就是产生式左边的非终结符，的follow加入前一个非终结符*/
                            if (first.get(chars[j]).contains('^') && Character.isUpperCase(chars[j])) {
                                if (Character.isUpperCase(chars[j - 1])) {
                                    set1 = follow.get(chars[j - 1]);
                                    for (char fc : followSet) {
                                        if (!set1.contains(fc)) {
                                            set1.add(fc);
                                            flag = true;
                                        }
                                    }
                                    --j;
                                } else break;
                            } else break;
                        }
                    }
                }
            }
        }
    }

    /*创建预测表*/
    public void createPredictTable() {
        for (char c : produce.keySet()) {
            HashSet<String> set = produce.get(c);
            for (String s : set) {     //遍历所有c->s的产生式
                if (Character.isUpperCase(s.charAt(0))) { //如果开头是非终结符，c->A...
                    HashSet<Character> firstSet = first.get(s.charAt(0));
                    for (char fc : firstSet) {
                        /*将c->s加入(c,非空的first(A)表中*/
                        if (fc != '^') predictTable.put(new Pair<>(c, fc), c + "->" + s);
                        else {
                            /*否则加入follow(c)*/
                            HashSet<Character> followSet = follow.get(c);
                            for (char foc : followSet) predictTable.put(new Pair<>(c, foc), c + "->" + s);
                        }
                    }
                } else {
                    /*如果是终结符，直接加入表中*/
                    if (s.charAt(0) != '^') predictTable.put(new Pair<>(c, s.charAt(0)), c + "->" + s);
                    else {
                        /*如果是空，将c->空加入follow(c)*/
                        HashSet<Character> followSet = follow.get(c);
                        for (char foc : followSet) predictTable.put(new Pair<>(c, foc), c + "->" + s);
                    }
                }
            }
        }
    }

    public void preProsess() {
        Set<Character> keySet = new HashSet<>();
        for (char c : produce.keySet()) keySet.add(c);
        for (char i : keySet) {
            for (char j : produce.keySet()) {
                if (j >= i) break;
                Set<String> set = new HashSet<>();
                HashSet<String> set2 = produce.get(i);
                for (String s : set2) set.add(s);
                for (String s : set) {
                    if (s.charAt(0) == j) {
                        set2.remove(s);
                        s = s.substring(1);
                        HashSet<String> set1 = produce.get(j);
                        for (String s1 : set1) {
                            set2.add(s1 + s);
                        }
                    }
                }
            }
            Set<String> digui = new HashSet<>();
            Set<String> nodigui = new HashSet<>();
            for (String s : produce.get(i)) {
                if (s.charAt(0) == i) digui.add(s.substring(1));
                else nodigui.add(s);
            }

            if (digui.size() != 0) {
                char newchar = 'Z';
                HashSet<String> newset = new HashSet<>();
                while (produce.containsKey(newchar)) newchar--;
                HashSet<String> set = produce.get(i);
                set.clear();
                for (String s : nodigui) set.add(s + newchar);
                for (String s : digui) newset.add(s + newchar);
                newset.add("^");
                produce.put(newchar, newset);
            }
        }
    }

    /*分析程序*/
    public LinkedList<String[]> analysis(String s) {
        LinkedList<String[]>strings = new LinkedList<>();
        StringBuilder builder = new StringBuilder();
        /*输出字符串各个部分*/
        String output;
        String stackStr = "";
        String action;
        String remainStr;
        String produeceStr;
        int step = 0;
        s += "#";
        Stack<Character> stack = new Stack<>();
        stack.push('#');
        stackStr += '#';
        stack.push('E');
        stackStr += 'E';
        char[] input = s.toCharArray();
        int cur = 0;
        remainStr = s.substring(cur);
//        builder.append(String.format("%-5s %-10s %-10s %-10s %-15s\n", "步骤", "分析栈", "剩余输入符号串", "所用产生式", "动作"));
        String[]row = {step+"", stackStr, remainStr, "", "初始化"};
        strings.add(row);
        while (cur < s.length()) {
            output = "";
            produeceStr = "";
            action = "";
            step++;
            if (input[cur] == stack.peek()) {
                cur++;
                remainStr = s.substring(cur);
                stack.pop();
                stackStr = stackStr.substring(0, stackStr.length() - 1);
                action = "GETNEXT(I)";
            } else {
                String s1 = predictTable.getOrDefault(new Pair<>(stack.pop(), input[cur]), "Error");
                stackStr = stackStr.substring(0, stackStr.length() - 1);
                if (s1.equals("Error")) {
                    row = new String[]{"Error","Error","Error","Error","Error"};
                    strings.add(row);
                    return strings;
                } else {
                    produeceStr = s1;
                    s1 = s1.substring(3);
                    if (!s1.equals("^")) {
                        action = "POP,PUSH(";
                        char[] chars = s1.toCharArray();
                        for (int i = chars.length - 1; i >= 0; i--) {
                            stack.push(chars[i]);
                            stackStr += chars[i];
                            action += chars[i];
                        }
                        action += ")";
                    } else action = "POP";
                }
            }

            /*最后一步全部输出完成*/
            if (cur == s.length()) {
                stackStr = "Finish!";
                remainStr = "Finish!";
                produeceStr = "Finish!";
            }

            /*将各个部分输出组合*/
            row = new String[]{step+"", stackStr, remainStr, produeceStr, action};
            strings.add(row);
        }

        return strings;
    }

    public void showTable(){
        Set<Pair<Character, Character>> pairs = predictTable.keySet();
        for(Pair pair:pairs){
            char key = (char) pair.getKey();
            char val = (char) pair.getValue();
            System.out.println("("+key+","+val+")"+"->"+"("+predictTable.get(new Pair<>(key,val))+")");
        }
    }


    public Map<Pair<Character, Character>, String> getTable(){
        return predictTable;
    }


    public static void main(String[] args) throws IOException {
        LL1 ll1 = new LL1();
        ll1.showTable();
    }
}
