package de.jaskerx.snake.utils;

public class DirectionChange {

	private int x;
	private int y;
	private int direction;
	
	
	public DirectionChange(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
