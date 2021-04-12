import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.geom.*; // For Ellipse2D, etc.
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.*;

import com.sun.jdi.VMCannotBeModifiedException;



public class Display extends JFrame implements ActionListener, KeyListener {

    private GameDrawCanvas canvas;
    private GameInfoDrawCanvas infoDrawCanvas;
    public final int frameWidth = 750;
    public final int frameHeight = 600;
    Robot myRobot;

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

    int qixX= 355;
    int qixY= 250;

    int pushX=0;
    int pushY=0;



    ArrayList<Integer> direction = new ArrayList<>(0);
    ArrayList<Integer> slowOrFast = new ArrayList<Integer>(0);

    boolean isFastPressed = false;
    boolean isSlowPressed = false;

    int lastDirection = 0;

    ArrayList<Integer> currentPathLineListX = new ArrayList<>();
    ArrayList<Integer> currentPathLineListY = new ArrayList<>();
    boolean clearPath = false;

    ArrayList<ArrayList<Integer>> permanentPathLineListX = new ArrayList<>();
    ArrayList<ArrayList<Integer>> permanentPathLineListY = new ArrayList<>();

    ArrayList<Shape> polygonList = new ArrayList<>();
    int startPathDirection = 0;
    int endPathDirection = 0;

    ArrayList<Line2D> currentPathLines = new ArrayList<>();
    ArrayList<Line2D> setPathLines = new ArrayList<>();
    Line2D currentLine = new Line2D.Double(0, 0,0, 0);

    class GameDrawCanvas extends JPanel {
        //painting method
        BufferedImage bufferImage;
        BufferStrategy bs;
        Robot myRobot;
        public GameDrawCanvas() throws AWTException {
            Display.this.createBufferStrategy(1);
            // myRobot = new Robot();

            // this.bufferImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        }
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            // System.out.println(canvas.getLocationOnScreen());
            //Point p = canvas.getLocationOnScreen();
            //int x = p.x;
            //int y = p.y;

            Graphics2D g2d = (Graphics2D)g;
            //System.out.println(myRobot.getPixelColor(x + playerX, y + playerY));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            drawPolygons(g2d, Color.BLUE);
            g2d.setColor(Color.WHITE);
            drawBackGround(g2d);
            g2d.setColor(Color.WHITE);
            currentLine = new Line2D.Double(startPathX, startPathY, playerX, playerY);

            //g2d.drawLine(startPathX, startPathY, playerX, playerY);
            g2d.draw(currentLine);
            upDateCurrentPath(currentPathLineListX, currentPathLineListY);
            drawPath(g2d, currentPathLineListX, currentPathLineListY);
            drawAllPaths(g2d);

            if(clearPath){
                // System.out.println("testing");
                addToPolyArr();
                //System.out.println(polygonList.size());
                updateBoard();

                permanentPathLineListX.add((ArrayList<Integer>) currentPathLineListX.clone());
                permanentPathLineListY.add((ArrayList<Integer>) currentPathLineListY.clone());
                setPathLines.addAll((ArrayList<Line2D>)currentPathLines.clone());
                currentPathLines.clear();
                currentPathLineListX.clear();
                //System.out.println(currentPathLineListX.size());
                currentPathLineListY.clear();
                startPathDirection = 0;
                endPathDirection = 0;
                if(slowOrFast.size() == 0) {
                    moveOff = false;
                }
            }
            drawPlayer(g2d);
            drawQix(g2d,qixX,qixY);
            Toolkit.getDefaultToolkit().sync();
//            int n = 12;
//            int[] xpoints = new int[]{50, 70, 70, 170,  170, 190,  190,  170, 170,  70, 70, 50};
//
//            int[] ypoints = new int[]{50, 50, 30, 30, 50, 50, 150, 150, 170, 170, 150, 150};
//            Polygon p = new Polygon(xpoints, ypoints, n);
//            g2d.fill(p);
//            g2d.drawImage(bufferImage, 0, 0, this);
//            g2d.dispose();
        }

