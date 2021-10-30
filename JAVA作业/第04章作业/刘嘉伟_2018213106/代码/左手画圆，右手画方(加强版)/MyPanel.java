import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private boolean check;
    public MyPanel(int x1,int y1,int x2,int y2,boolean check){
        this.x1 = x1;
        this.x2 = x2;
        this.y2 = y2;
        this.y1 = y1;
        this.check = check;
    }
    public void paint(Graphics g){
        if(check == true){
            g.setColor(Color.blue);
            int r = getLen(x1,x2,y1,y2);
            g.fillOval(x1-r,y1-r,2*r,2*r);
        }else {
            g.setColor(Color.red);
            g.fillRect(x1,y1,Math.abs(x2-x1),Math.abs(y2-y1));
        }
    }
    public int getLen(double a,double b,double c,double d){
        return (int)(Math.pow((Math.pow((a-b),2)+Math.pow((c-d),2)),0.5));
    }
}
