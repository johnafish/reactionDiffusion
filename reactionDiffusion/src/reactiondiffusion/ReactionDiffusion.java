package reactiondiffusion;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 * @author John Fish <john@johnafish.ca>
 */
public class ReactionDiffusion extends JFrame {

    @Override
    public void paint(Graphics g){
        //Drawing code goes here
    }
    public static void main(String[] args) {
        ReactionDiffusion r = new ReactionDiffusion();
        r.setSize(500, 500);
        r.setDefaultCloseOperation( EXIT_ON_CLOSE );
        r.setVisible(true); 
    }

}
