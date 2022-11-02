package de.jaskerx.snake.main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		try {
			
			new Game();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
