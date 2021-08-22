package Main;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class A_mess extends JFrame  {

    public A_mess() {

        initUI();
    }

    private void initUI() {

        add(new Screen());

        setTitle("WIP");
        setSize(Variables.Screen_Width, Variables.Screen_Height);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new A_mess();
            ex.setVisible(true);
        });
    }
}
