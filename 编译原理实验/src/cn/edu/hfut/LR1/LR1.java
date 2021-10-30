package cn.edu.hfut.LR1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LR1 {
    /*first集*/
    Map<Character, HashSet<Character>> first;
    /*产生式集*/
    Map<Character, HashSet<String>> produce;
    /*dfa状态集合*/
    Map<Integer/*状态名*/, Map<Character/*非终结符*/, Map<String/*产生式右边*/, Set<Character>/*展望信息*/>>> dfa;
    /*dfa状态转换表*/
    Map<Integer, Map<Character, Integer>> transfer;
    /*产生式标号*/
    Map<Integer, String> IDMap;
    Map<String, Integer> MapID;
    /*ACTION表*/
    Map<Integer, Map<Character, String>> ACTION;
    /*GOTO表*/
    Map<Integer, Map<Character, Integer>> GOTO;

    /*自动机状态*/
    int i = 1;

    LR1() throws IOException {
        init();
        readProduce();
        createFirst();
        createDFA();
        createPredictTable();
    }

    public void init() throws IOException {
        first = new HashMap<>();
        produce = new HashMap<>();
        dfa = new HashMap<>();
        transfer = new HashMap<>();
        IDMap = new HashMap<>();
        MapID = new HashMap<>();
        ACTION = new HashMap<>();
        GOTO = new HashMap<>();
    }

    /*读取产生式并初始化每个集合的键*/
    public void readProduce() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\LR1\\grammer.txt"));
        String s = "";
        int count = 0;
        while ((s = reader.readLine()) != null) {
            if (produce.get(s.charAt(0)) == null) {
                produce.put(s.charAt(0), new HashSet<>());
                first.put(s.charAt(0), new HashSet<>());
            }
            Set t = produce.get(s.charAt(0));
            IDMap.put(count, s);
            MapID.put(s, count++);
            s = s.substring(3);
            String[] split = s.split("\\|");
            for (String s1 : split)
                t.add(s1);
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
                if (s.charAt(0) == c) continue;
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

    public void createDFA() {
        int condition = 0;
        dfa.put(condition, new HashMap<>());
        Map<String, Set<Character>> map = new HashMap<>();
        Set<Character> set = new HashSet<>();
        set.add('#');
        map.put(".E", set);
        dfa.get(condition).put('S', map);
        closure(dfa.get(condition));
        while (dfa.containsKey(condition)) {
            getNextCondition(condition);
            condition++;
        }
    }

    public void getNextCondition(int n) {
        Map<Character, Map<String, Set<Character>>> characterMapMap = dfa.get(n);
        /*临时状态*/
        Map<Character, Map<Character, Map<String, Set<Character>>>> tmpDFA = new HashMap<>();
        int m = 0;
        /*临时表，识别不同的字符变成不同的状态*/
        for (char c : characterMapMap.keySet()) {
            Map<String, Set<Character>> map = characterMapMap.get(c);
            for (String s : map.keySet()) {
                Set<Character> set = map.get(s);
                int index = s.indexOf(".");
                if (index == s.length() - 1) continue;
                else {
                    char[] chars = s.toCharArray();
                    if (!tmpDFA.containsKey(chars[index + 1])) {
                        tmpDFA.put(chars[index + 1], new HashMap<>());
                    }
                    Map<Character, Map<String, Set<Character>>> tmpMap = tmpDFA.get(chars[index + 1]);
                    /*map1是临时状态*/
                    if (!tmpMap.containsKey(c)) tmpMap.put(c, new HashMap<>());
                    Map<String, Set<Character>> map1 = tmpMap.get(c);
                    char tmp = chars[index + 1];
                    chars[index + 1] = chars[index];
                    chars[index] = tmp;
                    String t = new String(chars);
                    if (!map1.containsKey(t)) map1.put(t, new HashSet<>());
                    Set<Character> set1 = map1.get(t);
                    for (char nc : set) set1.add(nc);
                }
            }
        }
        if (!transfer.containsKey(n)) transfer.put(n, new HashMap<>());
        Map<Character, Integer> tranMap = transfer.get(n);
        for (char c : tmpDFA.keySet()) {
            Map<Character, Map<String, Set<Character>>> map = tmpDFA.get(c);
            closure(map);
            int id = cmpCondition(map);
            if (id == -1) {
                dfa.put(i, map);
                tranMap.put(c, i++);
            } else {
                tranMap.put(c, id);
            }
        }
    }

    public int cmpCondition(Map<Character, Map<String, Set<Character>>> map) {
        /*对所有现有状态进行比较*/
        for (int s : dfa.keySet()) {
            Map<Character, Map<String, Set<Character>>> characterMapMap = dfa.get(s);
            /*如果该状态的左边不完全包含新状态的左边，寻找下一个状态*/
            if (!characterMapMap.keySet().containsAll(map.keySet())) continue;
            if (characterMapMap.keySet().size() != map.keySet().size()) continue;
            boolean flag = true;
            for (char c : map.keySet()) {
                Map<String, Set<Character>> smallMap = map.get(c);
                Map<String, Set<Character>> bigMap = characterMapMap.get(c);
                if (!bigMap.keySet().containsAll(smallMap.keySet())) {
                    flag = false;
                    break;
                }
                if (bigMap.keySet().size() != smallMap.keySet().size()) {
                    flag = false;
                    break;
                }
                for (String s1 : smallMap.keySet()) {
                    Set<Character> smallset = smallMap.get(s1);
                    Set<Character> bigset = bigMap.get(s1);
                    if (smallset.size() != bigset.size()) flag = false;
                    if (!bigset.containsAll(smallset)) flag = false;
                }
            }
            if (flag) return s;
        }
        return -1;
    }

    /*characterMapMap为DFA的状态i的状态集合*/
    public void closure(Map<Character, Map<String, Set<Character>>> characterMapMap) {
        /*状态集变化标志*/
        boolean flag = true;
        /*c为左边非终结符*/
        while (flag) {
            flag = false;
            Set<Character> keySet = new HashSet<>();
            for (char c : characterMapMap.keySet()) keySet.add(c);
            for (char c : keySet) {
                /*map是当前非终结符c的状态集合*/
                Map<String, Set<Character>> map = characterMapMap.get(c);
                /*s为产生式右边*/
                for (String s : map.keySet()) {
                    /*set为展望字符串集合*/
                    Set<Character> set = map.get(s);
                    int cur = s.indexOf('.');
                    if (cur == s.length() - 1) continue;
                    char[] chars = s.toCharArray();
                    if (Character.isUpperCase(chars[cur + 1])) {
                        HashSet<String> set1 = produce.get(chars[cur + 1]);
                        if (!characterMapMap.containsKey(chars[cur + 1])) {
                            characterMapMap.put(chars[cur + 1], new HashMap<>());
                        }
                        /*map1是状态集合里，左边为chars[cur+1]的集合*/
                        Map<String, Set<Character>> map1 = characterMapMap.get(chars[cur + 1]);
                        for (String ls : set1) {
                            ls = "." + ls;
                            if (!map1.containsKey(ls)) {
                                map1.put(ls, new HashSet<>());
                            }
                            /*对于产生式ls右边的展望集合*/
                            Set<Character> set2 = map1.get(ls);
                            if (cur == s.length() - 2) {
                                /*原先的展望集合要放进去*/
                                for (char nc : set)
                                    if (!set2.contains(nc)) {
                                        set2.add(nc);
                                        flag = true;
                                    }
                            } else if (Character.isUpperCase(chars[cur + 2])) {
                                /*后一个非终结符的first集合要加进去*/
                                HashSet<Character> set3 = first.get(chars[cur + 2]);
                                for (char nc : set3) {
                                    if (nc == '^') continue;
                                    if (!set2.contains(nc)) {
                                        set2.add(nc);
                                        flag = true;
                                    }
                                }
                            } else {
                                /*后一个终结符就是展望信息*/
                                if (!set2.contains(chars[cur + 2])) {
                                    set2.add(chars[cur + 2]);
                                    flag = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void printMessage() {
        System.out.println("转换表");
        for (int i : transfer.keySet()) {
            Map<Character, Integer> map = transfer.get(i);
            for (char c : map.keySet()) {
                int n = map.get(c);
                System.out.println(i + "  +  " + c + "---->" + n);
            }
        }

        for (int n : dfa.keySet()) {
            Map<Character, Map<String, Set<Character>>> mapMap = dfa.get(n);
            System.out.println("状态" + n + ": ");
            for (char c : mapMap.keySet()) {
                Map<String, Set<Character>> map = mapMap.get(c);
                for (String s : map.keySet()) {
                    System.out.print(c + "->" + s + ", ");
                    Set<Character> set = map.get(s);
                    for (char cc : set) System.out.print(cc + "/");
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    public void createPredictTable() {
        for (int id : dfa.keySet()) {
            ACTION.put(id, new HashMap<>());
            Map<Character, String> actionMap = ACTION.get(id);
            GOTO.put(id, new HashMap<>());
            Map<Character, Integer> goMap = GOTO.get(id);
            Map<Character, Map<String, Set<Character>>> characterMapMap = dfa.get(id);
            for (char c : characterMapMap.keySet()) {
                Map<String, Set<Character>> map = characterMapMap.get(c);
                for (String s : map.keySet()) {
                    String proS = c + "->" + s;
                    if (proS.equals("S->E.")) {
                        if (actionMap.containsKey('#')) {
                            System.out.println("Error");
                            return;
                        }
                        actionMap.put('#', "acc");
                        continue;
                    }
                    int index = proS.indexOf('.');
                    if (index == proS.length() - 1) {
                        for (char sc : map.get(s)) {
                            if (actionMap.containsKey(sc)) {
                                System.out.println("Error");
                                return;
                            }
                            actionMap.put(sc, "r" + MapID.get(proS.substring(0, proS.length() - 1)));
                        }
                    } else {
                        char t = proS.charAt(index + 1);
                        if (actionMap.containsKey(t)) {
                            System.out.println("Error");
                            return;
                        }
                        if (Character.isUpperCase(t)) {
                            goMap.put(t, transfer.get(id).get(t));
                        } else {
                            actionMap.put(t, "s" + transfer.get(id).get(t));
                        }
                    }
                }
            }
        }
    }

    public void showTable() {
        for (int id : ACTION.keySet()) {
            Map<Character, String> mapAc = ACTION.get(id);
            Map<Character, Integer> mapGo = GOTO.get(id);
            System.out.print(id + "\t");
            for (char c : mapAc.keySet()) {
                System.out.print(c + "/" + mapAc.get(c) + "\t\t  ");
            }
            for (char c : mapGo.keySet()) {
                System.out.print(c + "/" + mapGo.get(c) + "\t\t  ");
            }
            System.out.println();
        }
    }

    public LinkedList<String[]> analysis(String s) throws IOException {
        LinkedList<String[]> table = new LinkedList<>();
        String[] row;
        int step = 2;
        String conStack = "";
        String strStack = "";
        String input = "";
        String explain = "";
        s += '#';
        Stack<Integer> conditionStack = new Stack<>();
        conditionStack.push(0);
        conStack += 0;
        Stack<Character> stack = new Stack<>();
        stack.push('#');
        strStack += '#';
        char[] chars = s.toCharArray();
        int cur = 0;
        input = s.substring(cur);
        row = new String[]{"1", "0", "#", s, "初始化"};
        table.add(row);
        while (cur < chars.length - 1 || stack.peek() != 'E') {
            Map<Character, String> condi = ACTION.get(conditionStack.peek());
            String act = condi.getOrDefault(chars[cur], "Error");
            if (act.equals("Error")) {
                row = new String[]{"Error", "Error", "Error", "Error", "Error"};
                table.add(row);
                return table;
            }
            if (act.charAt(0) == 's') {
                explain = "ACTION[" + conditionStack.peek() + ",";
                conditionStack.push(Integer.parseInt(act.substring(1)));
                conStack += conditionStack.peek();
                stack.push(chars[cur++]);
                strStack += stack.peek();
                input = s.substring(cur);
                explain += stack.peek() + "]=" + act + ",状态" + conditionStack.peek() + "入栈";
            } else {
                explain = act + ":";
                String s1 = IDMap.get(Integer.parseInt(act.substring(1)));
                explain += s1 + "归约,GOTO(";
                int len = s1.length() - 3;
                for (int i = 0; i < len; i++) {
                    stack.pop();
                    strStack = strStack.substring(0, stack.size());
                    conditionStack.pop();
                    conStack = conStack.substring(0, conditionStack.size());
                }
                char c = s1.charAt(0);
                explain += conditionStack.peek() + "," + c + ")=";
                stack.push(c);
                strStack += c;
                conditionStack.push(GOTO.get(conditionStack.peek()).get(stack.peek()));
                conStack += conditionStack.peek();
                explain += conditionStack.peek() + "入栈";
            }
            row = new String[]{step + "", conStack, strStack, input, explain};
            table.add(row);
            step++;
        }
        row = new String[]{"Acc：分析成功", "", "", "", ""};
        table.add(row);
        return table;
    }

    public static void main(String[] args) throws IOException {
        LR1 lr1 = new LR1();
        lr1.printMessage();
    }

}
