package de.jaskerx.snake;

import de.jaskerx.snake.render.entity.Text;
import de.jaskerx.snake.render.SnakeObject;
import de.jaskerx.snake.runnable.RunnableManager;

import java.awt.event.KeyListener;

public class Snake {

    public static boolean debug = false;
    private static MainFrame mainFrame;
    private static RunnableManager runnableManager;

    public static void main(String[] args) {
        if(System.getProperty("debug") != null) {
            if(System.getProperty("debug").isEmpty()) {
                debug = true;
            } else {
                debug = Boolean.parseBoolean(System.getProperty("debug"));
            }
        }
        mainFrame = new MainFrame();
        runnableManager = new RunnableManager(mainFrame);

        // Spiel starten
        startNewGame();
    }

    public static void startNewGame() {
        mainFrame.getRenderer().stopRendering();

        // Neue Schlange erzeugen
        SnakeObject snakeObject = new SnakeObject(300, 300);

        for(KeyListener keyListener : mainFrame.getKeyListeners()) {
            mainFrame.removeKeyListener(keyListener);
        }
        mainFrame.addKeyListener(new SnakeKeyListener(snakeObject));
        mainFrame.setVisible(true);

        // Apfel und Punkteanzeige hinzufügen
        mainFrame.getRenderablesManager().getRenderablesGroups().clear();
        mainFrame.getRenderablesManager().addRenderable("snake", snakeObject);
        mainFrame.getRenderablesManager().replaceApple();
        mainFrame.getRenderablesManager().addRenderable("points", new Text(50, "0", 40, mainFrame));

        mainFrame.getRenderer().startRendering();
        runnableManager.startGameRunnable(snakeObject);
    }

}
