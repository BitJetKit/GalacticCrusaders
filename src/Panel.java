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
	int alienLives = 1;
	int holdALives = alienLives;
	int bulletFrequency = 50;

	int bulletSpeed = 4;
	int lives = 3;
	int holdLives = lives;

	int lvl = 1;
	int score = 0;
	int tempScore = score;

	boolean isPause;
	boolean isLeft, isRight, isShoot;
	boolean isGameEnd;
	boolean isComp = false;
	boolean isCollision;
	boolean isCheat, isAimBot;

	ArrayList<Alien> aliens = new ArrayList<Alien>();
	Spaceship p1, comp;

	String font = "Atari Font Full Version";

	File hit = new File("MLG\\HitmarkerSound.wav");
	boolean isMLG;

	Timer timer;

	public Panel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK); // TODO Add sound
		addKeyListener(this);

		try {
			img  = ImageIO.read(new File("Starfield.png"));
			img2 = img;
			crosshair = ImageIO.read(new File("Crosshair.png"));
			hitmarker = ImageIO.read(new File("MLG\\Hitmarker.png"));
			MLGlogo   = ImageIO.read(new File("MLG\\MLGLogo.png"));
		} catch (IOException e) {
			System.out.println(e);
		}

		p1 = new Spaceship(bulletSpeed, 4, lives); // TODO Create computer
		comp = new Spaceship(bulletSpeed, 4, lives); // player
		createLevel();
		timer = new Timer();
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
		
		if (isPause && !isGameEnd) {
			Color background = new Color(0, 0, 0, 200);
			g.setColor(background);
			g.fillRect(0, 0, screenWidth, screenHeight);
			g.setColor(Color.BLUE);
			drawCenteredString(g, "Press p to play", new Rectangle(0, 0, screenWidth, 3 * screenHeight / 4),
					new Font(font, Font.PLAIN, 28));
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

		if (isMLG) {
			g.drawImage(MLGlogo, Panel.screenWidth / 2 - 50, 0, 100, 50, null);
			if (isCollision) {
				drawHit(g);
				// timer.schedule(new TimerTask() {
				// @Override
				// public void run() {
				// g.drawImage(hitmarker, Panel.screenWidth/2,
				// Panel.screenHeight/2, 100, 100, null);
				// System.out.println("Hit");
				// try {
				// playClip(hit);
				// } catch (IOException e) {
				// e.printStackTrace();
				// } catch (UnsupportedAudioFileException e) {
				// e.printStackTrace();
				// } catch (LineUnavailableException e) {
				// e.printStackTrace();
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// }
				// }, 0, 1000);

				isCollision = false;
			}
		}
	}

	public void drawHit(Graphics g) {
		try {
			playClip(hit);
			System.out.println("Hit");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		g.drawImage(hitmarker, Panel.screenWidth / 2, Panel.screenHeight / 2, 100, 100, null);
		// delay(1000);
	}

	public void run() {
		while (true) {
			if (!isPause) {
				imgY += 4;
				if (imgY >= screenHeight)
					imgY = 0;

				if (isComp) {
					playComp();
				} else {

					for (Alien a : aliens) {
						if (isAimBot) {
							if (a.isLeft) {
								if ((p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
										/ p1.bulletSpeed <= (a.body.getX() - p1.body.getX()) / a.speed + 4
										&& (p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
												/ p1.bulletSpeed >= (a.body.getX() - p1.body.getX()) / a.speed - 4) {
									p1.shoot();
								}
							} else {
								if ((p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
										/ p1.bulletSpeed <= (p1.body.getX() - a.body.getX()) / a.speed + 4
										&& (p1.bullet.getY() - (a.body.getY() + a.body.getHeight()))
												/ p1.bulletSpeed >= (p1.body.getX() - a.body.getX()) / a.speed - 4) {
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
							isCollision = true;
						}

						if (p1.bullet.intersects(a.body)) { // TODO Fix
															// disappearing
															// bullet bug
							if (isMLG) {

							}
							// drawHit();
							p1.isCollision = true;
							isCollision = true;
							repaint();
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
						p1 = new Spaceship(bulletSpeed, 4, lives);
						holdALives = alienLives;
						lvl++;
						createLevel();
					}

					if (isRight && p1.body.getX() + p1.body.getWidth() < screenWidth)
						p1.moveShip(1);
					if (isLeft && p1.body.getX() > 0)
						p1.moveShip(-1);
					if (isShoot)
						p1.shoot();
				}

				for (int i = 0; i < aliens.size(); i++) {
					if (aliens.get(i).isDead) {
						aliens.remove(i);
					}
				}

				tempScore = score;

			}
			repaint();
			delay(1000 / frameRate);
		}
	}

	public void playComp() {
		for (Alien a : aliens) {
			if (a.isLeft) {
				if ((comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
						/ comp.bulletSpeed <= (a.body.getX() - comp.body.getX()) / a.speed + 4
						&& (comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
								/ comp.bulletSpeed >= (a.body.getX() - comp.body.getX()) / a.speed - 4) {
					comp.shoot();
				}
			} else {
				if ((comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
						/ comp.bulletSpeed <= (comp.body.getX() - a.body.getX()) / a.speed + 4
						&& (comp.bullet.getY() - (a.body.getY() + a.body.getHeight()))
								/ comp.bulletSpeed >= (comp.body.getX() - a.body.getX()) / a.speed - 4) {
					comp.shoot();
				}
			}

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
				isCollision = true;
			}

			if (comp.bullet.intersects(a.body)) { // TODO Fix
													// disappearing
													// bullet bug
				if (isMLG) {

				}
				// drawHit();
				comp.isCollision = true;
				isCollision = true;
				repaint();
				score += 100 / alienLives;
				a.health--;
			}

			if (a.health == 0) { // TODO Add kill animation
				a.isDead = true;
			}

			a.update();
			a.isCollision = false;

		}

		comp.update();
		comp.isCollision = false;

		if (aliens.isEmpty()) {
			tempScore = score;
			comp = new Spaceship(bulletSpeed, 4, lives);
			holdALives = alienLives;
			lvl++;
			createLevel();
		}
	}

	public void createLevel() { // TODO Balance levels?
		aliens.clear();
		score = tempScore;
		isPause = true;

		if ((lvl - 3) % 5 == 0) { // Every 3rd level starting from 5s
			numOfAliens = Math.min(23, lvl);
			alienLives = holdALives + 1;
			// System.out.println("Level: " + lvl + ", " + numOfAliens);
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (25 + 20), 50, 8, 3, alienLives));
			}
		} else if (lvl % 5 == 0) { // Every 5th level
			numOfAliens = Math.min(20, (lvl * 4) / 5);
			alienLives = holdALives + 1;
			// System.out.println("Level: " + lvl + ", " + numOfAliens);
			bulletFrequency = 40;
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (25), 50, 10, 4, alienLives));
			}
		} else { // Every 1st, 2nd, and 4th level starting from 5s
			numOfAliens = Math.min(28, lvl * 2);
			if ((lvl - 4) % 5 == 0)
				alienLives = holdALives - 1;
			// System.out.println("Level: " + lvl + ", " + numOfAliens);
			for (int i = 0; i < numOfAliens; i++) {
				aliens.add(new Alien(i * (25 + 20), 50, 8, 3, alienLives));
			}
		}
	}

	public void reset() {
		lives = holdLives;
		p1 = new Spaceship(bulletSpeed, 4, lives);
		isGameEnd = false;
		score = 0;
		tempScore = 0;
		createLevel();
	}

	private static void playClip(File clipFile)
			throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
		class AudioListener implements LineListener {
			private boolean done = false;

			@Override
			public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
				if (eventType == Type.STOP || eventType == Type.CLOSE) {
					done = true;
					notifyAll();
				}
			}

			public synchronized void waitUntilDone() throws InterruptedException {
				while (!done) {
					wait();
				}
			}
		}
		AudioListener listener = new AudioListener();
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
		try {
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			try {
				clip.start();
				listener.waitUntilDone();
			} finally {
				clip.close();
			}
		} finally {
			audioInputStream.close();
		}
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
			isShoot = true;
			break;

		case KeyEvent.VK_RIGHT:
			isRight = true;
			break;

		case KeyEvent.VK_LEFT:
			isLeft = true;
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
			break;

		case 'r':
			reset();
			break;

		case 's':
			holdALives = alienLives;
			lvl++;
			createLevel();
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