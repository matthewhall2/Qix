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

    int startPathX = playerX;
    int startPathY = playerY;

    int startX = 20;
    int startY = 20;

    int playerXSpeed = 0;
    int playerYSpeed = 0;

    int trueX = playerX - startX;
    int trueY = playerY - startY;

    int drawSpeed = 2;

    int fastSpeed = 4;
    int slowSpeed = 2;

    int[][] Board;
    boolean moveOff = false;
    boolean lastMoveOff = false;

    boolean isUpPressed = false;
    boolean isDownPressed = false;
    boolean isLeftPressed = false;
    boolean isRightPressed = false;

    ArrayList<Integer> direction = new ArrayList<>(0);
    ArrayList<Integer> slowOrFast = new ArrayList<Integer>(0);

    boolean isFastPressed = false;
    boolean isSlowPressed = false;



    class GameDrawCanvas extends JPanel {
        //painting method
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            drawBackGround(g2d);
            drawPath(g2d);
            drawPlayer(g2d);
            Toolkit.getDefaultToolkit().sync();
//            int n = 12;
//            int[] xpoints = new int[]{50, 70, 70, 170,  170, 190,  190,  170, 170,  70, 70, 50};
//
//            int[] ypoints = new int[]{50, 50, 30, 30, 50, 50, 150, 150, 170, 170, 150, 150};
//            Polygon p = new Polygon(xpoints, ypoints, n);
//            g2d.fill(p);
        }


        private void drawBackGround (Graphics2D g2d){
            g2d.setColor(Color.RED);
            this.setBackground(Color.BLACK);

            g2d.setColor(Color.WHITE);
            g2d.drawLine(startX, startY, frameWidth - startX, startX);
            g2d.drawLine(startX, startY, startX, frameHeight - startY);
            g2d.drawLine(startX, frameHeight - startY, frameWidth - startX, frameHeight - startY);
            g2d.drawLine(frameWidth - startX, startY, frameWidth - startX, frameHeight - startY);


        }

        private void drawPlayer(Graphics2D g2d){
            g2d.setColor(Color.RED);
            Ellipse2D player = new Ellipse2D.Double(playerX - playerRadius, playerY - playerRadius,
                    2 * playerRadius, 2*playerRadius);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(player);
            g2d.setStroke(new BasicStroke(1));
        }

        private void drawPath(Graphics2D g2d){
            g2d.setColor(Color.WHITE);
            g2d.drawLine(startPathX, startPathY, playerX, playerY);
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

    private int[][] copyBoard(){
        int[][] temp = new int[this.Board.length][this.Board[0].length];
        for(int i = 0; i < Board.length; i++){
            for(int j = 0; j < Board[0].length; j++){
                temp[i][j] = this.Board[i][j];
            }
        }
        return temp;
    }



    public Display()  {
        Board = new int[frameHeight - 2*startY + 1][frameWidth - 2*startX + 1];
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

    public void putNewEdgeInBoard(int xSpeed, int ySpeed, int X, int Y){
        if(xSpeed > 0) {
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                X -= 1;
            }
        } else if(xSpeed < 0){
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                X += 1;
            }
        }else if(ySpeed > 0){
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                Y -= 1;
            }
        }else if(ySpeed < 0){
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                Y += 1;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if(!direction.isEmpty()) {
            int lastDirectionKey = direction.size() - 1;

            int lastSpeedKey = slowOrFast.size() - 1;
            int speed = 2;
            if(lastSpeedKey != -1){
                speed = slowOrFast.get(lastSpeedKey);
            }

            switch(direction.get(lastDirectionKey)){
                case 1:
                    playerXSpeed = -speed;
                    playerYSpeed = 0;
                    break;
                case 2:
                    playerYSpeed = -speed;
                    playerXSpeed = 0;
                    break;
                case 3:
                    playerXSpeed = speed;
                    playerYSpeed = 0;
                    break;
                case 4:
                    playerYSpeed = speed;
                    playerXSpeed = 0;

            }

            playerX += playerXSpeed;
            playerY += playerYSpeed;

            int lastY = trueY;
            int lastX = trueX;
            trueX += playerXSpeed;
            trueY += playerYSpeed;

            // the following are corrections for going out of bound, or  from moving
            // into the playfield without a button pressed
            if (trueX < 0) {
                trueX = 0;
                playerX = 20;

            } else if (trueX > 710) {
                trueX = 710;
                playerX = 730;

            } else if (trueY < 0) {
                trueY = 0;
                playerY = 20;

            } else if (trueY > 560) {
                trueY = 560;
                playerY = 580;

            } else if (moveOff) {
                if (this.Board[trueY][trueX] > 1) {
                    while (this.Board[trueY][trueX] > 1) {
                        playerX -= playerXSpeed / 2;
                        playerY -= playerYSpeed / 2;
                        trueX -= playerXSpeed / 2;
                        trueY -= playerYSpeed / 2;
                    }
                }

                if(this.Board[lastY][lastX] == 1){
                    startPathX = playerX;
                    startPathY = playerY;
                }


            } else if (!moveOff && this.Board[lastY][lastX] == 1) {
                while (this.Board[trueY][trueX] != 1) {
                    playerX -= playerXSpeed / 2;
                    playerY -= playerYSpeed / 2;

                    trueX -= playerXSpeed / 2;
                    trueY -= playerYSpeed / 2;
                }
            }

            if(this.Board[trueY][trueX] == 1 && slowOrFast.size() == 0){
                moveOff = false;
                lastMoveOff = false;
            }
            //putNewEdgeInBoard(playerXSpeed, playerYSpeed, trueX, trueY);
            repaint();
        }
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
                if(!isUpPressed){
                    direction.add(2);
                }
                isUpPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                playerYSpeed = drawSpeed;
                playerXSpeed = 0;
                if(!isDownPressed){
                    direction.add(4);
                }
                isDownPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                playerXSpeed = -drawSpeed;
                playerYSpeed = 0;
                if(!isLeftPressed){
                    direction.add(1);
                }
                isLeftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                playerXSpeed = drawSpeed;
                playerYSpeed = 0;
                if(!isRightPressed){
                    direction.add(3);
                }
                isRightPressed = true;
                break;
            case KeyEvent.VK_F:
                drawSpeed = fastSpeed;
                if(moveOff){
                    lastMoveOff = true;
                }
                moveOff = true;
                if(!isFastPressed){
                    slowOrFast.add(fastSpeed);
                }
                isFastPressed = true;
                break;
            case KeyEvent.VK_S:
                drawSpeed = slowSpeed;
                if(moveOff){
                    lastMoveOff = true;
                }
                moveOff = true;
                startPathY = playerY;
                startPathX = playerX;
                if(!isSlowPressed){
                    slowOrFast.add(slowSpeed);
                }
                isSlowPressed = true;
        }



    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println(this.Board[trueY][trueX]);
        System.out.println(moveOff);
//        if(this.Board[trueY][trueX] == 1 && slowOrFast.size() == 1) {
//            moveOff = false;
//            System.out.println(moveOff);
//        }
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_S:
                isSlowPressed = false;
                slowOrFast.remove((Integer)slowSpeed);
//                if(slowOrFast.size() == 0){
//                    moveOff = false;
//                }

                break;
            case KeyEvent.VK_F:
                drawSpeed = 2;
                isFastPressed = false;
                moveOff = false;
                slowOrFast.remove((Integer)fastSpeed);
//                if(slowOrFast.size() == 0){
//                    moveOff = false;
//                }
                break;
            case KeyEvent.VK_DOWN:
                isDownPressed = false;
                direction.remove((Integer)4);
                break;
            case KeyEvent.VK_UP:
                isUpPressed = false;
                direction.remove((Integer)2);
                break;
            case KeyEvent.VK_LEFT:
                isLeftPressed = false;
                direction.remove((Integer)1);
                break;
            case KeyEvent.VK_RIGHT:
                isRightPressed = false;
                direction.remove((Integer)3);
                break;

            }
        }

    public static void main(String[]args){
        //opens the window by creating a new Display class
        Display d = new Display();




    }
}