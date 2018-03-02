package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean moving = true;
	protected boolean jumping;
	protected boolean left;
	protected boolean right;
	protected boolean sprintingToward;
	protected boolean sprintingAway;
	protected boolean blocking;
	protected boolean flinching;
	protected long flinchTimer;
	

	public Enemy(TileMap tm) {
		super(tm);
	}
	public boolean isDead() { return dead; }
	public int getDamage() { return damage; }
	public boolean getFlinching() { return flinching; }
	public void tacticMoving (boolean b) { moving = b; }
	public void tacticLeft (boolean b) { left = b; }
	public void tacticRight (boolean b) { right = b;}
	public void tacticJumping (boolean b) { jumping = b; }
	public void tacticSprintingToward (boolean b) { sprintingToward = b; }
	public void tacticSprintingAway ( boolean b) { sprintingAway = b; }
	public void tacticBlocking ( boolean b) { blocking = b; }
	public void hit (int damage) {
	if(dead || flinching) return;
	health -= damage;
	if(health < 0) health = 0;
	if(health == 0) dead = true;
	flinching = true;
	flinchTimer = System.nanoTime();
}
	public void update() {}
}
