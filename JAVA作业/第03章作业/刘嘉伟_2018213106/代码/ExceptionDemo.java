import java.util.Scanner;

public class ExceptionDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n;
        String s = sc.nextLine();
        try{
            n = Integer.parseInt(s);
            System.out.println(n);
        }catch (Exception ex){
            System.out.println("输入的数字格式有问题请检查");
        }
    }
}
