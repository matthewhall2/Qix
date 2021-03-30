import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.geom.*; // For Ellipse2D, etc.
import java.util.*;
public class Display  extends JFrame implements MouseListener, ActionListener, KeyListener {

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

    int trueX = 280;
    int trueY = 560;

    int drawSpeed = 0;

    int fastSpeed = 4;
    int slowSpeed = 2;

    int[][] Board;


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
        Board = new int[560][710];
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

//        while(true){
//            playerX += playerXSpeed;
//            playerY += playerYSpeed;
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            repaint();
//        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        playerX += playerXSpeed;
        playerY += playerYSpeed;
        trueX += playerXSpeed;
        trueY += playerYSpeed;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

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
                break;
            case KeyEvent.VK_S:
                drawSpeed = slowSpeed;
        }



    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
            case KeyEvent.VK_B:
                playerXSpeed = 0;
                playerYSpeed = 0;
                drawSpeed = 0;
                break;
            default:
                playerXSpeed = 0;
                playerYSpeed = 0;
        }


    }



    public static void main(String[]args){
        //opens the window by creating a new Display class

        Display d = new Display();

    }
}