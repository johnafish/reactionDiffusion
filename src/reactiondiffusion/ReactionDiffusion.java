package reactiondiffusion;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;

/**
 * @author John Fish <john@johnafish.ca>
 */
public class ReactionDiffusion extends JFrame {
    int size = 200;
    public static int windowSize = 400;
    double[][] a = new double[size][size];
    double[][] b = new double[size][size];
    double dA = 1.0;
    double dB = 0.5;
    double f = 0.0545;
    double k = 0.062;
    double delT = 1.0;
    
    public void populateInitialArray(){
        int lowerSeed = size/2-10;
        int upperSeed = size/2+10;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                
                if (i>lowerSeed && j>lowerSeed && i<upperSeed && j<upperSeed){
                    a[i][j] = 1;
                    b[i][j] = 1;
                } else {
                    a[i][j] = 1;
                    b[i][j] = 0;
                }
            }
        }
    }
    
    public double applyLaplacian(int x, int y, int aOrB){ //0==a, 1==b
        //double[][] laplacian = {{0, 0, 0}, {0,1,0}, {0, 0, 0}};
        double[][] laplacian = {{0.05, 0.2, 0.05}, {0.2,-1,0.2}, {0.05, 0.2, 0.05}};
        double sum = 0;
        if(aOrB==0){
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int xPos = x+(i-1);
                    int yPos = y+(j-1);
                    if (0<=xPos && xPos<size && 0<=yPos && yPos<size){
                        sum += laplacian[i][j]*a[xPos][yPos];
                    }
                }
            }
        }else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int xPos = x+(i-1);
                    int yPos = y+(j-1);
                    if (0<=xPos && xPos<size && 0<=yPos && yPos<size){
                        sum += laplacian[i][j]*b[xPos][yPos];
                    }
                }
            }
        }
        return sum;
    }
    
    public void nextGeneration(){
        double[][] aN = new double[size][size];
        double[][] bN = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double aP = a[i][j];
                double bP = b[i][j];
                double reaction = aP*bP*bP;
                aN[i][j] = aP+delT*(dA*applyLaplacian(i,j,0)-reaction+f*(1-aP));
                bN[i][j] = bP+delT*(dB*applyLaplacian(i,j,1)+reaction-bP*(k+f));
            }
        }
        a = aN;
        b = bN;
    }
    @Override
    public void paint(Graphics g){
        int cellSize = windowSize/size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //bwValue is the colour value of our pixel, pure a is white pure b is black
                int bwValue = (int) Math.round(127 + 127*a[i][j] - 127*b[i][j]);
                Color bw = new Color(bwValue,bwValue,bwValue);
                g.setColor(bw);
                g.fillRect(i*cellSize, j*cellSize, cellSize, cellSize);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
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