        private void upDateCurrentPath(ArrayList<Integer> X, ArrayList<Integer> Y){
            int i = 1;
            currentPathLines.clear();
            while(i < X.size()){
                currentPathLines.add(new Line2D.Double(X.get(i - 1), Y.get(i - 1), X.get(i), Y.get(i)));
                i += 1;
            }
        }

        private void drawPolygons(Graphics2D g2d, Color color){
            int i = polygonList.size() - 1;
            //polygonList.get(0);
            while(i >= 0){
                g2d.setColor(color);
                g2d.fill(polygonList.get(i));
                g2d.setColor(Color.WHITE);
                g2d.draw(polygonList.get(i));


                i -= 1;
            }
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
        private void drawQix(Graphics2D g2d,double x,double y){
            g2d.setColor(Color.WHITE);
            Rectangle2D qix = new Rectangle2D.Double(x,y, 20 ,20);
            g2d.fill(qix);
            g2d.draw(qix);

        }

        private void drawPath(Graphics2D g2d, ArrayList<Integer> X, ArrayList<Integer> Y){
            g2d.setColor(Color.WHITE);
//            int i = 1;
//            while(i < X.size()){
//                int x1 = X.get(i - 1);
//                int y1 = Y.get(i - 1);
//                int x2 = X.get(i);
//                int y2= Y.get(i);
//                g2d.drawLine(x1, y1, x2, y2);
//                i += 1;
//            }
            for(Line2D line: currentPathLines){
                g2d.draw(line);
            }
        }

        private void drawAllPaths(Graphics2D g2d){
            g2d.setColor(Color.WHITE);
            for(Line2D line: setPathLines){
                g2d.draw(line);
            }
//            int i = 0;
//            while(i < permanentPathLineListX.size()){
//                drawPath(g2d, permanentPathLineListX.get(i), permanentPathLineListY.get(i));
//                i++;
//            }
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

    public Display() throws AWTException {

        int[] topX = new int[]{startX,frameWidth - startX};
        int[] topY = new int[]{startY, startY};
        Line2D top = new Line2D.Double(startX, startY, frameWidth - startX, startY);

        int[] leftX = new int[]{startX, startX};
        int[] leftY = new int[]{startY, frameHeight - startY};
        Line2D left = new Line2D.Double(startX, startY, startX, frameHeight - startY);

        int[] bottomX = new int[]{startX, frameWidth - startX};
        int[] bottomY = new int[]{frameHeight - startY, frameHeight - startY};
        Line2D bottom = new Line2D.Double(startX, frameHeight - startY, frameWidth - startX, frameHeight - startY);

        int[] rightX = new int[]{frameWidth - startX, frameWidth - startX};
        int[] rightY = new int[]{startY, frameHeight - startY};
        Line2D right = new Line2D.Double(frameWidth - startX, startY, frameWidth - startX, frameHeight - startY);



        polygonList.add(top);
        polygonList.add(bottom);
        polygonList.add(left);
        polygonList.add(right);



        currentPathLines = new ArrayList<>();
        myRobot = new Robot();
        Board = new int[frameHeight - 2 * startY + 1][frameWidth - 2 * startX + 1];
        initBoardArray(Board);
        canvas = new GameDrawCanvas();
        infoDrawCanvas = new GameInfoDrawCanvas();
        canvas.setPreferredSize(new Dimension(frameWidth, frameHeight));
        infoDrawCanvas.setPreferredSize(new Dimension(frameWidth, 50));
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

    public void putNewEdgeInBoard(int direction, int X, int Y){
        this.Board[Y][X] = 1;
        if(direction == 3) {
            if(X > 0){
                X -= 1;
            }
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                X -= 1;
            }
        } else if(direction == 1){
            if(X < 710){
                X += 1;
            }
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                X += 1;
            }
        }else if(direction == 4){
            if(Y > 0){
                Y -= 1;
            }
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                Y -= 1;
            }
        }else if(direction == 2){
            if(Y < 560){
                Y += 1;
            }
            while (this.Board[Y][X] != 1) {
                this.Board[Y][X] = 1;
                Y += 1;
            }
        }
    }

