package cn.edu.hfut.LL1;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class p_table_win extends JFrame {
    JTable table;
    Map<Pair<Character, Character>, String> ptable;

    p_table_win(Map<Pair<Character, Character>, String> table) throws IOException {
        setTitle("预测表");
        ptable = table;
        setLayout(new BorderLayout());
        init();
        setBounds(400, 300, 500, 150);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void init() throws IOException {
        table_model model = new table_model().setVal(ptable);
        table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        add(pane);
    }

    public static void main(String[] args) throws IOException {
    }
}
