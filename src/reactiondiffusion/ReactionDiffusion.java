package reactiondiffusion;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;

/**
 * @author John Fish <john@johnafish.ca>
 */
public class ReactionDiffusion extends JFrame {
    int size = 30;
    double[][] a = new double[size][size];
    double[][] b = new double[size][size];
    
    public void populateInitialArray(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = ThreadLocalRandom.current().nextDouble();
            }
        }
    }
    
    @Override
    public void paint(Graphics g){
        //Drawing code goes here
    }
    public static void main(String[] args) {
        ReactionDiffusion r = new ReactionDiffusion();
        r.setSize(500, 500);
        r.setDefaultCloseOperation( EXIT_ON_CLOSE );
        r.populateInitialArray();
        r.setVisible(true);  //Calls paint
    }

}
