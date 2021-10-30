import java.math.BigInteger;   //引入大数字的包
public class getSum {
    public static void main(String[] args) {  //main函数主体
		
        BigInteger sum = new BigInteger("0"); //新建一个大数字类对象，初值为0
        for(int i=1;i<=100;i++)     //计算前一百个数字的阶层和
        {
            sum = sum.add(reb(i));  //将每个数字i阶层和相加
        }
        System.out.println(sum);    //输出结果
		
    }


    public static BigInteger reb(int x)   //计算数字x的阶层的函数
    {
    BigInteger t = new BigInteger("1");
    for(int i=1;i<=x;i++)     //将1-x所有数字相乘
    {
        t = t.multiply(new BigInteger(String.valueOf(i)));
    }
    return t;       //返回一个大数字类的对象1
    }
}
