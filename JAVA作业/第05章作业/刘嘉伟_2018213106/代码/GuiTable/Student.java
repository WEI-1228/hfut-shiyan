package JAVA_HOMEWORK.Chapter_05.GuiTable;

public class Student implements Comparable<Student> {
    private String name;
    private String num;
    private int score;
    public String toString(){
        return num+"\t\t"+name+"\t\t"+score;
    }
    public Student(String name,String num,int score){
        this.name = name;
        this.score = score;
        this.num = num;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int compareTo(Student o) {
        return this.score-o.score;
    }
}
