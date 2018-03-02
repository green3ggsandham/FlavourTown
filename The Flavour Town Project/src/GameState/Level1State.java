package GameState;

import TileMap.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import Entity.*;
import Main.GamePanel;
import java.util.ArrayList;
import Entity.Enemies.*;

public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	private Player player;
	private ArrayList<Enemy> enemies;
	private HUD hud;
	private Inventory inventory;
	
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	public void init() {
		
		//add map
		tileMap = new TileMap(30);
		tileMap.loadTiles("/TileSets/TestTileSet.png");
		tileMap.loadMap("/Maps/TestLevel..txt");
		tileMap.setPosition(0, 0);
		
		//add background
		bg = new Background("/Backgrounds/Level1Background.png", 1);
		bg.setVector(1, 0);
		
		//add player
		player = new Player(tileMap);
		player.setPosition(100,150);
		
		//add enemies
		populateEnemies();
		
		//add HUD
		hud = new HUD(player);
		
		//add inventory
		inventory = new Inventory();
		
	}
	
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		OnionEnemy o;
		Point[] points = new Point[] {
				new Point(200, 150),
				new Point(300, 150),
				new Point(400, 150),
		};
	
		for(int i = 0; i < points.length; i++) {
			o = new OnionEnemy(tileMap, player);
			o.setPosition(points[i].x, points[i].y);
			enemies.add(o);
		}
	}
	public void update() {
		
		//update player
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		//update background
		bg.setPosition((GamePanel.WIDTH / 2 - player.getx())/4, 0);
		bg.update();
		
		//attack enemies
		player.checkAttack(enemies);
		player.checkEnemyAttack(enemies);
		//player.checkEnemyTactic(enemies);
		
		//update enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
			if(enemies.get(i).isDead()) {
				enemies.remove(i);
				i--;
			}
		}
	}
	public void draw(Graphics2D g) {
	
		// draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);

		//draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		//draw player
		player.draw(g);
		
		//draw HUD
		hud.draw(g);
		
		//draw inventory
		inventory.draw(g);
		
	}
	
	public void keyPressed (int k) {
		if(k == KeyEvent.VK_A) inventory.setInventory();
		if(k == KeyEvent.VK_LEFT) player.setLeft(true); 
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();
		if(k == KeyEvent.VK_S) player.setSprinting(true);
		if(k == KeyEvent.VK_Q) player.setBlocking(true);
		if(k == KeyEvent.VK_DOWN) {
			player.setPounding(true);
			player.setDown(true);
		}
		if(k == KeyEvent.VK_W) {
			player.setDoubleJump();
			player.setJumping(true);
		}	
	}
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) { 
			player.setWalk(false);
			player.setLeft(false);
		}
		if(k == KeyEvent.VK_RIGHT) {
			player.setWalk(false);
			player.setRight(false);
		}
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_W) {player.setJumping(false);}
		if(k == KeyEvent.VK_E) player.setGliding(false);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();
		if(k == KeyEvent.VK_S) player.setSprinting(false);
		if(k == KeyEvent.VK_Q) player.setBlocking(false);
		if(k == KeyEvent.VK_DOWN) {
			player.setPounding(false);
			player.setDown(false);
		}
	}	
}
