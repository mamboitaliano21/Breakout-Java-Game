/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** Offset of Paddle counting from Top */
	private static final int PADDLE_OFFSET = HEIGHT - PADDLE_Y_OFFSET;

/** Animation Delay for the Ball */
	private static final int DELAY = 10;

/** Cheat **/
	private static final int CHEAT_ON = 1;
	private static final int CHEAT_OFF = 0;
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		setup();
		addMouseListeners();
		dropBall();
		while (brick_count != 0) {
			moveBall();
			pause(DELAY);
			updateScore();
			if (lose == 1) break;
		}
		gameLost();
	}
	
	private void setup(){
		
		int y = BRICK_Y_OFFSET;
		for (int i = 0;i < NBRICK_ROWS;i++) {
			int x = APPLICATION_WIDTH/(NBRICKS_PER_ROW*BRICK_WIDTH);
			y += BRICK_SEP + BRICK_HEIGHT;
			for (int j = 0; j<NBRICKS_PER_ROW; j++) {
				GRect Brick = new GRect(BRICK_WIDTH,BRICK_HEIGHT);
				Brick.setFilled(true);
				// Maintain same color pattern even if number
				// of bricks exceed 10
				if(i>10) i -= i*10;
				if(i<2) Brick.setColor(Color.red);
				if(i>=2 && i<=4) Brick.setColor(Color.orange);
				if(i>=4 && i<=6) Brick.setColor(Color.yellow);
				if(i>=6 && i<=8) Brick.setColor(Color.green);
				if(i>=8 && i<=10) Brick.setColor(Color.cyan);
				add(Brick,x,y);
				x = x + (BRICK_SEP + BRICK_WIDTH);
			}
		}
		brick_count = NBRICKS_PER_ROW * NBRICK_ROWS;
		Paddle = new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
		Paddle.setFilled(true);
		add(Paddle,(WIDTH- PADDLE_WIDTH)/2,PADDLE_OFFSET);
		
		Ball = new GOval(BALL_RADIUS,BALL_RADIUS);
		Ball.setFilled(true);
		add(Ball,(WIDTH-BALL_RADIUS)/2,(HEIGHT-BALL_RADIUS)/2);
		
		score_label = new GLabel("Score:  ");
		score_label.setFont("Times New Roman-24");
		add(score_label,BRICK_SEP,score_label.getAscent());
		cheat = CHEAT_OFF;
	}
	
	public void mouseMoved(MouseEvent e){
		if( (e.getX() > 0) && (e.getX() < WIDTH - PADDLE_WIDTH)) Paddle.setLocation(e.getX(),PADDLE_OFFSET);
	}
	
	private void moveBall(){
		GObject collider = getCollidingObject();
		//bouncing effect
		if(Ball.getX() - BALL_RADIUS <= 0 || Ball.getX() + BALL_RADIUS >= WIDTH)  vx = -vx;
		if (Ball.getY() - BALL_RADIUS <= 0) vy = -vy;
		if (Ball.getY() + BALL_RADIUS >= HEIGHT) lose = 1;
		if(collider == Paddle)vy = -vy;

		
		
		if (collider != Paddle && collider != null && collider != score_label && collider != cheat_label){
			if(cheat == CHEAT_OFF){
				remove(collider); vy=-vy; 
			}
			if(cheat == CHEAT_ON){
				remove(collider);
			}
			brick_count =-1 ; score += 1;
		}

		
		Ball.move(vx,vy);
	}
	
	private void dropBall(){
		vx = rgen.nextDouble(1.0, 3.0);
		vy = 3;
		if (rgen.nextBoolean(0.5)) vx = -vx;
	}
	
	private GObject getCollidingObject(){

		GObject collider = getElementAt(Ball.getX(),Ball.getY());
		if (collider == null) collider = getElementAt(Ball.getX() + BALL_RADIUS*2,Ball.getY());
		if (collider == null) collider = getElementAt(Ball.getX(),Ball.getY() + BALL_RADIUS*2);
		if (collider == null) collider = getElementAt(Ball.getX() + BALL_RADIUS*2,Ball.getY() + BALL_RADIUS*2);

		return collider;
	}
	
	private void updateScore(){
		score_label.setLabel("Score: " + score);
		
	}
	
	 public void mousePressed(MouseEvent e){
		if (cheat == CHEAT_OFF){
			cheat = CHEAT_ON;
			cheat_label = new GLabel("Cheat On");
			cheat_label.setFont("Times New Roman-24");
			add(cheat_label,WIDTH-cheat_label.getWidth(),score_label.getAscent());
		}
		else if (cheat == CHEAT_ON){
			cheat = CHEAT_OFF;
			remove(cheat_label);
		}
		
		
	}
	 
	 //You lost the game
	 private void gameLost(){
		 removeAll();
		 GLabel youLost = new GLabel("You Suck");
		 youLost.setFont("Times New Roman-100");
		 youLost.setLocation(getWidth()/2-youLost.getWidth()/2,getHeight()/2-youLost.getAscent()/2);
		 add(youLost);
	 }

	/* Private instance variable */
	private GRect Paddle;
	private GOval Ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int score;
	private GLabel score_label;
	private int brick_count;
	private int cheat;
	private GLabel cheat_label;
	private int lose;
	
}
