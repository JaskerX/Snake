package de.jaskerx.snake.main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.jaskerx.engine.simple.main.Engine;
import de.jaskerx.engine.simple.objects.CustomCode;
import de.jaskerx.engine.simple.objects.Entity;
import de.jaskerx.engine.simple.objects.Stage;
import de.jaskerx.engine.simple.objects.entities.Image;
import de.jaskerx.engine.simple.objects.entities.Rectangle;
import de.jaskerx.engine.simple.objects.entities.Text;
import de.jaskerx.snake.utils.DirectionChange;

public class Game {

	Engine engine;
	Text score;
	Entity[] notChanging;
	ArrayList<Entity> snakeParts = new ArrayList<>();
	ArrayList<Entity> dCs = new ArrayList<>();
	ArrayList<Entity> end = new ArrayList<>();
	
	ArrayList<DirectionChange> directionChanges = new ArrayList<>();
	
	
	public Game() throws IOException {
		engine = new Engine(60);
		
		Image apple = new Image(30, 30);
		apple.setPosX(new Random().nextInt(30) * 30);
		apple.setPosY(new Random().nextInt(20) * 30);
		apple.setNumAttributes(0);
		apple.setImage(ImageIO.read(getClass().getResource("/resources/apple.png")));
		Rectangle snake = new Rectangle(30, 30);
		snake.setColor(Color.GREEN);
		snake.setPosX(210);
		snake.setPosY(210);
		snake.setNumAttributes(4, 0);
		score = new Text(900, 100, Color.WHITE);
		score.setPosX(0);
		score.setPosY(50);
		score.setText("Punkte: 0");
		score.setNumAttributes(0);
		Text textEnd = new Text(900, 20, Color.WHITE);
		textEnd.setPosX(0);
		textEnd.setPosY(80);
		textEnd.setText("Du hast verloren!");
		Text textTopFive = new Text(900, 20, Color.WHITE);
		textTopFive.setPosX(0);
		textTopFive.setPosY(200);
		textTopFive.setText("Top 5 erreichte Punkte:");
		
		notChanging = new Entity[] {apple, score};
		snakeParts.add(snake);
		end.add(textEnd);
		end.add(textTopFive);
		
		Stage s = new Stage(900, 600, "Snake", getCode(0), notChanging, Arrays.copyOf(snakeParts.toArray(), snakeParts.size(), Entity[].class), Arrays.copyOf(dCs.toArray(), dCs.size(), Entity[].class));
		s.setColor(Color.BLACK);
		Stage sEnd = new Stage(900, 600, "Snake", getCode(1), Arrays.copyOf(end.toArray(), end.size(), Entity[].class));
		sEnd.setColor(Color.BLACK);
		
		engine.setStages(s, sEnd);
		
		JFrame frame = engine.getFrame();
		frame.addKeyListener(getKeyListener());
		
		engine.render(0);
	}
	
	
	private CustomCode getCode(int stage) {
		
		CustomCode code = null;
		
		switch(stage) {
		
			case 0:
				code = new CustomCode() {
					@Override
					public void executeBackground() {
						
						Entity last = engine.getRenderedStage().getEntities()[1][snakeParts.size() - 1];
						Entity first = engine.getRenderedStage().getEntities()[1][0];
						Entity apple = engine.getRenderedStage().getEntities()[0][0];
						
						//direction change
						if(engine.getRenderedStage().getEntities().length > 1) {
							
							for(int i = 0; i < engine.getRenderedStage().getEntities()[1].length; i++) {
								Entity e = engine.getRenderedStage().getEntities()[1][i];
								
								for(int j = 0; j < directionChanges.size(); j++) {
									
									if(e.getPosX() == directionChanges.get(j).getX() && e.getPosY() == directionChanges.get(j).getY()) {
										e.setNumAttribute(0, directionChanges.get(j).getDirection());
										
										if(i == engine.getRenderedStage().getEntities()[1].length - 1) {
											try {
												Thread.sleep(1);
											} catch (InterruptedException e1) {
												e1.printStackTrace();
											}
											directionChanges.remove(j);
											dCs.remove(j);
											engine.getRenderedStage().setEntities(2, Arrays.copyOf(dCs.toArray(), dCs.size(), Entity[].class));
										}
									}
								}
							}
						}
						
						//apple
						if(first.getPosX() == apple.getPosX() && first.getPosY() == apple.getPosY()) {
							
							apple.setPosX(new Random().nextInt(30) * 30);
							apple.setPosY(new Random().nextInt(20) * 30);
							Rectangle snake = new Rectangle(30, 30);
							snake.setColor(Color.GREEN);
							
							switch(last.getNumAttributes()[0]) {
								case 1:
									snake.setPosX(last.getPosX());
									snake.setPosY(last.getPosY() + 30);
									break;
								case 2:
									snake.setPosX(last.getPosX() + 30);
									snake.setPosY(last.getPosY());
									break;
								case 3:
									snake.setPosX(last.getPosX());
									snake.setPosY(last.getPosY() - 30);
									break;
								case 4:
									snake.setPosX(last.getPosX() - 30);
									snake.setPosY(last.getPosY());
									break;
							}
							
							snake.setNumAttributes(last.getNumAttributes()[0], snakeParts.size());
							snakeParts.add(snake);
							engine.getRenderedStage().setEntities(1, Arrays.copyOf(snakeParts.toArray(), snakeParts.size(), Entity[].class));
							score.setText("Punkte: " + (snakeParts.size() - 1));
						}
						
						//snake
						// search -> first
						int xSearch = first.getPosX() + first.getWidth() / 2;
						int ySearch = first.getPosY() + first.getHeight() / 2;
						for(int i = 0; i < snakeParts.size(); i++) {
							// check -> snakeParts.get[i]
							int xCheck = snakeParts.get(i).getPosX();
							int yCheck = snakeParts.get(i).getPosY();
							int xCheck2 = (int) (snakeParts.get(i).getPosX() + first.getWidth());
							int yCheck2 = (int) (snakeParts.get(i).getPosY() + first.getHeight());
							if((snakeParts.get(i).getNumAttributes()[1] != first.getNumAttributes()[1]) && (xCheck < xSearch && xSearch < xCheck2) && (yCheck < ySearch && ySearch < yCheck2)) {
								stopGame();
							}
						}
						
						//border
						if(first.getPosX() < 0 || first.getPosY() < 0 || first.getPosX() + first.getWidth() > 900 || first.getPosY() + first.getHeight() > 600) {
							stopGame();
						}
						
					}
		
					@Override
					public void executeForeground() {
						
						for(int i = 0; i < engine.getRenderedStage().getEntities().length; i++) {
							for(int j = 0; j < engine.getRenderedStage().getEntities()[i].length; j++) {
								Entity e = engine.getRenderedStage().getEntities()[i][j];
								e.draw(engine.getGraphics());
								switch(e.getNumAttributes()[0]) {
									case 1: e.setPosY(e.getPosY() - 2);
										break;
									case 2: e.setPosX(e.getPosX() - 2);
										break;
									case 3: e.setPosY(e.getPosY() + 2);
										break;
									case 4: e.setPosX(e.getPosX() + 2);
										break;
								}
							}
						}
					}
				};
				break;
			
			case 1:
				code = new CustomCode() {
					
					@Override
					public void executeBackground() {}
					
					@Override
					public void executeForeground() {
						
						for(int i = 0; i < engine.getRenderedStage().getEntities().length; i++) {
							for(int j = 0; j < engine.getRenderedStage().getEntities()[i].length; j++) {
								Entity e = engine.getRenderedStage().getEntities()[i][j];
								e.draw(engine.getGraphics());
							}
						}
					}
					
				};
				break;
		}
		
		return code;
	}
	
