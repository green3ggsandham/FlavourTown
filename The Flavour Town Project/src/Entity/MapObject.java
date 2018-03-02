package Entity;

import TileMap.TileMap;
import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.Tile;

public abstract class MapObject {
	
	//tile
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//dimensions
	protected int width;
	protected int height;
	
	//collision box
	protected int cwidth;
	protected int cheight;
	
	// collision
	protected double collisionHeight;
	protected double collisionWidth;
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected int topLeft;
	protected int topRight;
	protected int bottomLeft;
	protected int bottomRight;
	
	//animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	//movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	protected boolean doubleJump;
	protected int jumpCount;
	protected int allowJump;
	
	//movement attributes
	protected double moveSpeed;
	protected double sprintSpeed;
	protected double maxSpeed;
	protected double maxSprint;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double poundSpeed;
	protected double maxPoundSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	//constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		//tileSize = tm.getTileSize();
		tileSize = 30;
	}
	
	//all the game physics...
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);	
	}
	public Rectangle getRectangle() {
		return new Rectangle ((int)x - cwidth, (int)y - cheight, cwidth, cheight);
	}
	
	//public Triangle getTriangle() {
		//return new Triangle (())
	//}
	public void calculateCorners(double x, double y) {
		
		
		//regular tile
		int leftTile = (int)(x - cwidth / 2) / tileSize ;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize ;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize ;
		
		//bottom half tile
		int leftHalfTile = (int)(x - cwidth / 2) / tileSize;
		int rightHalfTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topHalfTile = (int)(y - cheight / 4) / tileSize;
		int bottomHalfTile = (int)(y + cheight / 4 - 1) / tileSize;
		
		//
		//add more tile types...
		
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
	
			if( bl == Tile.BLOCKED) {
				bottomLeft = tileMap.getType(bottomTile, leftTile);
			}
			else if ( bl == Tile.HALFBLOCK) {
				bottomLeft = tileMap.getType(bottomHalfTile, leftTile);
			}
			else {
				bottomLeft = 0;
			}
			if (br == Tile.BLOCKED) {
				bottomRight = tileMap.getType(bottomTile, rightTile);
			}
			else if ( br == Tile.HALFBLOCK) {
				bottomRight = tileMap.getType(bottomHalfTile, rightTile);
			}
			else {
				bottomRight = 0;
			}
			if(tr == Tile.BLOCKED) {
				topRight = tileMap.getType(topTile, rightTile);
			}
			else if ( tr == Tile.HALFBLOCK) {
				topRight = tileMap.getType(topHalfTile, rightTile);
			}
			else {
				topRight = 0;
			}
			if(tl == Tile.BLOCKED) {
				topLeft = tileMap.getType(topTile, leftTile);
			}
			else if ( tl == Tile.HALFBLOCK) {
				topLeft = tileMap.getType(topHalfTile, leftTile);
			}
			else {
				topLeft = 0;
			}
	}
	public void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		xdest = x + dx;
		ydest = y + dy;
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft == 1 || topRight == 1) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else if(topLeft == 2 || topRight == 2) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 4;
			}
			else {
				ytemp += dy;
			}
		}
		if (dy > 0) {
			if(bottomLeft == 1 || bottomRight == 1) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else if(bottomLeft == 2 || bottomRight == 2) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 4;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft == 1 || bottomLeft == 1 || topLeft == 2 || bottomLeft == 2) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight == 1 || bottomRight == 1 || topRight == 2 || bottomRight == 2) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(bottomLeft == 0 && bottomRight == 0) {
				falling = true;
			}
		}
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dy = dy;
		this.dx = dx;
	}
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	public void setLeft(boolean b) {left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b;}
	public boolean notOnScreen() {
		return x + xmap + width < 0 
			|| x + xmap - width > GamePanel.WIDTH
			|| y + ymap + height < 0
			|| y + ymap - height > GamePanel.HEIGHT;
	}
	public void draw(java.awt.Graphics2D g) {
		
		if(facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), width, height, null);
			}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
	}
}
