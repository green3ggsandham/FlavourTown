package Entity.Items;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Pear extends Item {
	
	private Player player;
	
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames =  {1};
	
	private static final int STATIC = 0;
	
	public Pear(TileMap tm, Player p) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Items/Pear.png"));
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i = 0; i < 10; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
				}
				sprites.add(bi);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = STATIC;
		animation.setFrames(sprites.get(STATIC));
		animation.setDelay(300);
	}
	
	public void updtate() {
		
		
	}

}
