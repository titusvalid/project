package Objects;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Main.Variables;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Image;
import java.awt.Point;

public class Player extends Texture {

    private static int width;

    public Player() {

        initPlayer();
    }

    private void initPlayer() {

        var playerImg = "src/images/player.png";
        var ii = new ImageIcon(playerImg);

        setWidth(ii.getImage().getWidth(null));
        setImage(ii.getImage());

        int START_X = 270;
        setX(START_X);

        int START_Y = 250;
        setY(START_Y);
    }
    

        
    public void act() {
    	

    	
    	
    	
    	setX(x+= dx) ;
    	setY(y+= dy);
    	
        if (x <= 2) {

            x = 2;
        }

        if (x >= Variables.Screen_Width - 2 * getWidth()) {

            x = Variables.Screen_Width - 2 * getWidth();
        }
        if (y <= 2) {

            y = 2;
        }

        if (y >= Variables.Screen_Height - 2 * getWidth()) {

            y = Variables.Screen_Height - 2 * getWidth();
        }
    }

    
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 2;
        }
        if (key == KeyEvent.VK_UP) {

            dy = -2;
        }
        if (key == KeyEvent.VK_DOWN) {

            dy = 2;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
        if (key == KeyEvent.VK_UP) {

            dy = 0;
        }
        if (key == KeyEvent.VK_DOWN) {

            dy = 0;
        }
    }

	public static int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}


}

