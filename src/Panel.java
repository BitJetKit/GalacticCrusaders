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
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.*;
import javax.swing.JPanel;

@SuppressWarnings({ "serial", "unused" })
public class Panel extends JPanel implements KeyListener {
	static BufferedImage img, img2, crosshair;
	static int imgY;
	static BufferedImage hitmarker, MLGlogo;

	static int screenWidth = 1280;
	static int screenHeight = 720;
	int frameRate = 100;

	int numOfAliens = 0;
	int alienLives  = 1;
	int holdALives  = alienLives;
	int bulletFrequency = 50;

	int bulletSpeed = 4;
	int shipSpeed   = 4;
	int lives = 3;
	int holdLives = lives;

	int lvl = 1;
	int score = 0;
	int tempScore = score;
	int kills = 0;

	boolean isPause, isHelp;
	boolean isLeft, isRight, isShoot;
	boolean isGameEnd;
	boolean isComp;
	boolean isCheat, isAimBot;

	ArrayList<Alien> aliens = new ArrayList<Alien>();
	Alien temp;
	Spaceship p1, comp;

	String font = "Atari Font Full Version";

	Sound hitSound;
	
	boolean isMLG;

	TaskRunner sound;

	public Panel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK); // TODO Add sound
		addKeyListener(this);

		hitSound = new Sound("MLG\\HitmarkerSound.wav");
		sound = new TaskRunner(1);
		
		try {
			img  = ImageIO.read(new File("Starfield.png"));
			img2 = img;
			crosshair = ImageIO.read(new File("Crosshair.png"));
			hitmarker = ImageIO.read(new File("MLG\\Hitmarker.png"));
			MLGlogo   = ImageIO.read(new File("MLG\\MLGLogo.png"));
		} catch (IOException e) {
			System.out.println(e);
		}

		p1   = new Spaceship(bulletSpeed, shipSpeed, lives);
		comp = new Spaceship(bulletSpeed, shipSpeed, lives);
		createLevel();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g.drawImage(img, 0, imgY, screenWidth, screenHeight, null);
		g.drawImage(img2, 0, imgY - screenHeight, screenWidth, screenHeight, null);

		if (isComp) {
			comp.draw(g2);
		} else {
			p1.draw(g2);
		}

		for (Alien a : aliens) {
			a.draw(g2);
		}

		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font(font, Font.PLAIN, 16));
		g.drawString("Level: " + lvl, 10, 20);
		g.drawString("" + score, 10, 40);
		g.drawString("Lives: " + lives, screenWidth - 125, 20);

		if (isCheat) {
			g.drawImage(crosshair, 250, 10, 25, 25, null);
		}
		
		if (isPause && !isGameEnd && !isHelp) {
			Color background = new Color(0, 0, 0, 200);
			g.setColor(background);
			g.fillRect(0, 0, screenWidth, screenHeight);
			g.setColor(Color.BLUE);
			drawCenteredString(g, "Press p to play", new Rectangle(0, 0, screenWidth, (3 * screenHeight) / 4),
					new Font(font, Font.PLAIN, 28));
			g.setColor(Color.GRAY);
			drawCenteredString(g, "Press h for help", new Rectangle(0, 0, screenWidth, screenHeight),
					new Font(font, Font.PLAIN, 20));
		}
		
		if (isHelp && isPause) {
			Color background = new Color(0, 0, 0, 200);
			g.setColor(background);
			g.fillRect(0, 0, screenWidth, screenHeight);
			g.setColor(Color.GRAY);
			drawCenteredString(g, "Arrow keys or A and D to Move", new Rectangle(0, 0, screenWidth, (3 * screenHeight) / 4), 
					new Font(font, Font.PLAIN, 20));
			drawCenteredString(g, "Space or Up to Shoot", new Rectangle(0, 0, screenWidth, screenHeight), 
					new Font(font, Font.PLAIN, 20));
			drawCenteredString(g, "L to Skip Level", new Rectangle(0, 0, screenWidth, (5 * screenHeight) / 4), 
					new Font(font, Font.PLAIN, 20));
		}

		if (isGameEnd) {
			isPause = true;
			Color background = new Color(0, 0, 0, 200);
			g.setColor(background);
			g.fillRect(0, 0, screenWidth, screenHeight);
			g.setColor(Color.WHITE);
			drawCenteredString(g, "Final score: " + score, new Rectangle(0, 0, screenWidth, screenHeight),
					new Font(font, Font.PLAIN, 24));
			drawCenteredString(g, "Final level: " + lvl, new Rectangle(0, 0, screenWidth, (5 * screenHeight) / 4),
					new Font(font, Font.PLAIN, 24));
			g.setColor(Color.RED);
			drawCenteredString(g, "Press r to retry", new Rectangle(0, 0, screenWidth, (3 * screenHeight) / 4),
					new Font(font, Font.PLAIN, 28));	
		}											

		if (isComp) {
			g.setColor(new Color (169, 169, 169, 200));
			drawCenteredString(g, "COMPUTER", new Rectangle(0, 0, screenWidth, 50),
					new Font(font, Font.PLAIN, 20));
		}
		
		if (isMLG) {
			g.drawImage(MLGlogo, Panel.screenWidth / 2 - 50, 0, 100, 50, null);
			if (isGameEnd) {
				g2.translate(p1.body.getX(), p1.body.getY());
				g2.scale(1.25, 1.25);
				g2.translate(-p1.body.getX(), -p1.body.getY());
			}
		}
	}

	public void drawKill(Alien a) {
		Graphics g = this.getGraphics();
		g.setColor(Color.GREEN);
		g.drawLine((int)(a.body.getX() - 10), (int)(a.body.getY() - 10), (int)(a.body.getX() - 1), (int)(a.body.getY() - 1));
		delay(50);
	}
	
	public void drawHit(){
		Graphics g = this.getGraphics();
		sound.runTask( () -> {hitSound.play();});
		g.drawImage(hitmarker, (int)(p1.bullet.getX()-25), (int)(p1.bullet.getY()-25), 50, 50, null);
		delay(50);
	}

	public void run() {
		while (true) {
			if (!isPause) {
				imgY += 2;
				if (imgY >= screenHeight) imgY = 0;
				
				if (isComp) {
					playComp();
				} else {
					int errorLimit = 2;
					for (Alien a : aliens) {
						if (isAimBot) {
							if (a.isLeft) {
								if ((p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
										/ p1.bulletSpeed <= (a.body.getCenterX() - p1.bullet.getCenterX()) / a.speed + errorLimit
										&& (p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
												/ p1.bulletSpeed >= (a.body.getCenterX() - p1.bullet.getCenterX()) / a.speed - errorLimit) {
									p1.shoot();
								}
							} else {
								if ((p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
										/ p1.bulletSpeed <= (p1.bullet.getCenterX() - a.body.getCenterX()) / a.speed + errorLimit
										&& (p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
												/ p1.bulletSpeed >= (p1.bullet.getCenterX() - a.body.getCenterX()) / a.speed - errorLimit) {
									p1.shoot();
								}
							}
						}
						
						if (a.bullet.intersects(p1.body) || a.bullet.intersects(p1.gunBody)) {
							p1.health--;
							lives--;
							score -= 50;
							a.isCollision = true;
							if (p1.health == 0) {
								isGameEnd = true;
								break;
							}
						}

						if ((int) (Math.random() * bulletFrequency) == 1) {
							a.isShoot = true;
						}

						if (a.body.intersects(p1.body)) {
							a.health--;
							p1.health--;
							score -= 100;
							if (p1.health == 0) {
								isGameEnd = true;
								break;
							}
						}

						if (p1.bullet.intersects(a.bullet)) {
							a.isCollision = true;
							p1.isCollision = true;
						}

						if (p1.bullet.intersects(a.body)) { // TODO Fix disappearing bullet bug
							if (isMLG) {
								drawHit();
							}
							p1.isCollision = true;
							score += 100 / alienLives;
							a.health--;
						}

						if (a.health == 0) { // TODO Add kill animation
							a.isDead = true;
						}

						a.update();
						a.isCollision = false;
					}

					p1.update();
					p1.isCollision = false;

					if (aliens.isEmpty()) {
						tempScore = score;
						p1 = new Spaceship(bulletSpeed, shipSpeed, lives);
						holdALives = alienLives;
						lvl++;
						createLevel();
					}

					if (isRight && p1.body.getX() + p1.body.getWidth() < screenWidth)
						p1.move(1);
					if (isLeft && p1.body.getX() > 0)
						p1.move(-1);
					if (isShoot)
						p1.shoot();
				}

				for (int i = 0; i < aliens.size(); i++) {
					if (aliens.get(i).isDead) {
						kills++;
						temp = aliens.get(i);
						aliens.remove(i);
					}
				}
				
				

				if (kills % 3 == 0 && isMLG) {
					
				}
				
				tempScore = score;

			}
			repaint();
			delay(1000 / frameRate);
		}
	}

	public void playComp() {
		boolean isControl = false;
		int errorLimit = 2;
		
		for (Alien a : aliens) {
			
			if (a.isLeft) {
				if ((comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
						/ comp.bulletSpeed <= (a.body.getCenterX() - comp.bullet.getCenterX()) / a.speed + errorLimit
						&& (comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
								/ comp.bulletSpeed >= (a.body.getCenterX() - comp.bullet.getCenterX()) / a.speed - errorLimit) {
					comp.shoot();
				}
			} else {
				if ((comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
						/ comp.bulletSpeed <= (comp.bullet.getCenterX() - a.body.getCenterX()) / a.speed + errorLimit
						&& (comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
								/ comp.bulletSpeed >= (comp.bullet.getCenterX() - a.body.getCenterX()) / a.speed - errorLimit) {
					comp.shoot();
				}
			}
			
			moveComp(a);
			//isControl = noMoveComp(a);

			if (a.bullet.intersects(comp.body) || a.bullet.intersects(comp.gunBody)) {
				comp.health--;
				lives--;
				score -= 50;
				a.isCollision = true;
				if (comp.health == 0) {
					isGameEnd = true;
					break;
				}
			}

			if ((int) (Math.random() * bulletFrequency) == 1) {
				a.isShoot = true;
			}

			if (a.body.intersects(comp.body)) {
				a.health--;
				comp.health--;
				score -= 100;
				if (comp.health == 0) {
					isGameEnd = true;
					break;
				}
			}

			if (comp.bullet.intersects(a.bullet)) {
				a.isCollision = true;
				comp.isCollision = true;
			}

			if (comp.bullet.intersects(a.body)) {
				comp.isCollision = true;
				score += 100 / alienLives;
				a.health--;
			}

			if (a.health == 0) { 
				a.isDead = true;
			}

			a.update();
			a.isCollision = false;
		}
		
		if (!isLeft && !isControl) {
			comp.move(1);
			if (comp.body.x >= screenWidth - comp.body.width - 150) {
				isLeft = true;
			}
		}
		
		if (isLeft && !isControl) {
			comp.move(-1);
			if (comp.body.x <= 150) {
				isLeft = false;
			}
		}

		comp.update();
		comp.isCollision = false;

		if (aliens.isEmpty()) {
			tempScore = score;
			comp = new Spaceship(bulletSpeed, shipSpeed, lives);
			holdALives = alienLives;
			lvl++;
			createLevel();
			isPause = false;
		}
		
		if (isGameEnd) {
			reset();
		}
	}
	
	public void moveComp (Alien a) {
		int errorLimit = 4;
		if (a.bullet.getCenterX() >= comp.body.getCenterX() && 
			a.bullet.getMinX() - errorLimit <= comp.body.getMaxX() &&			//Right-half of ship
			comp.gunBody.getY() - a.bullet.getMaxY() <= 200) {
			
			isLeft = true;
		}
		
		if (a.bullet.getCenterX() < comp.body.getCenterX() && 
			a.bullet.getMaxX() + errorLimit >= comp.body.getMinX() && 			//Left-half of ship
			comp.gunBody.getY() - a.bullet.getMaxY() <= 200) {
			
			isLeft = false;
			}
	}
	
	public boolean noMoveComp (Alien a) {
		int errorLimit = 2;
		if (a.bullet.getCenterX() >= comp.body.getCenterX() && 
			a.bullet.getMinX() - errorLimit <= comp.body.getMaxX() &&			//Right-half of ship
			comp.gunBody.getY() - a.bullet.getMaxY() <= 200) {
			
			comp.move(-1);
			return true;
		}
			
		if (a.bullet.getCenterX() < comp.body.getCenterX() && 
			a.bullet.getMaxX() + errorLimit >= comp.body.getMinX() && 			//Left-half of ship
			comp.gunBody.getY() - a.bullet.getMaxY() <= 200) {
			
			comp.move(1);
			return true;
		}
		return true;
	}

	public void createLevel() { // TODO Balance levels?
		aliens.clear();
		score = tempScore;
		isPause = true;

		if ((lvl - 3) % 5 == 0) { // Every 3rd level starting from 5s
			numOfAliens = Math.min(23, lvl);
			alienLives = holdALives + 1;
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (27 + 20), 50, 6, 3, alienLives));
			}
		} 
		
		else if (lvl % 10 == 0) { // Every 10th level
			numOfAliens = Math.min(20, ((lvl * 4) / 5)/2);
			alienLives = Math.min(4, holdALives + 1);
			bulletFrequency = 40;
			for (int i = 0; i < numOfAliens - 2; i++) {
				aliens.add(new Alien((i+1) * (27 + 1), 50, 8, 4, alienLives));
			}
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (27 + 1), 50 + 27, 8, 4, alienLives));
			}
		} 
		
		else if (lvl % 5 == 0) { // Every 5th level
			numOfAliens = Math.min(20, (lvl * 4) / 5);
			alienLives = Math.min(4, holdALives + 1);
			bulletFrequency = 40;
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (27 + 1), 50, 8, 4, alienLives));
			}
		} 
		
		else { // Every 1st, 2nd, and 4th level starting from 5s
			numOfAliens = Math.min(28, lvl * 2);
			if ((lvl - 4) % 5 == 0) alienLives = holdALives - 1;
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (27 + 20), 50, 6, 3, alienLives));
			}
		}
	}

	public void reset() {
		lives = holdLives;
		if (isComp) {
			comp = new Spaceship(bulletSpeed, shipSpeed, lives);
		} else {
			p1 = new Spaceship(bulletSpeed, shipSpeed, lives);
		}
		
		isGameEnd = false;
		score = 0;
		tempScore = 0;
		createLevel();
		isPause = false;
	}

	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g
	 *            The Graphics instance.
	 * @param text
	 *            The String to draw.
	 * @param rect
	 *            The Rectangle to center the text in.
	 */
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);
		// Determine the X coordinate for the text
		int x = (rect.width - metrics.stringWidth(text)) / 2;
		// Determine the Y coordinate for the text (note we add the ascent, as
		// in java 2d 0 is top of the screen)
		int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		// Set the font
		g.setFont(font);
		// Draw the String
		g.drawString(text, x, y);
	}

	public void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (!isComp) isShoot = true;
			else frameRate++;
			break;

		case KeyEvent.VK_RIGHT:
			isRight = true;
			break;

		case KeyEvent.VK_LEFT:
			isLeft = true;
			break;
			
		case KeyEvent.VK_DOWN:
			if (isComp) frameRate--;
			break;
		}

		switch (e.getKeyChar()) {
		case ' ':
			isShoot = true;
			break;

		case 'd':
			isRight = true;
			break;

		case 'a':
			isLeft = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			isShoot = false;
			break;
			
		case KeyEvent.VK_RIGHT:
			isRight = false;
			break;

		case KeyEvent.VK_LEFT:
			isLeft = false;
			break;
		}

		switch (e.getKeyChar()) {
		case ' ':
			isShoot = false;
			break;

		case 'd':
			isRight = false;
			break;

		case 'a':
			isLeft = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {

		case 'q':
			System.exit(1);
			break;

		case 'p':
			isPause = !isPause;
			isHelp = false;
			break;

		case 'r':
			reset();
			break;

		case 'l':
			holdALives = alienLives;
			lvl++;
			createLevel();
			break;
			
		case 'c':
			isComp = !isComp;
			break;
			
		case 'h':
			isHelp = !isHelp;
			break;

		case 'm':
			isMLG = !isMLG;
			break;
			
		case '1':
			isCheat  = !isCheat;
			isAimBot = !isAimBot;
			break;
		}
	}
}