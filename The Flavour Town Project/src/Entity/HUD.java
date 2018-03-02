package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD {

	private Player player;
	private BufferedImage image;
	private Font font;
	
	

	
	public HUD(Player p) {
		player = p;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/hud.gif"));
			font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void draw(Graphics2D g) {
	
		g.drawImage(image, 0, 30, null);
		g.setFont(font);
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 45);
		g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100 , 30, 65);
		g.drawString(player.getStamina() / 100 + "/" + player.getMaxStamina() / 100, 30, 85);
	}
	
}
