
public class WanQuanShu implements Runnable{
    Long wan;
    Long getWan(){
        return wan;
    }
    private boolean iswan(long n){
        boolean isperfectnumner=false;
        int sum=0;
        for(int i=1;i<n;i++) {
            if(n%i==0) {
                sum+=i;
            }

        }
        if(sum==n){
            isperfectnumner=true;//是完数
        }
        return isperfectnumner;
    }
    public void run() {
        long temp = 0;
        for(int i = 0;i<10000;i++){
            if(iswan(i)) {
                temp += i;
            }
        }
        wan = temp;
        System.out.println("十万以内完全数之和为："+wan);
    }
}