	private void stopGame() {
		if(engine.getRenderedStage().equals(engine.getStages()[0])) {
			engine.stop();
			int points = snakeParts.size() - 1;
			((Text) engine.getStages()[1].getEntities()[0][0]).setText("Das Spiel ist beendet! Du hast " + points + ((points == 1) ? " Punkt" : " Punkte") + " erreicht.");
			loadData(points);
			engine.render(1);
		}
	}
	
	private KeyListener getKeyListener() {
		
		KeyListener kl = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent event) {
		
				//stage2
				if(engine.getRenderedStage().getEntities().length == 1) return;
				
				//stage1
				Entity e = engine.getRenderedStage().getEntities()[1][0];
				DirectionChange c;
								
				switch(event.getKeyChar()) {
				
					case 'w':
						e.setNumAttribute(0, 1);
						break;
					case 'a':
						e.setNumAttribute(0, 2);
						break;
					case 's':
						e.setNumAttribute(0, 3);
						break;
					case 'd':
						e.setNumAttribute(0, 4);
						break;
					
				}
				
				if((event.getKeyChar() == 'w' || event.getKeyChar() == 'a' || event.getKeyChar() == 's' || event.getKeyChar() == 'd') && !engine.getRenderedStage().doWait()) {
					
					engine.getRenderedStage().setWait(true);
					for(int i = 0; i < engine.getRenderedStage().getEntities()[1].length; i++) {
						Entity e2 = engine.getRenderedStage().getEntities()[1][i];
						
						if(e2.getNumAttributes()[0] != 0) {
							int difX = 0;
							int difY = 0;
							if(e2.getPosX() % 30 < 15) {
								difX -= e2.getPosX() % 30;
							} else {
								difX = 30 - (e2.getPosX() % 30);
							}
							if(e2.getPosY() % 30 < 15) {
								difY -= e2.getPosY() % 30;
							} else {
								difY = 30 - (e2.getPosY() % 30);
							}
							
							e2.setPosX(e2.getPosX() + difX);
							e2.setPosY(e2.getPosY() + difY);
						}
					}
					
					c = new DirectionChange(e.getPosX(), e.getPosY());
					c.setDirection(e.getNumAttributes()[0]);
					directionChanges.add(c);
					Rectangle dC = new Rectangle(30, 30);
					dC.setColor(Color.GREEN);
					dC.setPosX(c.getX());
					dC.setPosY(c.getY());
					dC.setNumAttributes(0);
					dCs.add(dC);
					engine.getRenderedStage().setEntities(2, Arrays.copyOf(dCs.toArray(), dCs.size(), Entity[].class));
					
					engine.getRenderedStage().setWait(false);
				}
			}
		};
		
		return kl;
	}
	
	private void loadData(int score) {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:snake.db");
			
			Statement stat = conn.createStatement();
	        stat.executeUpdate("CREATE TABLE IF NOT EXISTS points ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'score' INTEGER)");
	        
	        stat.executeUpdate("INSERT INTO points (score) VALUES (" + score + ")");
	        
	        ResultSet rs = stat.executeQuery("SELECT score FROM points GROUP BY score ORDER BY score DESC LIMIT 5");
	        int i = 0;
	        while(rs.next()) {
	        	ResultSet rsAmount = conn.createStatement().executeQuery("SELECT COUNT(*) FROM points WHERE score = " + rs.getInt("score"));
	        	Text t = new Text(900, 20, Color.WHITE);
	    		t.setPosX(0);
	    		t.setPosY(250 + i * 40);
	    		t.setText(i + 1 + ":   " + rs.getInt("score") + (rs.getInt("score") == 1 ? " Punkt   (" : " Punkte   (") + rsAmount.getInt(1) + " mal)");
	        	end.add(t);
	        	i++;
	        }
	        
	        rs = conn.createStatement().executeQuery("SELECT ROUND(AVG(score)), AVG(score) FROM points");
	        if(rs.next()) {
	        	Text t = new Text(900, 20, Color.WHITE);
	    		t.setPosX(0);
	    		t.setPosY(500 - (5 - i) * 40);
	    		t.setText("Durchschnittliche Punkte:");
	        	end.add(t);
	        	Text t2 = new Text(900, 20, Color.WHITE);
	        	t2.setPosX(0);
	        	t2.setPosY(550 - (5 - i) * 40);
	        	t2.setText(rs.getInt(1) + (rs.getInt(1) == 1 ? " Punkt (" : " Punkte (") + rs.getFloat(2) + ")");
	        	end.add(t2);
	        }
	        conn.close();
	        engine.getStages()[1].setEntities(Arrays.copyOf(end.toArray(), end.size(), Entity[].class));
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