    public void addToPolyArr(){
//        ArrayList<Integer> finalPoint = findPolygonCorner();
//        if(finalPoint.size() == 4){
//            int[] X = arrayListToIntArray(currentPathLineListX, 2);
//            int[] Y = arrayListToIntArray(currentPathLineListY, 2);
//            Y[Y.length - 2] = finalPoint.get(1);
//            X[X.length - 2] = finalPoint.get(0);
//            X[X.length - 1] = finalPoint.get(2);
//            Y[Y.length - 1] = finalPoint.get(3);
//            Polygon p = new Polygon(X, Y, X.length);
//            polygonList.add(p);
//        }else if(finalPoint.size() == 2) {
//            int[] X = arrayListToIntArray(currentPathLineListX, 1);
//            int[] Y = arrayListToIntArray(currentPathLineListY, 1);
//            Y[Y.length - 1] = finalPoint.get(1);
//            X[X.length - 1] = finalPoint.get(0);
//            Polygon p = new Polygon(X, Y, X.length);
//            polygonList.add(p);
//        }else{
//            int[] X = arrayListToIntArray(currentPathLineListX, 0);
//            int[] Y = arrayListToIntArray(currentPathLineListY, 0);
//            Polygon p = new Polygon(X, Y, X.length);
//            polygonList.add(p);
//        }
        findPolygonCorner();
        int[] X = arrayListToIntArray(currentPathLineListX);
        int[] Y = arrayListToIntArray(currentPathLineListY);
        Polygon p = new Polygon(X, Y, X.length);
        polygonList.add(p);

    }

    public void findPolygonCorner() {
        System.out.println("start");
        int[][] directionsToCheckLeft = new int[][]{{0, 1, -1, 0, 0, -1},
                {-1, 0, 0, -1, 1, 0}, {0, -1, 1, 0, 0, 1}, {1, 0, 0, 1, -1, 0}};
        int[][] directionsToCheckRight = new int[][]{{0, -1, -1, 0, 0, 1}, {1, 0, 0, -1, -1, 0}, {0, 1, 1, 0, 0, -1},
                {-1, 0, 0, 1, 1, 0}};
        int[] directionsToCount = new int[]{1, 0, -1, 0, -1, 0, 0, -1};
        ArrayList<Integer> leftX = (ArrayList<Integer>) currentPathLineListX.clone();
        ArrayList<Integer> leftY = (ArrayList<Integer>) currentPathLineListY.clone();

        ArrayList<Integer> rightX = (ArrayList<Integer>) currentPathLineListX.clone();
        ArrayList<Integer> rightY = (ArrayList<Integer>) currentPathLineListY.clone();

        ArrayList<Integer> r = new ArrayList<>();

        findCorners(endPathDirection, directionsToCheckLeft, leftX, leftY);
        findCorners(endPathDirection, directionsToCheckRight, rightX, rightY);
        float left = getAverageLength(leftX, leftY);
        float right = getAverageLength(rightX, rightY);
        //System.out.println(left);
        //System.out.println(right);
        if(left <= right){
            currentPathLineListX = (ArrayList<Integer>) leftX.clone();
            currentPathLineListY =  (ArrayList<Integer>) leftY.clone();
        }else{
            currentPathLineListX =  (ArrayList<Integer>) rightX.clone();
            currentPathLineListY =  (ArrayList<Integer>) rightY.clone();
        }
        System.out.println("end");

    }

    private float getAverageLength(ArrayList<Integer> pathX, ArrayList<Integer> pathY){
        float total = 0;
        for(int i = 1; i < pathX.size(); i++){
            total += Math.sqrt(Math.pow(pathX.get(i) - pathX.get(i - 1), 2) +  Math.pow(pathY.get(i) - pathY.get(i - 1), 2));
        }
        //System.out.println(pathX.size());
        //System.out.println(total);
        return total / pathX.size();
    }

