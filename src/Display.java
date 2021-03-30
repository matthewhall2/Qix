import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.geom.*; // For Ellipse2D, etc.
import java.util.*;
public class Display  extends JFrame implements ActionListener, KeyListener {

    private GameDrawCanvas canvas;
    private GameInfoDrawCanvas infoDrawCanvas;
    public final int frameWidth = 750;
    public final int frameHeight = 600;
    int length = 0;
    boolean backDraw = false;
    int playerRadius =5;
    int playerX = 300;
    int playerY = 580;

    int playerXSpeed = 0;
    int playerYSpeed = 0;

    int trueX = playerX - 20;
    int trueY = playerY - 20;

    int drawSpeed = 2;

    int fastSpeed = 4;
    int slowSpeed = 2;

    int[][] Board;
    boolean moveOff = false;


    class GameDrawCanvas extends JPanel {
        //painting method
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            drawBackGround(g2d);
            drawPlayer(g2d);
        }

        private void drawBackGround (Graphics2D g2d){
            g2d.setColor(Color.RED);
            this.setBackground(Color.BLACK);

            g2d.setColor(Color.WHITE);
            g2d.drawLine(20, 20, 730, 20);
            g2d.drawLine(20, 20, 20, 580);
            g2d.drawLine(20, 580, 730, 580);
            g2d.drawLine(730, 20, 730, 580);


        }

        private void drawPlayer(Graphics2D g2d){
            g2d.setColor(Color.RED);
            Ellipse2D player = new Ellipse2D.Double(playerX - playerRadius, playerY - playerRadius,
                    2 * playerRadius, 2*playerRadius);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(player);
            g2d.setStroke(new BasicStroke(1));


            Toolkit.getDefaultToolkit().sync();

        }
    }

    class GameInfoDrawCanvas extends JPanel{
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);

            this.setBackground(Color.BLACK);


            g2d.setColor(Color.RED);
            g2d.drawLine(0, 140, frameWidth, 140);
            g2d.setColor(Color.MAGENTA);
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));

            g2d.drawString("QIX", 50, 50);

        }
    }

    private int[][] initBoardArray(int[][] Board){
        for(int i = 0; i < Board.length; i++){
            for(int j = 0; j < Board[i].length; j++){
                if(j == 0 || j == Board[i].length - 1){
                    Board[i][j] = 1;
                }
                if(i == 0 || i == Board.length - 1){
                    Board[i][j] = 1;
                }
            }
        }
        return Board;
    }



    public Display()  {
        Board = new int[561][711];
        initBoardArray(Board);
        canvas = new GameDrawCanvas();
        infoDrawCanvas = new GameInfoDrawCanvas();
        canvas.setPreferredSize(new Dimension(frameWidth, frameHeight));
        infoDrawCanvas.setPreferredSize(new Dimension(frameWidth, 150));
        // Add both panels to this JFrame
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        addKeyListener(this);
        cp.add(canvas, BorderLayout.CENTER);
        cp.add(infoDrawCanvas, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Timer timer = new Timer(25, this);
        timer.start();
        setTitle("Qix");
        pack();           // pack all the components in the JFrame
        setVisible(true); // show it

    }



    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        playerX += playerXSpeed;
        playerY += playerYSpeed;
        int lastY = trueY;
        int lastX = trueX;
        trueX += playerXSpeed;
        trueY += playerYSpeed;

        if(trueX < 0) {
            trueX = 0;
            playerX = 20;
        }else if(trueX > 710) {
            trueX = 710;
            playerX = 730;
        }else if(trueY < 0) {
            trueY = 0;
            playerY = 20;
        }else if(trueY > 560) {
            trueY = 560;
            playerY = 580;
        }else if(moveOff){
            if(this.Board[trueY][trueX] != 1){
                while(this.Board[trueY][trueX] > 1) {
                    playerX -= playerXSpeed / 2;
                    playerY -= playerYSpeed / 2;
                    trueX -= playerXSpeed / 2;
                    trueY -= playerYSpeed / 2;
                }
            }

        }else if(!moveOff && this.Board[lastY][lastX] == 1){
            while(this.Board[trueY][trueX] != 1) {
                playerX -= playerXSpeed / 2;
                playerY -= playerYSpeed / 2;
                trueX -= playerXSpeed / 2;
                trueY -= playerYSpeed / 2;
            }
        }
        repaint();
    }



    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                playerYSpeed = -drawSpeed;
                playerXSpeed = 0;
                break;
            case KeyEvent.VK_DOWN:
                playerYSpeed = drawSpeed;
                playerXSpeed = 0;
                break;
            case KeyEvent.VK_LEFT:
                playerXSpeed = -drawSpeed;
                playerYSpeed = 0;
                break;
            case KeyEvent.VK_RIGHT:
                playerXSpeed = drawSpeed;
                playerYSpeed = 0;
                break;
            case KeyEvent.VK_F:
                drawSpeed = fastSpeed;
                moveOff = true;
                break;
            case KeyEvent.VK_S:
                moveOff = true;
                drawSpeed = slowSpeed;
        }



    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println(this.Board[trueY][trueX]);
        System.out.println(moveOff);
        if(this.Board[trueY][trueX] == 1) {
            moveOff = false;
            System.out.println(moveOff);
        }
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_S:
            case KeyEvent.VK_F:


                drawSpeed = 2;
                break;
            default:
                playerXSpeed = 0;
                playerYSpeed = 0;
        }


    }



    public static void main(String[]args){
        //opens the window by creating a new Display class
        Display d = new Display();
        System.out.println(4/2);
    }
}