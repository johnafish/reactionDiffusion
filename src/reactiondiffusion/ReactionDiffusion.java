package reactiondiffusion;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;

/**
 * @author John Fish <john@johnafish.ca>
 */
public class ReactionDiffusion extends JFrame {
    int size = 100;
    public static int windowSize = 500;
    double[][] a = new double[size][size];
    double[][] b = new double[size][size];
    
    public void populateInitialArray(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = ThreadLocalRandom.current().nextDouble();
            }
        }
    }
    public void nextGeneration(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = ThreadLocalRandom.current().nextDouble();
            }
        }
    }
    @Override
    public void paint(Graphics g){
        int cellSize = windowSize/size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //bwValue is the colour value of our pixel, pure a is white pure b is black
                int bwValue = (int) Math.round(128+a[i][j]*127-b[i][j]*127);
                Color bw = new Color(bwValue,bwValue,bwValue);
                g.setColor(bw);
                g.fillRect(i*cellSize, j*cellSize, cellSize, cellSize);
            }
        }
    }
    public static void main(String[] args) {
        ReactionDiffusion r = new ReactionDiffusion();
        r.setSize(windowSize, windowSize);
        r.setDefaultCloseOperation( EXIT_ON_CLOSE );
        r.populateInitialArray();
        r.setVisible(true);  //Calls paint
        while(true){
            r.nextGeneration();
            r.repaint();
        }
    }

}
