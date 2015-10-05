package reactiondiffusion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;

/**
 * @author John Fish <john@johnafish.ca>
 */
public class ReactionDiffusion extends JFrame {
    int size = 600;
    public static int windowSize = 600;
    double[][] a = new double[size][size];
    double[][] b = new double[size][size];
    double dA = 1.0;
    double dB = 0.5;
    //Cool (f,k): (.018, .051)
    double f = .018; 
    double k = .051;
    double delT = 1.0;
    
    public void populateInitialArray(){
        int radius = 4;
        int midX = size/2;
        int midY = size/2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int delX = i-midX;
                int delY = j-midY;
                double distance = Math.sqrt(delX*delX+delY*delY);
                if (distance<radius){
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
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB );

        int cellSize = windowSize/size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //bwValue is the colour value of our pixel, pure a is white pure b is black
                int bwValue = (int) Math.round(128+127*a[i][j] - 127*b[i][j]);
                if(0>bwValue || 255<bwValue){
                    System.out.println(a[i][j]+" "+b[i][j]+" "+bwValue);
                }
                Color bw = new Color(bwValue,bwValue,bwValue);
                img.setRGB(i, j, bw.getRGB());
            }
        }
        
        g.drawImage(img, 0, 0, rootPane);
    }
    public static void main(String[] args) throws InterruptedException {
        ReactionDiffusion r = new ReactionDiffusion();
        r.setSize(windowSize, windowSize);
        r.setDefaultCloseOperation( EXIT_ON_CLOSE );
        r.populateInitialArray();
        r.setUndecorated(true);
        r.setVisible(true);  //Calls paint
        while(true){
            r.nextGeneration();
            r.repaint(); 
        }
        
    }

}
