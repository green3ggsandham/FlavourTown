package Entity;

import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;

public class FireBall extends MapObject {

	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public FireBall(TileMap tm, boolean right) {
		
		super(tm);
		
		moveSpeed = 4;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cwidth = 5;
		cheight = 5;
		
		//load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/CompleteFireBall.png"));
			
			sprites = new BufferedImage[6];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
			hitSprites = new BufferedImage[2];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
			}
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(75);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setHit() {
		if(animation.hasPlayedOnce()) { remove = true; }
		if(hit) return;
		hit = true;
		remove = false;
		animation.setFrames(hitSprites);
		animation.setDelay(60);
		if(dx < 0) {
			dx = -2.5;
		}
		else if(dx > 0) {
			dx = 2.5;
		}
		
	}
	public boolean shouldRemove() { return remove;}
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) {
			setHit();
		}
		animation.update();
		if(hit || animation.hasPlayedOnce()) {
			setHit();
		}
	}
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
	}

}
