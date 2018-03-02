package Entity;

import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {
		
	//player hud stats
	private int health;
	private int maxHealth;
	private int stamina;
	private int maxStamina;
	private int fire;
	private int maxFire;
	private boolean dead;
	
	//flinch variables
	private boolean flinching;
	private long flinchTimer;
	
	//fire attack variables
	private boolean allowFire = true;
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private int maxFireBallDamage;
	private ArrayList<FireBall> fireBalls;
	private boolean startFire;
	
	//scratch attack variables
	private boolean scratching;
	private int scratchDamage;
	private int maxScratchDamage;
	private int scratchRange;
	
	//other player moves
	private boolean gliding;
	private boolean sprinting;
	private boolean blocking;
	private boolean pounding;
	private boolean walk;
	
	//initialize animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 2, 5, 1, 1, 14, 7, 13, 2, 1, 1, 1 };
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int SCRATCHING = 5;
	private static final int FIREBALL = 6;
	private static final int STARTWALKING = 7;
	private static final int DUCKING = 8;
	private static final int BLOCKING = 9;
	private static final int DEAD = 10;

	
	//initialize specifics on player gameplay stats
	public Player(TileMap tm) {
		
		super(tm);
		
	//	width = 66;
	//	height = 66;	
	//	cwidth = 55;
	//	cheight = 55;
		
		
		//test for guy fieri
		width = 80;
		height = 80;
		
		moveSpeed = 0.35;
		sprintSpeed = 1.0;
		maxSprint = 4.0;
		maxSpeed = 1.6;
		stopSpeed = 0.3;
		fallSpeed = 0.15;
		poundSpeed = 10;
		maxFallSpeed = 4.0;
		maxPoundSpeed = 20;
		jumpStart = -3.8;
		stopJumpSpeed = 0.3;
		
		
		health = maxHealth = 10;
		stamina = maxStamina = 1500;
		fire = maxFire = 1800;
		fireCost = 600;
		fireBallDamage = maxFireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		scratchDamage = maxScratchDamage = 3;
		scratchRange = 55;
		
		facingRight = true;
		
		//load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/GuyFieriSprite.png"));
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i = 0; i < 11; i++) {
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
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(800);
	}

	//get values for hud update
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	public int getStamina() { return stamina; }
	public int getMaxStamina() { return maxStamina; }
	
	//get attack damage / attacks
	public int getScratchDamage() { return scratchDamage; }
	public boolean getScratching() { return scratching; }
	public int getFireBallDamage() { return fireBallDamage; }	
	public boolean getFiring() { return firing; } 
	
	//setting player movement
	public void setFiring() { firing = true; }
	public void setScratching() { scratching = true; }
	public void setSprinting(boolean b) { sprinting = b; }
	public void setWalk(boolean b) {walk = b;}
	public void setBlocking(boolean b) { blocking = b; }
	public void setGliding(boolean b) {
		gliding = b;
	}
	public void setPounding(boolean b) {
		if(falling && b == true) {
			pounding = b;
		}
		else pounding = false;
	}
	public void setDoubleJump() {
		if(jumpCount == 1 && allowJump == 1) {
			doubleJump = true;
			jumpCount = 0;
			allowJump = 0;
		}
		else {
			jumpCount = 1;
			doubleJump = false;
			if(dy == 0) {
				allowJump = 1;
			}
		}
	}

	//check attack
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy ee = enemies.get(i);
			
			//scratch attack
			if(scratching) {
				if(facingRight) {
					if(
						ee.getx() > x && 
						ee.getx() < x + scratchRange && 
						ee.gety() > y - height / 2 && 
					    ee.gety() < y + height / 2) 
					{
						ee.hit(scratchDamage);
					}
				}
				else {
					if(
						ee.getx() < x && 
						ee.getx() > x - scratchRange &&
						ee.gety() > y - height / 2 &&
						ee.gety() < y + height /2) 
					{
						ee.hit(scratchDamage);
					}
				}
			}
		
			//fireball
			for(int j = 0; j < fireBalls.size(); j ++) {
				if(fireBalls.get(j).intersects(ee)) {
					ee.hit(fireBallDamage);
					fireBalls.get(j).setHit();
				}
			}
		}
	}
	public void checkEnemyAttack(ArrayList<Enemy> enemies) {
			for(int i = 0; i < enemies.size(); i++) {
				Enemy ee = enemies.get(i);
				if(
					ee.getx() > x - ee.getWidth() && 
					ee.getx() < x + ee.getWidth() &&
					ee.gety() > y - height / 2 && 
					ee.gety() < y + height / 2)
					{
						hit(ee.getDamage());
					}
			}
	}
	public void checkEnemyTactic(ArrayList<Enemy> enemies) {
		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy ee = enemies.get(i);
			//if(jumping) {
				//ee.tacticMoving(true);
			//}
			//if(down) {
				//ee.tacticMoving(false);
			//}
			if(jumping) {
				ee.tacticMoving(false);
			}
			else {
				ee.tacticMoving(true);
			}
		}
	}
	

	//check if hit
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	//pretty obvious what this does
	private void getNextPosition() {
		
		//movement
		if(left && !dead) {
			if(sprinting && !falling) {
				dx -= sprintSpeed;
				if(dx < -maxSprint) {
					dx = -maxSprint;
				}
			}
			else {
				dx -= moveSpeed;
				if(dx < -maxSpeed) {
					dx = -maxSpeed;
				}
			}
		}
		else if(right && !dead) {
			if(sprinting && !falling) {
				dx += sprintSpeed;
				if(dx > maxSprint) {
					dx = maxSprint;
				}
			}
			else {
				dx += moveSpeed;
				if(dx > maxSpeed) {
					dx = maxSpeed;
				}
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot attack while moving
		if((currentAction == DUCKING ||
			currentAction == SCRATCHING || 
			currentAction == FIREBALL || 
			currentAction == BLOCKING && 
			!(jumping || falling))) {
			
			dx = 0;
		}
		
		//double jump
		if(doubleJump ) {
			dy = jumpStart*1.4;
			falling = true;
			doubleJump = false;
		}
		//jumping
		else if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}
		
		//pounding
		if(pounding) {
			if(dy > 0 && gliding) dy += poundSpeed ;
			else dy += poundSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxPoundSpeed) dy = maxPoundSpeed;
		}
		//falling
		if(falling) {
			if(dy > 0 && gliding) dy += fallSpeed * 0.075;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
		if(dead) {
			left = false;
			right = false;
		}
		
	}
	
	//update all character movements...
	public void update() {
		
		//update position
		getNextPosition();
		checkTileMapCollision();
	 	setPosition(xtemp, ytemp);
		
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
			
		}
		
		// check attack has stopped
		if(currentAction == SCRATCHING && animation.hasPlayedOnce()) {
			scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(allowFire) {
				startFire = true;
				allowFire = false;
			}
			if(animation.hasPlayedOnce()) {
				firing = false;
				allowFire = true;
			}
		}
		
		//player stamina
		
		//scale attack damage on stamina
		if(stamina <= 0){
			scratchDamage -= 2;
			fireBallDamage -= 3;
			stamina = 0;
			if(scratchDamage <= 0) {
				scratchDamage = 0;
			}
			if(fireBallDamage <= 0) {
				fireBallDamage = 0;
			}
		}
		if(stamina < maxStamina / 5) {
			stamina += 1;
		}
		else {
			stamina += 3;
		}
		if(stamina > maxStamina) {
			stamina = maxStamina;
			fireBallDamage = maxFireBallDamage;
			scratchDamage = maxScratchDamage;
		}
		if(stamina == maxStamina / 4 && fireBallDamage < maxFireBallDamage) {
			fireBallDamage += 3;
			scratchDamage += 2;
			if(fireBallDamage > maxFireBallDamage) {
				fireBallDamage = maxFireBallDamage;
			}
		}
		if(stamina == maxStamina / 2 && fireBallDamage < maxFireBallDamage) {
			fireBallDamage += 3;
			scratchDamage += 2;
			if(fireBallDamage > maxFireBallDamage) {
				fireBallDamage = maxFireBallDamage;
			}
		}
		
		
		//fireball attack!
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(startFire) {
			if(fire > fireCost && startFire) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
			startFire = false;
		}
		
		//update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		//set animations
		if(scratching) {
			if(currentAction != SCRATCHING) {
				stamina -= 400;
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(100);
			}
		}
		else if(blocking) {
			if(currentAction != BLOCKING) {
				currentAction = BLOCKING;
				animation.setFrames(sprites.get(BLOCKING));
				animation.setDelay(100);
			}
		}
		else if (firing) {
			if(currentAction != FIREBALL) {
				stamina -= 500;
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(90);
				cheight = 56;
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(130);
					cheight = 54;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				cwidth = 45;
				cheight = 54;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				cwidth = 45;
				cheight = 50;
				
			}
		}
		else if((left || right) && sprinting && !walk) {
			
				if(currentAction != STARTWALKING ) {
					currentAction = STARTWALKING;
					animation.setFrames(sprites.get(STARTWALKING));
					animation.setDelay(90);
					cheight = 54;
					cwidth = 40;
				}
				if(animation.hasPlayedOnce()) {
					currentAction = WALKING;
					animation.setFrames(sprites.get(WALKING));
					walk = true;
				}
		}
			
		else if((left || right) && !sprinting && !walk) {
			
				if(currentAction != STARTWALKING) {
					currentAction = STARTWALKING;
					animation.setFrames(sprites.get(STARTWALKING));
					animation.setDelay(150);
					cheight = 54;
					cwidth = 40;
				}
				if(animation.hasPlayedOnce()) {
					currentAction = WALKING;
					animation.setFrames(sprites.get(WALKING));
					walk = true;
				}
		}
		else if(walk) {}
		
		else if(down){
			
			if(currentAction != DUCKING ) {
				currentAction = DUCKING;
				animation.setFrames(sprites.get(DUCKING));
				animation.setDelay(150);
				cheight = 54;
				cwidth = 40;
			}
		}
		
		else if (dead) {
			if(currentAction != DEAD) {
				currentAction = DEAD;
				animation.setFrames(sprites.get(DEAD));
				animation.setDelay(100);
				cheight = 54;
				cwidth = 45;
			}
		}
		
		
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(800);
				cwidth = 45;
				cheight = 54;
			}
		}
		
		//set direction
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left)  facingRight = false;
		}
	
	
		
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		//draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		//draw player
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
			
		super.draw(g);
		
		
	}
	
}
