import java.math.BigInteger;   //��������ֵİ�
public class getSum {
    public static void main(String[] args) {  //main��������
		
        BigInteger sum = new BigInteger("0"); //�½�һ������������󣬳�ֵΪ0
        for(int i=1;i<=100;i++)     //����ǰһ�ٸ����ֵĽײ��
        {
            sum = sum.add(reb(i));  //��ÿ������i�ײ�����
        }
        System.out.println(sum);    //������
		
    }


    public static BigInteger reb(int x)   //��������x�Ľײ�ĺ���
    {
    BigInteger t = new BigInteger("1");
    for(int i=1;i<=x;i++)     //��1-x�����������
    {
        t = t.multiply(new BigInteger(String.valueOf(i)));
    }
    return t;       //����һ����������Ķ���1
    }
}
