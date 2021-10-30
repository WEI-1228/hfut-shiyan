import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JFramDemo extends JFrame implements MouseListener {
    MyPanel panel;
    int checkLeft;
    int checkRight;
    int x1,x2,y1,y2;
    public JFramDemo(){
        init();
        setBounds(300,300,700,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void init() {
        checkLeft = 0;
        checkRight = 0;
        setTitle("左手画圆，右手画方");
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getModifiers() == InputEvent.BUTTON1_MASK){
            if(checkLeft == 0){
                x1 = e.getX()-8;
                y1 = e.getY()-8-30;
                checkLeft = 1;
                return;
            }
            if(checkLeft == 1){
                x2 = e.getX()-8;
                y2 = e.getY()-8-30;
                panel = new MyPanel(x1,y1,x2,y2,true);
                add(panel,BorderLayout.CENTER);
                setVisible(true);
                checkLeft = 0;
                return;
            }
        }
        if(e.getModifiers() == InputEvent.BUTTON3_MASK){
            if(checkRight == 0){
                x1 = e.getX()-8;
                y1 = e.getY()-8-30;
                checkRight = 1;
                return;
            }
            if(checkRight == 1){
                x2 = e.getX()-8;
                y2 = e.getY()-8-30;
                panel = new MyPanel(x1,y1,x2,y2,false);
                add(panel,BorderLayout.CENTER);
                setVisible(true);
                checkRight = 0;
                return;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getModifiers() == InputEvent.BUTTON1_MASK){
            if(checkLeft == 0){
                x1 = e.getX()-8;
                y1 = e.getY()-8-30;
                checkLeft = 1;
                return;
            }
            if(checkLeft == 1){
                x2 = e.getX()-8;
                y2 = e.getY()-8-30;
                panel = new MyPanel(x1,y1,x2,y2,true);
                add(panel,BorderLayout.CENTER);
                setVisible(true);
                checkLeft = 0;
                return;
            }
        }
        if(e.getModifiers() == InputEvent.BUTTON3_MASK){
            if(checkRight == 0){
                x1 = e.getX()-8;
                y1 = e.getY()-8-30;
                checkRight = 1;
                return;
            }
            if(checkRight == 1){
                x2 = e.getX()-8;
                y2 = e.getY()-8-30;
                panel = new MyPanel(x1,y1,x2,y2,false);
                add(panel,BorderLayout.CENTER);
                setVisible(true);
                checkRight = 0;
                return;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
