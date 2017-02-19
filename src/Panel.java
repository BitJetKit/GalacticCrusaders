import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel implements KeyListener {
	static BufferedImage img;
	static BufferedImage img2;
	static BufferedImage img3;
	static int imgY;
	
	static int screenWidth = 1280;
	static int screenHeight = 720;
	int frameRate = 200;
	
	int lvl = 0;

	boolean isPause = false;
	boolean isLeft, isRight;
	
	ArrayList<Alien> aliens;
	Spaceship p1;

	public Panel() {
		imgY = 0;
		try {
			img = ImageIO.read(new File("Starfield.png"));
			img2 = img;
			img3 = img;
		} catch (IOException e) {
			System.out.println(e);
		}
		
		p1 = new Spaceship(2, 2, 1);
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK);
		addKeyListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(img, 0, imgY, screenWidth, screenHeight, null);
		g.drawImage(img2, 0, imgY - screenHeight, screenWidth, screenHeight, null);
		g.drawImage(img2, 0, imgY - screenHeight * 2, screenWidth, screenHeight, null);
		
		p1.draw(g2);
	}

	public void run() {

		while (!isPause) {
			imgY += 4;
			if (imgY >= screenHeight) {
				System.out.println(imgY);
				imgY = 0;
			}
			
			p1.update();
			if (isRight) p1.moveShip(1);
			if (isLeft)  p1.moveShip(-1);
			repaint();
			delay(1000 / frameRate);
		}
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
		
	}
}
