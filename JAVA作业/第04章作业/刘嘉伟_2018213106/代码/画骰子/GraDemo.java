import javax.swing.*;
import java.awt.*;

public class GraDemo extends JFrame {
    MyPanel panel;
    public GraDemo(){
        init();
        setBounds(300,300,350,350);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        setTitle("画骰子");
        panel = new MyPanel();
        panel.setBackground(Color.yellow);
        add(panel, BorderLayout.CENTER);
    }
}
