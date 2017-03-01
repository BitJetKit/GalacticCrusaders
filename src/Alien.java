import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Alien {
	static BufferedImage alienImg, alienShip;
	
	Rectangle body;
	Rectangle shield;
	Rectangle bullet;
	int bulletSpeed;
	int speed;
	int health;
	
	int counter;
	
	boolean isDead;
	boolean isLeft;
	boolean isShoot;
	boolean isCollision;
	
	public Alien() {
		try {
			alienImg = ImageIO.read(new File("Alien.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		body   = new Rectangle (0, 50, 25, 25);
		shield = new Rectangle (body.x-2, body.y-2, 27, 27);
		bullet = new Rectangle ((int)(body.getCenterX()-1), (int)(body.getY()), 3, 10);
		bulletSpeed = 2;
		speed = 1;
		health = 1;
	}

	public Alien (int x, int y, int bs, int s, int he) {
		try {
			alienImg = ImageIO.read(new File("Alien.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		body   = new Rectangle (x, y, 25, 25);
		shield = new Rectangle (body.x-2, body.y-2, 27, 27);
		bullet = new Rectangle ((int)(body.getCenterX()-1), (int)(body.getY()), 3, 10);
		bulletSpeed = bs;
		speed = s;
		health = he;
	}
	
	public Alien (int x, int y, int w, int h, int bs, int s, int he) {
		try {
			alienImg = ImageIO.read(new File("Alien.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		body   = new Rectangle(x, y, w, h);
		shield = new Rectangle (body.x-2, body.y-2, 27, 27);
		bullet = new Rectangle ((int)(body.getCenterX()-1), (int)(body.getY()), 3, 10);
		bulletSpeed = bs;
		speed = s;
		health = he;
	}
	
	public void draw (Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fill(bullet);
		g.setColor(new Color (0, Math.max(255 - health * 30, 75), 0));
		//g.fill(body);
		if (health != 1) {
			if (health == 2) {
				g.setColor(new Color (179, 58, 58));
			} else if (health == 3) {
				g.setColor(new Color (228, 235, 23));
			}
			g.draw(shield);
		}
		g.drawImage(alienImg, body.x, body.y, body.width, body.height, null);
	}
	
	public void update() {
		
		if (isShoot) {
			shoot();
		}
		
		if (!isLeft) {
			moveAlien(1);
			if (body.x >= Panel.screenWidth - body.width) {
				isLeft = true;
			}
		}
		
		if (isLeft) {
			moveAlien(-1);
			if (body.x <= 0) {
				counter++;
				isLeft = false;
			}
		}
		
		if (bullet.y > Panel.screenHeight) {
			resetBullet();
		}
		
		if (isCollision) {
			resetBullet();
		}
		
		if (counter == 1) {
			bullet.y += 30;
			body.y   += 30;
			shield.y += 30;
			counter = 0;
		}
	}
	
	public void shoot() {
		bullet.y += bulletSpeed;
	}
	
	public void resetBullet() {
		bullet.y = (int)(body.getY());
		bullet.x = (int)(body.getCenterX());
		isShoot = false;
	}
	
	public void moveAlien(int multiplier) {
		body.x   += speed * multiplier;
		shield.x += speed * multiplier;
		if (isShoot == false) bullet.x += speed * multiplier;
	}
}
