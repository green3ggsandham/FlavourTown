package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Inventory {


	private BufferedImage image;
	private boolean inventory;
	
	public Inventory() {
		try {
		image = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/hud.gif"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setInventory() {
		if(!inventory) {
			inventory = true;
		}
		else {
			inventory = false;
		}
	}
	public void draw(Graphics2D g) {
		
		if(inventory) {
			g.drawImage(image, 200, 200 , null);
		}
		
	}
	
	
}
