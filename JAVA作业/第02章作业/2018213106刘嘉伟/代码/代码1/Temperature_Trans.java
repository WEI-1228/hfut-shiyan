import java.util.Scanner;   //引入输入对象
public class Temperature_Trans {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入摄氏温度: ");   //提示信息
        double tem = sc.nextDouble();    //接受用户输入的摄氏度
        double hua = (9.0/5)*tem+32;    //温度转换
        System.out.println("华氏温度为: "+hua);   //输出结果
    }
}
