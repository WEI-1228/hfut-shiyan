import java.util.Scanner;   //引入输入函数对象
public class Volumn {
    public static void main(String[] args) {
        System.out.println("请输入球的半径: ");
        Scanner sc = new Scanner(System.in);   //接收用户输入
        double r = sc.nextDouble();
        final double PI = 3.14;     //定义Π的值，不用改变所以final修饰
        double v = (4.0/3)*pi*r*r;   //计算球的体积
        System.out.println("该球的体积为: "+v);   //输出结果
    }
}
