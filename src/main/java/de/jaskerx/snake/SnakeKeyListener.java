package de.jaskerx.snake;

import de.jaskerx.snake.render.SnakeObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakeKeyListener implements KeyListener {

    private final SnakeObject snakeObject;

    public SnakeKeyListener(SnakeObject snakeObject) {
        this.snakeObject = snakeObject;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Richtung ändern
        switch (e.getKeyChar()) {
            case 'w':
                this.snakeObject.setDirection(Direction.UP);
                break;
            case 'a':
                this.snakeObject.setDirection(Direction.LEFT);
                break;
            case 's':
                this.snakeObject.setDirection(Direction.DOWN);
                break;
            case 'd':
                this.snakeObject.setDirection(Direction.RIGHT);
                break;
        }

        // neues Spiel starten
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            Snake.startNewGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
