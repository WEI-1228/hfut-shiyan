package JAVA_HOMEWORK.Chapter_05.GuiTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GetStudent extends JFrame implements ActionListener {
    JTextField name;
    JTextField num;
    JTextField score;
    JButton save;
    JButton cal;
    File student;
    File grade;
    BufferedWriter stu;
    BufferedWriter res;
    BufferedReader readStu;
    public GetStudent() throws IOException {
        student = new File("L:\\first\\src\\JAVA_HOMEWORK\\Chapter_05\\GuiTable\\student.txt");
        grade = new File("L:\\first\\src\\JAVA_HOMEWORK\\Chapter_05\\GuiTable\\grade.txt");
        if(!student.exists()){
            student.createNewFile();
            stu = new BufferedWriter(new FileWriter(student));
            stu.write("学号\t\t姓名\t\t分数");
            stu.newLine();
            stu.flush();
            stu.close();
        }
        init();
        setLayout(new FlowLayout());
        setBounds(300,300,400,170);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        setTitle("学生信息存储管理");
        name = new JTextField(30);
        num = new JTextField(30);
        score = new JTextField(30);
        add(new JLabel("姓名"));
        add(name);
        add(new JLabel("学号"));
        add(num);
        add(new JLabel("分数"));
        add(score);
        save = new JButton("保存");
        cal = new JButton("整理");
        add(save);
        add(cal);
        save.addActionListener(this);
        cal.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == save){
            try {
                stu = new BufferedWriter(new FileWriter(student,true));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            String na;
            String sc;
            String nu;
            na = name.getText();
            sc = score.getText();
            nu = num.getText();
            try {
                stu.append((CharSequence)(nu+"\t\t"+na+"\t\t"+sc));
                stu.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                stu.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }finally {
                try {
                    stu.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if(e.getSource() == cal){
            StudentList list = new StudentList();
            try {
                readStu = new BufferedReader(new FileReader(student));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            try {
                res = new BufferedWriter(new FileWriter(grade));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            String t = null;
            try {
                readStu.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                while ((t = readStu.readLine()) != null){
                    String[]tmp = t.split("\t\t");
                    list.addStudent(tmp[1],tmp[0],Integer.parseInt(tmp[2]));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            list.sortStudent();
            Student perMax =list.getMax();
            Student perMin = list.getMin();
            Student per = null;
            int len = list.getNumer();
            double ave = list.getAve();
            for(int i = 0;i < len;i++){
                per = list.getStudent(i);
                try {
                    res.write(per.toString());
                    res.newLine();
                    res.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                res.newLine();
                res.newLine();
                res.write("平均分: \t"+ave);
                res.newLine();
                res.flush();
                res.write("最高分: \t"+perMax.toString());
                res.newLine();res.flush();
                res.write("最低分: \t"+perMin.toString());
                res.flush();
                res.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
