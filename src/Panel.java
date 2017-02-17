import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel implements KeyListener {
	static BufferedImage img;
	static BufferedImage img2;
	static int imgY;
	
	static int screenWidth = 1280;
	static int screenHeight = 720;
	int frameRate = 200;

	boolean isPause = false;

	public Panel() {
		imgY = 0;
		try {
			img = ImageIO.read(new File("Starfield.png"));
			img2 = img;
		} catch (IOException e) {
			System.out.println(e);
		}
		
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK);
		addKeyListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, imgY, screenWidth, screenHeight, null);
		g.drawImage(img2, 0, imgY - screenHeight, screenWidth, screenHeight, null);
	}

	public void run() {

		while (!isPause) {
			imgY++;
			if (imgY >= screenHeight) {
				imgY = 0;
			}
			repaint();
			delay(1000 / frameRate);
		}
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
		if (e.getKeyChar() == 'w') {
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'p') {

		}
	}
}
