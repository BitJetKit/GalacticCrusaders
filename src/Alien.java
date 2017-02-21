import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Alien {

	Rectangle body;
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
		body = new Rectangle (0, 50, 25, 25);
		bullet = new Rectangle ((int)(body.getCenterX()), (int)(body.getY()), 3, 10);
		bulletSpeed = 2;
		speed = 1;
		health = 1;
	}

	public Alien (int x, int y, int bs, int s, int he) {
		body = new Rectangle (x, y, 25, 25);
		bullet = new Rectangle ((int)(body.getCenterX()), (int)(body.getY()), 3, 10);
		bulletSpeed = bs;
		speed = s;
		health = he;
	}
	
	public Alien (int x, int y, int w, int h, int bs, int s, int he) {
		body = new Rectangle(x, y, w, h);
		bullet = new Rectangle ((int)(body.getCenterX()), (int)(body.getY()), 3, 10);
		bulletSpeed = bs;
		speed = s;
		health = he;
	}
	
	public void draw (Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fill(body);
		g.fill(bullet);
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
			bullet.y = (int)(body.getY());
			bullet.x = (int)(body.getCenterX());
			isShoot = false;
		}
		
		if (counter == 1) {
			bullet.y += 30;
			body.y   += 30;
			counter = 0;
		}
	}
	
	public void shoot() {
		bullet.y += bulletSpeed;
	}
	
	public void moveAlien(int multiplier) {
		body.x   += speed * multiplier;
		if (isShoot == false) bullet.x += speed * multiplier;
	}
}
