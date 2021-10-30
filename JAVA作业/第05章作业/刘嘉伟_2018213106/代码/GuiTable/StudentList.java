package JAVA_HOMEWORK.Chapter_05.GuiTable;

import java.util.ArrayList;
import java.util.Collections;

public class StudentList {
    ArrayList<Student>stuList;
    public StudentList(){
        stuList = new ArrayList<Student>();
    }
    public void addStudent(String name,String num,int score){
        Student tmp = new Student(name,num,score);
        stuList.add(tmp);
    }
    public double getAve(){
        double sum = 0;
        for(int i = 0;i < stuList.size();i++){
            sum += stuList.get(i).getScore();
        }
        return sum/stuList.size();
    }
    public void sortStudent(){
        Collections.sort(stuList);
    }
    public Student getMin(){
        return stuList.get(0);
    }
    public Student getMax(){
        return stuList.get(stuList.size()-1);
    }
    public Student getStudent(int i){
        Student t = stuList.get(i);
        return t;
    }
    public int getNumer(){
        return stuList.size();
    }
}
