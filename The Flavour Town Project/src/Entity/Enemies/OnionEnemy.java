package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class OnionEnemy extends Enemy {
	
	
	private Player player;
	private Font font;
	
	
	//initialize animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {8, 3};
	
	//animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	
	public OnionEnemy(TileMap tm, Player p) {
		
		super(tm);
		player = p;
		
		//opposite.. forgot to mirror picture
		facingRight = false;
		right = true;
		
		moveSpeed = 0.6;
		maxSpeed = 0.6;
		fallSpeed = 0.5;
		jumpStart = -2.8;
		stopJumpSpeed = 0.8 ;
		maxFallSpeed = 4.0;
		
		width = 25;
		height = 27;
		cwidth = 23;
		cheight = 25;
		
		health = maxHealth = 10;
		damage = 1;
		
		try { font = new Font("Arial", Font.PLAIN, 15); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/OnionEnemy.png"));
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
		currentAction = WALKING;
		animation.setFrames(sprites.get(WALKING));
		animation.setDelay(300);
		
		
	}

	private void getNextPosition() {
		
		//movement
		if(!moving) {
			dx= 0;
			left = false;
			right = false;
		}
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		//falling
		if(falling) {
			dy += fallSpeed;
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(jumping) {
			dy = jumpStart;
			falling = true;
		}
		
		
	}
	
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	public void update() {
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check flinch
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		if(!moving) {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(200);
				dx= 0;
				left = false;
				right = false;
			}
		}
		
		else {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(200);
				dx= maxSpeed;
				right = true;
				facingRight = false;
			}
		}
		
		//if hits wall turns around
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = true;
			
		}
		else if(left && dx ==0) {
			right = true;
			left = false;
			facingRight = false;
		}
		
		animation.update();
	}
	public void draw(Graphics2D g) {
		
		setMapPosition();
		g.setFont(font);
		
		if(flinching) {
			if(player.getScratching()) {
				g.drawString("" + player.getScratchDamage(), (int)(x + xmap - width / 2 + 10), (int)(y + ymap - height / 2 - 5) );
			}
			else if(player.getFiring()) {
				g.drawString("" + player.getFireBallDamage(), (int)(x + xmap - width / 2 + 10), (int)(y + ymap - height / 2 - 5) );
			}
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		} 
		super.draw(g);
		
	}	
}