    private void findCorners(int direction, int[][] directionArr, ArrayList<Integer> pathX, ArrayList<Integer> pathY){
        System.out.println(Arrays.toString(Board[1]));
        System.out.print(Arrays.toString(Board[2]));
        int[] directions = directionArr[direction - 1];
        int counter = 0;
        int oldx = playerX - 20;
        System.out.println(oldx);
        int oldy = playerY - 20;
        System.out.println(oldy);
        //System.out.println(Arrays.toString(Board[oldy]));
        int x = 0;
        int y = 0;
        int lastX = pathX.get(0);
        int lastY = pathY.get(0);
        int testCounter = 0;
//        System.out.println(lastY - playerY);
//        System.out.println(lastX - playerX);
        while(oldx + 20 != lastX || oldy + 20 != lastY) {
//            System.out.println(oldx);
//            System.out.println(oldy);
            testCounter += 1;
            directions = directionArr[direction - 1];
            for (int i = 1; i < 6; i += 2) {
                //System.out.println(y);
                x = oldx + directions[i - 1];
                y = oldy + directions[i];
                if (this.Board[y][x] == 1) {
                    if (i != 3 && counter != 0) {
                        pathX.add(oldx + 20);
                        pathY.add(oldy + 20);
                        //System.out.println("test");
                    }
                    counter += 1;
                    break;
                }
            }
            direction = getNewDirection(x, y, oldx, oldy);
            oldx = x;
            oldy = y;
        }


    }

    private int getNewDirection(int newx, int newy, int oldx, int oldy){
        if(newx - oldx == 1 && newy - oldy == 0){
            return 3;
        }else if(newx - oldx == -1){
            return 1;
        }else if(newy - oldy == 1){
            return 4;
        }else {
            return 2;
        }
    }

    private int[] getDirection(int dir){
        switch (dir){
            case 1:
                return new int[]{-1, 0};
            case 2:
                return new int[]{0, -1};
            case 3:
                return new int[]{1, 0};
            case 4:
                return new int[]{0, 1};
            default:
                return new int[]{0, 0};
        }
    }


