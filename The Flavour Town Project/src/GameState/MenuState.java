package GameState;

import TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	
	public Background bg;
	public Background bg1;
	public Background bg2;
	public Background bg3;
	public Background bg4;
	public Background bg5;
	public Background bg6;
	public static int scale = 2;
	
	private int currentChoice = 0;
	private String[] options = { "Start", "Help", "Quit" };
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			bg = new Background("/Backgrounds/MenuBackground.png", 1);
			bg.setVector(-.5*MenuState.scale,  0);
			bg1 = new Background("/Backgrounds/burgermid.png", 1);
			bg1.setVector(1*MenuState.scale,  10);
			bg2 = new Background("/Backgrounds/burgermid.png", 1);
			bg2.setVector(.25*MenuState.scale,  80*MenuState.scale);
			bg3 = new Background("/Backgrounds/burgermid.png", 1);
			bg3.setVector(-.25*MenuState.scale,  25*MenuState.scale);
			bg4 = new Background("/Backgrounds/burgermid.png", 1);
			bg4.setVector(.4*MenuState.scale,  90*MenuState.scale);
			bg5 = new Background("/Backgrounds/burgermid.png", 1);
			bg5.setVector(-1*MenuState.scale,  50*MenuState.scale);
			bg6 = new Background("/Backgrounds/burgermid.png", 1);
			bg6.setVector(.5*MenuState.scale,  35*MenuState.scale);
			
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 24*MenuState.scale);
			font = new Font("Arial", Font.PLAIN, 10*MenuState.scale);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void init() {}
	public void update() {

		bg.update();
		bg1.update();
		bg2.update();
		bg3.update();
		bg4.update();
		bg5.update();
		bg6.update();
		
	}
	public void draw(Graphics2D g) {
		bg.draw(g);
		bg1.draw(g);
		bg2.draw(g);
		bg3.draw(g);
		bg4.draw(g);
		bg5.draw(g);
		bg6.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Flavour Town", 48*MenuState.scale, 70*MenuState.scale);
		g.setFont(font);
		for(int i = 0; i < options.length ; i++) {
			if(i == currentChoice) {
				g.setColor(Color.YELLOW);
			}
			else {
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 120*MenuState.scale, 110*MenuState.scale + i *15*MenuState.scale);
		}
	}
	private void select() {
		if(currentChoice == 0) {
			gsm.setSTate(GameStateManager.LEVEL1STATE);
		}
		if (currentChoice == 1) {
			
		}
		if (currentChoice == 2) {
			System.exit(0);
		}
	}
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
}
