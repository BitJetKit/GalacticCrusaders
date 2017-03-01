import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Spaceship {
	
	Rectangle body;
	Rectangle gunBody;
	Rectangle bullet;
	int bulletSpeed;
	int speed;
	int health;
	
	boolean isShoot;
	boolean isCollision;
	
	public Spaceship() {
		body    = new Rectangle (Panel.screenWidth/2, Panel.screenHeight - 20, 50, 15);
		gunBody = new Rectangle ((body.x + body.width/2) - 12/2, body.y - (25-body.height), 12, 25);
		bullet  = new Rectangle ((int)(gunBody.getCenterX()), (int)(gunBody.getY() - 2), 2, 10);
		bulletSpeed = 2;
		speed       = 2;
		health = 3;
	}
	
	public Spaceship (int bs, int s, int h) {
		body    = new Rectangle (Panel.screenWidth/2, Panel.screenHeight - 20, 50, 15);
		gunBody = new Rectangle ((int)(body.getCenterX() - 6), body.y - (25 - body.height), 12, 25);
		bullet  = new Rectangle ((int)(gunBody.getCenterX() - 1), (int)(gunBody.getY() + 2), 2, 10);
		bulletSpeed = bs;
		speed       = s;
		health = h;
	}
	
	public void draw (Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fill(bullet);
		g.setColor(new Color (Math.min(50 + health * 40, 255), 0, 0));
		g.fill(body);
		g.fill(gunBody);
	}
	
	public void update() {
		if (isShoot) {
			bullet.y -= bulletSpeed;
		}
		
		if (bullet.y + bullet.height < 0 || isCollision) {
			bullet.y = (int)(gunBody.getY() + 2);
			bullet.x = (int)(gunBody.getCenterX() - 1);
			isShoot = false;
		}
	}
	
	public void shoot() {
		isShoot = true;
	}
	
	public void moveShip (int multiplier) {
		body.x    += speed * multiplier;
		gunBody.x += speed * multiplier;
		if (isShoot == false) bullet.x  += speed * multiplier;
	}
}
