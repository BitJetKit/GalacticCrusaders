import java.awt.Rectangle;

public class Spaceship {
	
	int xPos;
	int yPos;
	int height;
	int width;
	int gunHeight;
	int gunWidth;
	Rectangle body;
	Rectangle gBody;
	int bulletSpeed;
	int speed;
	int health;
	
	public Spaceship() {
		xPos = Panel.screenWidth/2;
		yPos = 40;
		bulletSpeed = 2;
		speed = 2;
		health = 1;
	}
	
	public Spaceship (int x, int y, int bs, int s, int h) {
		height = 5;
		width = 10;
		gunHeight = 8;
		gunWidth = 6;
		body  = new Rectangle (x, y, width, height);
		gBody = new Rectangle (x/2, y - (gunHeight-height), gunWidth, gunHeight);
		bulletSpeed = bs;
		speed = s;
		health = h;
	}
}