    public int[] arrayListToIntArray(ArrayList<Integer> lst){

        int[] r = new int[lst.size()];
        int i = 0;
        while(i < lst.size()){
            r[i] = lst.get(i);
            i += 1;
        }
        return r;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        boolean update = false;
        clearPath = false;
        if(!direction.isEmpty()) {
            // int lastDirectionKey = direction.size() - 1;
            int dir = direction.get(direction.size() - 1);
            int lastSpeedKey = slowOrFast.size() - 1;
            int speed = 2;
            if(lastSpeedKey != -1){
                speed = slowOrFast.get(lastSpeedKey);
            }
            if(!moveOff){
                dir = 0;
            }

            switch(dir){
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
            boolean moveBack = false;

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
                //System.out.println("test");
                if (this.Board[trueY][trueX] > 1) {
                    moveBack = true;
                    while (this.Board[trueY][trueX] > 1) {
                        playerX -= playerXSpeed / 2;
                        playerY -= playerYSpeed / 2;
                        trueX -= playerXSpeed / 2;
                        trueY -= playerYSpeed / 2;

                    }
                }

                if(this.Board[lastY][lastX] == 1 && this.Board[trueY][trueX] >= 1){
//                    playerX = lastX + 20;
//                    playerY = lastY + 20;
                    startPathX = playerX;
                    startPathY = playerY;

                }else if(this.Board[lastY][lastX] == 1 && this.Board[trueY][trueX] == 0 || dir != lastDirection) {
//                    if(this.Board[trueY][trueX] >= 1){
//                        startPathX = lastX + 20;
//                        startPathY = lastY + 20;
//                    }
                    clearPath = false;
                    if(!moveBack && this.Board[trueY][trueX] !=1) {
                        startPathX = lastX + 20;
                        startPathY = lastY + 20;
//                    System.out.println(lastX);
////                    System.out.println(lastY);
                        if(currentPathLineListX.size() == 0){
                            startPathDirection = dir;
                        }
                        currentPathLineListX.add(lastX + 20);
                        currentPathLineListY.add(lastY + 20);
                        //if(currentPathLineListX.size() >= 2){
                           // System.out.println("test");
//                            int x1 = currentPathLineListX.get(currentPathLineListX.size() - 2);
//                            int x2 = currentPathLineListX.get(currentPathLineListX.size() - 1);
//                            int y1 = currentPathLineListX.get(currentPathLineListY.size() - 2);
//                            int y2 = currentPathLineListX.get(currentPathLineListY.size() - 1);
//                            currentPathLines.add(new Line2D.Double(x1, y1, x2, y2));
                            //System.out.println(currentPathLineListX.size());
                       // }
                        if (dir != lastDirection) {
                            putNewEdgeInBoard(lastDirection, lastX, lastY);

                        } else {
                            //putNewEdgeInBoard(lastDirection, playerX, playerY);
                        }
                    }
                }


            } else if (!moveOff && this.Board[lastY][lastX] == 1) {
//                playerX = lastX + 20;
//                playerY = lastY + 20;
//                trueX = lastX;
//                trueY = lastY;
//                startPathY = playerY;
//                startPathX = playerX;
                while (this.Board[trueY][trueX] != 1) {
                    playerX -= playerXSpeed / 2;
                    playerY -= playerYSpeed / 2;
                    trueX -= playerXSpeed / 2;
                    trueY -= playerYSpeed / 2;
                    startPathY = playerY;
                    startPathX = playerX;

                }
            }

            if(this.Board[trueY][trueX] == 1 && slowOrFast.size() == 0){
                moveOff = false;
                lastMoveOff = false;
                startPathX = playerX;
                startPathY = playerY;
            }
            if(this.Board[trueY][trueX] == 1 && this.currentPathLineListX.size() > 0){
                //System.out.println("the fuck");
                this.currentPathLineListX.add(lastX + 20);
                this.currentPathLineListY.add(lastY + 20);
                this.currentPathLineListX.add(trueX + 20);
                this.currentPathLineListY.add(trueY + 20);
//                int x1 = currentPathLineListX.get(currentPathLineListX.size() - 2);
//                int x2 = currentPathLineListX.get(currentPathLineListX.size() - 1);
//                int y1 = currentPathLineListX.get(currentPathLineListY.size() - 2);
//                int y2 = currentPathLineListX.get(currentPathLineListY.size() - 1);
//                currentPathLines.add(new Line2D.Double(x1, y1, x2, y2));
                startPathX = playerX;
                startPathY = playerY;
                //System.out.println(this.currentPathLineListX.size());
                clearPath = true;
                //moveOff =false;
                update = true;
                putNewEdgeInBoard(lastDirection, lastX, lastY);
                putNewEdgeInBoard(dir, trueX, trueY);
                endPathDirection = dir;
                //fillBoardArea();

                // System.out.println(Arrays.toString(this.Board[trueY]));
//                System.out.println(clearPath);
            }

            lastDirection = dir;

        }

        moveQix();
        checkQix();
        repaint();

    }
    public void checkQix(){
        Rectangle2D testRect = new Rectangle2D.Double(qixX,qixY,20,20);
        if (currentLine.intersects(testRect)){
            System.out.println("LOST");
            currentLine= new Line2D.Double(0,0,0,0);
            currentPathLines.clear();
            currentPathLineListX.clear();
            currentPathLineListY.clear();
            playerX=pushX;
            playerY=pushY;
            startPathX = playerX;
            startPathY = playerY;
            trueX = playerX - 20;
            trueY = playerY - 20;
            System.out.println(currentPathLines.size());
        }
        for (Line2D l: currentPathLines){
            if (l.intersects(testRect)){
                System.out.println("LOST");
                currentLine= new Line2D.Double(0,0,0,0);
                currentPathLines.clear();
                currentPathLineListX.clear();
                currentPathLineListY.clear();
                startPathX = playerX;
                startPathY = playerY;
                playerX=pushX;
                playerY=pushY;
                startPathX = playerX;
                startPathY = playerY;
                trueX = playerX - 20;
                trueY = playerY - 20;
                System.out.println(currentPathLines.size());
                break;
            }
        }
    }
    public void moveQix () {

        Random chance= new Random();
        Random chance2= new Random();
        int chanceNum= chance.nextInt(101);
        int lastcoord1=qixX;
        int lastcoord2=qixY;
        int moveX=0;
        int moveY=0;
        int movedir=0;
        if (chanceNum<=33){
            moveX=1;
        }
        else if (chanceNum>33 && chanceNum<=66){
            moveY=1;
        }
        else  if (chanceNum>66){
            moveX=1;
            moveY=1;
        }
        int dirNum= chance2.nextInt(101);
        if (dirNum<=50){
            movedir=0;
        }
        else {
            movedir=1;
        }

            if (moveX==1 && movedir==1){
                qixX= qixX+5;
            }
            else if (moveY==1 && movedir==1) {
                qixY = qixY + 5;
            }
            if (moveX==1 && movedir==0){
                qixX= qixX-5;
            }
            else if (moveY==1 && movedir==0) {
                qixY = qixY - 5;
            }
            if (moveX==1 && moveY==1 && movedir==1){
                qixX= qixX+5;
                qixY= qixY+5;
            }
            else if (moveX==1&&moveY==1 && movedir==0) {
                qixX=qixX-5;
                qixY=qixY-5;
            }
            Rectangle2D testRect = new Rectangle2D.Double(qixX,qixY,20,20);
            for (Shape p : polygonList){
                if (p.intersects(testRect)){
                        qixX=lastcoord1;
                        qixY=lastcoord2;
                }
            }
    }



