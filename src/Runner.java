import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
	
public class Runner {

		public static void main(String[] args) {
		    JFrame f = new JFrame("Galactic Crusaders"); 
		    Panel p = new Panel();
		    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		    f.setLocation(dim.width/2 - Panel.screenWidth/2, dim.height/2 - Panel.screenHeight/2);
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    f.add(p);
		    f.pack();
		    f.setVisible(true);
		    p.setFocusable(true);
		    p.requestFocusInWindow();
		    p.run();
	}
}
