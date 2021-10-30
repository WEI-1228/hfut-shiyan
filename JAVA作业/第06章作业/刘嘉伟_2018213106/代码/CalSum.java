
public class CalSum {
    public static void main(String[] args) throws InterruptedException {
        Long su = 0L;
        Long wanquan = 0L;
        SuShu s1 = new SuShu();
        Thread t1 = new Thread(s1);
        t1.start();
        WanQuanShu w1 = new WanQuanShu();
        Thread t2 = new Thread(w1);
        t2.start();
        while(true){
            if(!t1.isAlive()&&!t2.isAlive())
                break;
        }
        su = s1.getSu();
        wanquan = w1.getWan();
        System.out.println("十万以内素数之和与完全数之和的乘积为："+su * wanquan);
    }
}
