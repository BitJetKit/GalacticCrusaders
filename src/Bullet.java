import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Bullet extends Rectangle {
	BufferedImage bulletImg;
	
	Rectangle bullet;
	int bulletSpeed;
	
	public Bullet() {
		bullet = new Rectangle (0, 0, 1, 10);
		bulletSpeed = 1;
	}
	
	public Bullet (int x, int y, int width, int height, int speed) {
		bullet = new Rectangle (x, y, width, height);
		bulletSpeed = speed;
	}
	
	public Bullet (int x, int y, int width, int height, int speed, BufferedImage img) {
		bullet = new Rectangle (x, y, width, height);
		bulletSpeed = speed;
		bulletImg = img;
	}
	
//	public double getX() {
//		return bullet.getX();
//	}
//	
//	public double getY() {
//		return bullet.getY();
//	}
//
//	public double getWidth() {
//		return bullet.getWidth();
//	}
//
//	public double getHeight() {
//		return bullet.getHeight();
//	}
	
	public int getSpeed() {
		return bulletSpeed;
	}
	
	public void draw (Graphics2D g) {
		g.fill(bullet);
		g.drawImage(bulletImg, bullet.x, bullet.y, bullet.width, bullet.height, null);
	}
	
	public void fire() {
		bullet.y += bulletSpeed;
	}
}
