import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Spaceship {
	
	Rectangle body;
	Rectangle gunBody;
	int bulletSpeed;
	int speed;
	int health;
	
	public Spaceship() {
		body    = new Rectangle (Panel.screenWidth/2, Panel.screenHeight - 20, 50, 15);
		gunBody = new Rectangle ((body.x + body.width/2) - 12/2, body.y - (25-body.height), 12, 25);
		bulletSpeed = 2;
		speed       = 2;
		health = 1;
	}
	
	public Spaceship (int bs, int s, int h) {
		body    = new Rectangle (Panel.screenWidth/2, Panel.screenHeight - 20, 50, 15);
		gunBody = new Rectangle ((int)(body.getCenterX() - 6), body.y - (25 - body.height), 12, 25);
		bulletSpeed = bs;
		speed       = s;
		health = h;
	}
	
	public void draw (Graphics2D g) {
		g.setColor(Color.RED);
		g.fill(body);
		g.fill(gunBody);
		//g.drawRect(xPos, yPos, gunWidth, gunHeight);
	}
	
	public void update () {
		
	}
	
	public void moveShip (int multiplier) {
			body.x    += speed * multiplier;
			gunBody.x += speed * multiplier;
	}
}
