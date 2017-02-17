import java.awt.Rectangle;


public class Alien {

	int xPos;
	int yPos;
	Rectangle body;
	int bulletSpeed;
	int speed;
	int health;
	
	boolean isDead;
	
	public Alien() {
		xPos = 0;
		yPos = 50;
		bulletSpeed = 2;
		speed = 2;
		health = 1;
	}

	public Alien (int x, int y, int bs, int s, int he) {
		xPos = x;
		yPos = y;
		bulletSpeed = bs;
		speed = s;
		health = he;
	}
	
	public Alien (int x, int y, int w, int h, int bs, int s, int he) {
		body = new Rectangle(x, y, w, h);
		bulletSpeed = bs;
		speed = s;
		health = he;
	}
}
