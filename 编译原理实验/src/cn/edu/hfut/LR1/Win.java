package cn.edu.hfut.LR1;

import cn.edu.hfut.LL1.LL1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Win extends JFrame implements ActionListener {
    JTextField field;
    JTable table;
    JButton button;

    Win() {
        setLayout(null);
        init();
        setTitle("LR(1)语法分析器");
        setBounds(600, 300, 700, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        button = new JButton("开始");
        field = new JTextField(20);
        table = new JTable(new Model());
        JScrollPane scrollPane = new JScrollPane(table);
        button.setBounds(370, 10, 100, 40);
        field.setBounds(10, 10, 300, 40);
        scrollPane.setBounds(0, 100, 700, 400);
        add(button);
        add(field);
        add(scrollPane);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = field.getText();
        try {
            table.setModel(new Model().setVal(text));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Win win = new Win();
    }
}
