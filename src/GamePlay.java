import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 100;
    private int ballposY = 300;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 695, 620);

        // drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 620);
        g.fillRect(0, 0, 695, 3);
        g.fillRect(692, 0, 3, 620);

        // the scores
        g.setColor(Color.white);
        g.setFont(new Font(Font.DIALOG,Font.BOLD, 25));
        g.drawString("" + score, 645,30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        //termination of the game
        if(totalBricks == 0) {
            play = false;
            g.setColor(Color.red);
            g.setFont(new Font(Font.DIALOG,Font.BOLD, 45));
            g.drawString("YOU WON!", 235,300);
            g.drawString("Press ENTER to restart", 100,340);
        }
        if(ballposY > 620) {
            play = false;
            g.setColor(Color.red);
            g.setFont(new Font(Font.DIALOG,Font.BOLD, 35));
            g.drawString("GAME OVER!", 220, 300);
            g.drawString("Press ENTER to restart", 140,340);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play) {
            //check if the ball hits the paddle
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8))) {
                ballXdir = -2;
                ballYdir = -ballYdir;
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8))) {
                ballXdir = (ballXdir < 0) ? -1 : 1;
                ballYdir = -ballYdir;
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8))) {
                ballXdir = 2;
                ballYdir = -ballYdir;
            }
            //check if the ball hits the borders
            if(ballposX < 0 || ballposX > 670) {
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {
                ballYdir = -ballYdir;
            }
            //check if the ball hits the bricks
            A: for(int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        Rectangle brickRect = new Rectangle(j * map.brickWidth + 80, i * map.brickHeight + 50, map.brickWidth, map.brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20,20);
                        if(new Rectangle(ballposX, ballposY, 20, 20).intersects(brickRect)) {
                            if(ballRect.x + 19 == brickRect.x || ballRect.x == brickRect.x + brickRect.width - 1) {
                                ballXdir = - ballXdir;
                            } else {
                                ballYdir = - ballYdir;
                            }
                            map.map[i][j] = 0;
                            totalBricks--;
                            score += 5;
                            break A;
                        }
                    }
                }
            }
            //make the ball move if there's no collision
            ballposX += ballXdir;
            ballposY += ballYdir;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 590) {
                playerX = 590;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 5) {
                playerX = 5;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                score = 0;
                totalBricks = 21;
                playerX = 310;
                ballposX = 100;
                ballposY = 300;
                ballXdir = -1;
                ballYdir = -2;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }
        public void moveRight() {
            play = true;
            playerX+=20;
        }
        public void moveLeft() {
            play = true;
            playerX-=20;
        }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased (KeyEvent e) {}
    }
