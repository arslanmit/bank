import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: syanlik
 * Date: 11.02.2014
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 */
public class AnaEkran  extends JWindow {
    private Dimension d=Toolkit.getDefaultToolkit().getScreenSize();

    public AnaEkran(){
        JLabel lbImage    = new JLabel (new ImageIcon ("Src/Images/Splash.jpg"));
        Color cl = new Color (0, 0, 0);
        lbImage.setBorder (new LineBorder (cl, 1));

        getContentPane().add (lbImage, BorderLayout.CENTER);
        pack();

        setSize (getSize().width, getSize().height);
        setLocation (d.width / 2 - getWidth() / 2, d.height / 2 - getHeight() / 2);

        //show();

        for (int i = 0; i <= 1000; i++) { }

        new BankaSistemi ();

        toFront();
        dispose ();
        setVisible (false);
    }

    public static void main(String args[]){
        new AnaEkran();
    }
}
