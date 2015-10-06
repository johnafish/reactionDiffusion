package reactiondiffusion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;

/**
 * @author John Fish <john@johnafish.ca>
 */
public class ReactionDiffusion extends JFrame implements MouseListener, MouseMotionListener {
    public static int width = 400;
    public static int height = 400;
    double[][] a = new double[width][height];
    double[][] b = new double[width][height];
    double dA = 1.0;
    double dB = 0.5;
    //Cool (f,k): (.018, .051), 
    double f = .018; 
    double k = .051;
    double delT = 1.0;
    int timeCount = 0;
    int frameSkip = 1;
    
    public ReactionDiffusion(){
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void populateInitialArray(){
        int radius = 12;
        int midX = width/2;
        int midY = height/2;
        boolean circle = false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int delX = i-midX;
                int delY = j-midY;
                if(circle){
                    double distance = Math.sqrt(delX*delX+delY*delY);
                    if (distance<radius){
                        a[i][j] = 0;
                        b[i][j] = 1;
                    } else {
                        a[i][j] = 1;
                        b[i][j] = 0;
                    }
                } else {
                    if (delX>-radius && delX<radius && delY>-radius &&delY<radius){
                        a[i][j] = 1;
                        b[i][j] = 1;
                    }else {
                        a[i][j] = 1;
                        b[i][j] = 0;
                    }
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
                    if (0<=xPos && xPos<width && 0<=yPos && yPos<height){
                        sum += laplacian[i][j]*a[xPos][yPos];
                    }
                }
            }
        }else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int xPos = x+(i-1);
                    int yPos = y+(j-1);
                    if (0<=xPos && xPos<width && 0<=yPos && yPos<height){
                        sum += laplacian[i][j]*b[xPos][yPos];
                    }
                }
            }
        }
        return sum;
    }
    
    public void nextGeneration(){
        double[][] aN = new double[width][height];
        double[][] bN = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
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
    public void mousePressed( MouseEvent e ) {}
    public void mouseClicked( MouseEvent e ) {}
    public void mouseMoved( MouseEvent e ) { }
    public void mouseEntered( MouseEvent e) {}
    public void mouseExited( MouseEvent e) {}
    public void mouseReleased( MouseEvent e) {}
    public void mouseDragged( MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        a[mouseX][mouseY] = .5;
        b[mouseX][mouseY] = .5;
    }
    @Override
    public void paint(Graphics g){
        timeCount++;
        if (timeCount%frameSkip==0){
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    //bwValue is the colour value of our pixel, pure a is white pure b is black
                    int bwValue = (int) Math.round(128+127*a[i][j] - 127*b[i][j]);
                    Color bw = new Color((int)Math.abs(Math.round(55*a[i][j])),(int)Math.abs(Math.round(120*b[i][j])),0);
                    img.setRGB(i, j, bw.getRGB());
                }
            }

            g.drawImage(img, 0, 0, rootPane);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ReactionDiffusion r = new ReactionDiffusion();
        r.setSize(width, height);
        r.setDefaultCloseOperation( EXIT_ON_CLOSE );
        r.populateInitialArray();
//        r.setUndecorated(true);
        r.setVisible(true);  //Calls paint
        while(true){
            r.nextGeneration();
            r.repaint(); 
        }
        
    }

}
