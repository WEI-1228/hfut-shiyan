
public class SuShu implements Runnable {
    Long su;
    Long getSu(){
        return su;
    }
    private boolean issu(long su){
        boolean ok = true;
        for(long i = 2;i <= Math.sqrt(su);i++){
            if(su % i == 0)
                ok = false;
        }
        return ok;
    }
    public void run() {
        long temp = 0;
        for(int i = 2;i < 100000;i++){
            if(issu(i))
                temp += i;
        }
        su = temp;
        System.out.println("十万以内素数之和为："+su);
    }
}
