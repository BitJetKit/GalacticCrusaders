import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Panel extends JPanel implements KeyListener {
	static BufferedImage img, img2;
	static int imgY;
	
	static int screenWidth = 1280;
	static int screenHeight = 720;
	int frameRate = 100;
	
	int numOfAliens = 0;
	int bulletFrequency = 50;
	int lives = 3;
	
	int lvl = 0;
	int score = 0;
	int tempScore = score;

	boolean isPause;
	boolean isLeft, isRight;
	boolean isCollision;
	boolean isGameEnd;
	
	ArrayList<Alien> aliens = new ArrayList<Alien>();
	Spaceship p1;
	
	String font = "Atari Font Full Version";

	public Panel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK);											//TODO Add sound
		addKeyListener(this);
		
		imgY = 0;
		try {
			img = ImageIO.read(new File("Starfield.png"));		//TODO Fix choppy starfield movement
			img2 = img;
		} catch (IOException e) {
			System.out.println(e);
		}
		
		p1 = new Spaceship(4, 4, lives);		//TODO Create computer player
		nextLevel();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(img, 0, imgY, screenWidth, screenHeight, null);
		g.drawImage(img2, 0, imgY - screenHeight, screenWidth, screenHeight, null);
		
		p1.draw(g2);
		for (Alien a : aliens) a.draw(g2);
		
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font(font, Font.PLAIN, 16));		//TODO Draw lives
		g.drawString("Level: " + lvl, 10, 20);
		g.drawString("" + score, 10, 40);
		
		if (isPause && !isGameEnd) {
			Color background = new Color (0, 0, 0, 200);
			g.setColor(background);
			g.fillRect(0, 0, screenWidth, screenHeight);	//TODO Display score and level
			g.setColor(Color.BLUE);
			drawCenteredString(g, "Press p to play", new Rectangle (0, 0, screenWidth, 3 * screenHeight / 4), new Font (font, Font.PLAIN, 28));
		}
		
		if (isGameEnd) {
			isPause = true;
			Color background = new Color (0, 0, 0, 200);
			g.setColor(background);
			g.fillRect(0, 0, screenWidth, screenHeight);
			g.setColor(Color.RED);
			drawCenteredString(g, "gg", new Rectangle (0, 0, screenWidth, screenHeight), new Font (font, Font.BOLD, 28));
			drawCenteredString(g, "Press r to reset", new Rectangle (0, 0, screenWidth, 3 * screenHeight / 4), new Font (font, Font.PLAIN, 28));
		}
	}

	public void run() {
		while (true) {
			if (!isPause) {
				imgY += 4;
				if (imgY >= screenHeight) {
					//System.out.println(imgY);
					imgY = 0;
				}
					
				p1.update(isCollision);
				isCollision = false;

				for (Alien a : aliens) {
					if (a.bullet.intersects(p1.body) || a.bullet.intersects(p1.gunBody)) {
						p1.health--;
						lives--;
						score -= 50;
						isCollision = true;
						if (p1.health == 0) {
							isGameEnd = true;
							break;
						}
					}
					
					a.update(isCollision);
					if ((int) (Math.random() * bulletFrequency) == 1) {
						a.isShoot = true;
					}
					
					if (p1.health == 0) {
						isGameEnd = true;
					}
					
					if (p1.bullet.intersects(a.bullet)) {		//TODO Fix collision bug
						isCollision = true;
						a.resetBullet();
					}
					
					if (p1.bullet.intersects(a.body)) {
						isCollision = true;
						a.health--;
					}
					
					if (a.health == 0) {
						a.isDead = true;
					}
				}
					
				for (int i = 0; i < aliens.size(); i++) {
					if (aliens.get(i).isDead) {
						aliens.remove(i);
						score += 100;
					}
				}
				
				tempScore = score;
				
				if (aliens.isEmpty()) {
					tempScore = score;
					reset();
					nextLevel();
				}
					
				if (isRight) p1.moveShip(1);
				if (isLeft)  p1.moveShip(-1);
			}
			repaint();
			delay(1000 / frameRate);
		}
	}
	
	public void nextLevel() {		//TODO Balance levels
		//tempScore = score;
		aliens.clear();
		score = tempScore;
		lvl++;
		isPause = true;
		
		if (lvl == 3) {
			numOfAliens = 5;
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (25+20), 50, 8, 3, 2));
			}
		} else if (lvl == 5) {
			numOfAliens = 5;
			bulletFrequency = 40;
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (25), 50, 10, 4, 2));
			} 
		} else {
			numOfAliens = lvl * 3;
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (25+20), 50, 8, 3, 1));
			}
		}
	}
	
	public void reset() {
		lives = 3;
		p1 = new Spaceship(4, 4, lives);
		isGameEnd = false;
		isPause = true;
		aliens.clear();
		score = 0;
		lvl--;
		nextLevel();
	}
	
	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}

	public void delay (int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void keyPressed (KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) isRight = true;
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) isLeft = true;
	}

	@Override
	public void keyReleased (KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) isRight = false;
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) isLeft = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == ' ' || e.getKeyCode() == KeyEvent.VK_UP) p1.shoot();
		
		if (e.getKeyChar() == 'q') System.exit(1);
		
		if (e.getKeyChar() == 'p') isPause = !isPause;
		
		if (e.getKeyChar() == 'r') reset();
		
		if (e.getKeyChar() == 's') nextLevel();
	}
}