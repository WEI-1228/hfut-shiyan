package cn.edu.hfut.LL1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

public class Win extends JFrame implements ActionListener {
    JTextField field;
    JTable table;
    JButton button;
    JButton button2;
    Model model;
    Win(){
        setLayout(null);
        init();
        setTitle("LL(1)语法分析器");
        setBounds(600,300,500,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        model = new Model();
        button = new JButton("开始");
        button2 = new JButton("查看分析表");
        field = new JTextField(20);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        button.setBounds(280, 10, 60, 40);
        button2.setBounds(350, 10, 100, 40);
        field.setBounds(10,10,250,40);
        scrollPane.setBounds(10,100, 450,400);
        add(button);
        add(button2);
        add(field);
        add(scrollPane);
        button.addActionListener(this);
        button2.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button){
            String text = field.getText();
            try {
                model = new Model().setVal(text);
                table.setModel(model);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            try {
                new p_table_win(model.getTable());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }


    }
}