    private void updateBoard() {
        //System.out.println(polygonList.size());
        Point p = new Point();
        for (int row = 20; row <= 580; row++) {
            for (int col = 20; col <= 730; col++) {
                p.x = col;
                p.y = row;
                if (this.Board[row - 20][col - 20] != 1) {
                    for(int i = 0; i < polygonList.size(); i++){
                        if(polygonList.get(i).contains(p)){
                            this.Board[row - 20][col - 20] = i + 2;
                        }
                    }
//                    for (Polygon poly : polygonList) {
//                        if (poly.contains(p)) {
//                            this.Board[row - 20][col - 20] = 2;
//                        }
//                    }

                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    public boolean canMove(int xpos, int ypos, int dir){
        if(dir == 1){
            if(xpos - 1 >= 0){
                if(this.Board[ypos][xpos - 1] == 1){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }else if(dir == 2){
            if(ypos - 1 >= 0){
                if(this.Board[ypos - 1][xpos] == 1){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }else if(dir == 3){
            if(xpos + 1 <= 710){
                if(this.Board[ypos][xpos + 1] == 1){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }else if(dir == 4){
            if(ypos + 1 <= 560){
                if(this.Board[ypos + 1][xpos] == 1){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                playerYSpeed = -drawSpeed;
                playerXSpeed = 0;


                if(!isUpPressed && (lastDirection != 4 || canMove(trueX,  trueY, 2))){
                    direction.add(2);
                }
                isUpPressed = true;

                break;
            case KeyEvent.VK_DOWN:
                playerYSpeed = drawSpeed;
                playerXSpeed = 0;

                if(!isDownPressed && (lastDirection != 2|| canMove(trueX, trueY, 4))){
                    direction.add(4);
                }
                isDownPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                playerXSpeed = -drawSpeed;
                playerYSpeed = 0;

                if(!isLeftPressed && (lastDirection != 3|| canMove(trueX, trueY, 1))){
                    direction.add(1);
                }
                isLeftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                playerXSpeed = drawSpeed;
                playerYSpeed = 0;

                if(!isRightPressed && (lastDirection != 1|| canMove(trueX, trueY, 3))){
                    direction.add(3);
                }
                isRightPressed = true;
                break;
            case KeyEvent.VK_F:
                drawSpeed = fastSpeed;
                if(moveOff){
                    lastMoveOff = true;
                }
                else {
                    pushX = playerX;
                    pushY =playerY;
                }
                moveOff = true;
//                startPathY = playerY;
//                startPathX = playerX;
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
                else {
                    pushX= playerX;
                    pushY= playerY;
                }
//                if(!moveOff) {
//                    startPathY = playerY;
//                    startPathX = playerX;
//                }
                moveOff = true;

                if(!isSlowPressed){
                    slowOrFast.add(slowSpeed);
                }
                isSlowPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
//        System.out.println(this.Board[trueY][trueX]);
//        System.out.println(moveOff);
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
                //moveOff = false;
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

    public static void main(String[]args) throws AWTException {
        //opens the window by creating a new Display class
        Display d = new Display();




    }
}