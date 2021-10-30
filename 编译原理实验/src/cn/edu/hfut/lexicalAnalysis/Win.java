package cn.edu.hfut.lexicalAnalysis;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Win extends JFrame implements ActionListener {
    JTextArea textArea1;
    JTable table;
    JButton startButton;
    JButton clearButton;

    public Win(){
        setLayout(null);
        init();
        setTitle("词法分析器");
        setBounds(400,300,1100,700);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void init() {
        textArea1 = new JTextArea();
        table = new JTable(new Model());
        JScrollPane pane1 = new JScrollPane(textArea1);
        JScrollPane pane2 = new JScrollPane(table);
        startButton = new JButton("开始");
        clearButton = new JButton("清除");
        startButton.setBounds(400,20, 100,40);
        clearButton.setBounds(550,20, 100,40);
        JLabel label1 = new JLabel("源程序");
        JLabel label2 = new JLabel("输出结果");
        label1.setBounds(20,50,100,50);
        label2.setBounds(1000,50,200,50);
        pane1.setBounds(10,100, 500,500);
        pane2.setBounds(550,100,500,500);
        this.add(label1);
        this.add(label2);
        this.add(startButton);
        this.add(clearButton);
        this.add(pane1);
        this.add(pane2);
        startButton.addActionListener(this);
        clearButton.addActionListener(this);
    }



    public static void main(String[] args) {
        Win windows = new Win();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startButton){
            String text = textArea1.getText();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("L:\\JAVA\\编译原理实验\\src\\cn\\edu\\hfut\\lexicalAnalysis\\src.txt"));
                writer.write(text);
                writer.flush();
                writer.close();
                table.setModel(new Model().setVal());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }else if(e.getSource()==clearButton){
            textArea1.setText("");
            table.setModel(new Model());
        }
    }
}
