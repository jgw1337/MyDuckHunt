package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Target {
	// How much time passes before creating a new duck?
	public static long timeBetweenDucks = Framework.secInNanosec / 2;
	
	// Last time a duck was created
	public static long lastDuckTime = 0;
	
	/**
	 * Target info
	 * <li>Starting location
	 * <li>Speed
	 * <li>Points for shooting the duck
	 */
	public static int[][] duckLines = {
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.25), -2, 20},
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.40), -3, 30},
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.55), -4, 40},
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.70), -5, 50},
	};
	
	// Which is the next duck
	public static int nextDuckLines = 0;
	
	// Target coords
	public int x, y;
	
	// Target speed and direction
	public int speed;
	
	// Points for shooting duck
	public int score;
	
	// Target image
	private BufferedImage duckImg;
	
	/**
	 * Create new duck
	 * 
	 * @param x			Starting x-coord
	 * @param y			Starting y-coord
	 * @param speed		Target speed
	 * @param score		How many points
	 * @param duckImg	Target image
	 */
	public Target(int x, int y, int speed, int score, BufferedImage duckImg) {
		this.x = x;
		this.y = y;
		
		this.speed = speed;
		
		this.score = score;
		
		this.duckImg = duckImg;
	}
	
	/**
	 * Move duck
	 */
	public void Update() {
		x += speed;
	}
	
	/**
	 * Draw duck
	 * 
	 * @param g2d	Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		g2d.drawImage(duckImg, x, y, null);
	}
}
