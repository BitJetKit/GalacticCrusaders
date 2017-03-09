import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Alien {
	static BufferedImage alienImg, missile;
	
	Rectangle body;
	Rectangle shield;
	Rectangle bullet;
	//Bullet bullet;
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
		shield = new Rectangle (body.x-1, body.y-1, 27, 27);
	//	bullet = new Bullet ((int)(body.getCenterX()-2), (int)(body.getCenterY()-6), 4, 10, 2);
		speed = 1;
		health = 1;
	}

	public Alien (int x, int y, int bs, int s, int he) {
		try {
			alienImg = ImageIO.read(new File("Alien.png"));
		//	missile  = ImageIO.read(new File("Missile.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		body   = new Rectangle (x, y, 25, 25);
		shield = new Rectangle (body.x-1, body.y-1, 27, 27);
		//bullet = new Bullet ((int)(body.getCenterX()-2), (int)(body.getCenterY()-6), 4, 10, bs);
		bullet = new Rectangle ((int)(body.getCenterX()-2), (int)(body.getCenterY()-6), 4, 10);
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
		shield = new Rectangle (body.x-1, body.y-1, 27, 27);
		//bullet = new Bullet ((int)(body.getCenterX()-2), (int)(body.getCenterY()-6), 4, 10, bs);
		speed = s;
		health = he;
	}
	
	public void draw (Graphics2D g) {
		//System.out.println(bullet.getWidth());
		g.setColor(Color.GREEN);
		//g.drawImage(missile, bullet.x, bullet.y, bullet.width + 20, bullet.height + 20, null);
		g.fill(bullet);
		//bullet.draw(g);
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
			move(1);
			if (shield.x + shield.width >= Panel.screenWidth) {
				isLeft = true;
				body.x   = (Panel.screenWidth - (body.x - shield.x)) - body.width;
				shield.x = Panel.screenWidth - shield.width;
			}
		}
		
		if (isLeft) {
			move(-1);
			if (shield.x <= 0) {
				counter++;
				isLeft = false;
				body.x   = body.x - shield.x;
				shield.x = 0;
			}
		}
		
		if (bullet.getY() > Panel.screenHeight || isCollision) {
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
		//bullet.fire();
	}
	
	public void resetBullet() {
		bullet.x = (int)(body.getCenterX()-2);
		bullet.y = (int)(body.getCenterY()-6);
		//bullet = new Bullet ((int)(body.getCenterX()-2), (int)(body.getCenterY()-6), 4, 10, 8);
		isShoot = false;
	}
	
	public void move(int multiplier) {
		body.x   += speed * multiplier;
		shield.x += speed * multiplier;
		if (isShoot == false) bullet.x += speed * multiplier;
	}
}
