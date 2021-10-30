import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    public void paint(Graphics g){
        g.setColor(Color.white);
        g.fillRoundRect(15,0,300,300,20,20);
        g.setColor(Color.black);
        g.fillOval(55,30,50,50);
        g.fillOval(140,130,50,50);
        g.fillOval(230,220,50,50);


    }
}
