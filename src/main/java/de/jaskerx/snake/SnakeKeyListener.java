package de.jaskerx.snake;

import de.jaskerx.snake.runnable.RunnableManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakeKeyListener implements KeyListener {

    private final SnakeObject snakeObject;
    private final MainFrame mainFrame;
    private final RunnableManager runnableManager;

    public SnakeKeyListener(SnakeObject snakeObject, MainFrame mainFrame, RunnableManager runnableManager) {
        this.snakeObject = snakeObject;
        this.mainFrame = mainFrame;
        this.runnableManager = runnableManager;
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.mainFrame.startNewGame(this.runnableManager);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
