package Main;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import Objects.Enemy;
import Objects.Player;
import Objects.Shot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Screen extends JPanel {



	private Dimension d;
    private List<Enemy> enemies;
    private Player player;
    private Shot shot;
    
    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer speed;
    public double imageAngleRad = 0;
    private Point mousePoint;
    public Screen() {

        initScreen();
        gameInit();
    }

    private void initScreen() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Variables.Screen_Width, Variables.Screen_Height);
        setBackground(Color.black);

        speed = new Timer(Variables.Speed, new GameCycle());
        speed.start();

        gameInit();
    }


    private void gameInit() {

        enemies = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                var alien = new Enemy(Variables.Enemy_StartX + 18 * j,
                        Variables.Enemy_StartY + 18 * i);
                enemies.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();
    }

    private void drawEnemies(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }
    
    
    
    
    

    
    
    
    
    
    

    private void drawPlayer(Graphics g) {
    	  Graphics2D g2 = (Graphics2D)g;
    	 AffineTransform tr = new AffineTransform();
    	 

    	 double rotation = 0f;
    	 if (mousePoint != null) {
         int deltaX = mousePoint.x ;
         int deltaY = mousePoint.y ;

         rotation = -Math.atan2(deltaX, deltaY);

         rotation = Math.toDegrees(rotation) + 180;
    	 }
    	 tr.translate(player.getX(), player.getY());
    	    tr.rotate(
    	            Math.toRadians(rotation),
    	            Player.getWidth() / 2
    	          
    	    );
        if (player.isVisible()) {
 
        	
            g2.drawImage(player.getImage(), tr, this);
            
           
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }
    
    public class RotatePane extends javax.swing.JPanel implements MouseListener {
 

	public RotatePane() {
		
		    	
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
            	System.out.println("boom");
                mousePoint = e.getPoint();
               
                repaint();

            }

        }
        );
        }
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Line Clicked !");
    }
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    	}
    
    

    
    
    
 void drawShot(Graphics g) {

        if (shot.isVisible()) {
        	
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Enemy a : enemies) {

            Enemy.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (inGame) {
            drawEnemies(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (speed.isRunning()) {
                speed.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Variables.Screen_Width, Variables.Screen_Height);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Variables.Screen_Width / 2 - 30, Variables.Screen_Width - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Variables.Screen_Width / 2 - 30, Variables.Screen_Width - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Variables.Screen_Width - fontMetrics.stringWidth(message)) / 2,
                Variables.Screen_Width / 2);
    }

    private void update() {

        if (deaths == Variables.Enemies_to_Kill) {

            inGame = false;
            speed.stop();
            message = "Game won!";
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Enemy enemy : enemies) {

                int alienX = enemy.getX();
                int alienY = enemy.getY();

                if (enemy.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Variables.Enemy_Width)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Variables.Enemy_Height)) {

                        var ii = new ImageIcon(explImg);
                        enemy.setImage(ii.getImage());
                        enemy.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // aliens

        for (Enemy enemy : enemies) {

            int x = enemy.getX();

            if (x >= Variables.Screen_Width - Variables.Border_Right && direction != -1) {

                direction = -1;

                Iterator<Enemy> i1 = enemies.iterator();

                while (i1.hasNext()) {

                    Enemy a2 = i1.next();
                    a2.setY(a2.getY() + Variables.Enemy_Descent);
                }
            }

            if (x <= Variables.Border_Left && direction != 1) {

                direction = 1;

                Iterator<Enemy> i2 = enemies.iterator();

                while (i2.hasNext()) {

                    Enemy a = i2.next();
                    a.setY(a.getY() + Variables.Enemy_Descent);
                }
            }
        }

        Iterator<Enemy> it = enemies.iterator();

        while (it.hasNext()) {

            Enemy enemy = it.next();

            if (enemy.isVisible()) {

                int y = enemy.getY();

                if (y > Variables.Bottom - Variables.Enemy_Height) {
                    inGame = false;
                    message = "Invasion!";
                }

                enemy.act(direction);
            }
        }

        // bombs
        var generator = new Random();

        for (Enemy enemy : enemies) {

            int shot = generator.nextInt(15);
            Enemy.Bomb bomb = enemy.getBomb();

            if (shot == Variables.Shot_Randomiser && enemy.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(enemy.getX());
                bomb.setY(enemy.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Variables.Player_Width)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Variables.Player_Height)) {

                    var ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Variables.Bottom - Variables.Shot_HitboxHeight) {

                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}
