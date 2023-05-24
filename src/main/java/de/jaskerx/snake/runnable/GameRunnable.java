package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.render.SnakeObject;
import de.jaskerx.snake.render.entity.Apple;
import de.jaskerx.snake.render.RenderablesManager;
import de.jaskerx.snake.render.entity.SnakePart;
import de.jaskerx.snake.render.entity.Text;

import java.util.concurrent.CompletableFuture;

public class GameRunnable implements Runnable {

    private final MainFrame mainFrame;
    private final SnakeObject snakeObject;
    private final RunnableManager runnableManager;

    public GameRunnable(MainFrame mainFrame, SnakeObject snakeObject, RunnableManager runnableManager) {
        this.mainFrame = mainFrame;
        this.snakeObject = snakeObject;
        this.runnableManager = runnableManager;
    }

    @Override
    public void run() {
        RenderablesManager renderablesManager = this.mainFrame.getRenderablesManager();
        SnakePart front = this.snakeObject.getFront();

        // Berührt der Anfang der Schlange den Apfel?
        if(front.checkCollision((Apple) renderablesManager.getRenderablesGroups().get("apple").getFirst())) {
            this.snakeObject.addSection();
            renderablesManager.replaceApple();
            renderablesManager.increasePoints();
        }

        // Ist die Schlange außerhalb des Bildes?
        if(front.getX() < -15 || front.getX() >= this.mainFrame.WIDTH_PX || front.getY() < -15 || front.getY() >= this.mainFrame.HEIGHT_PX) {
            endGame();
            return;
        }

        if(this.snakeObject.checkSelfCollision()) {
            endGame();
            return;
        }

        // Schlange bewegen
        this.snakeObject.move();
    }

    private void endGame() {
        this.runnableManager.stopGameRunnable();
        int points = Integer.parseInt(((Text) this.mainFrame.getRenderablesManager().getRenderablesGroups().get("points").getFirst()).getText());
        this.mainFrame.getRenderablesManager().getRenderablesGroups().clear();
        // zu Ladebildschirm wechseln
        this.runnableManager.startLoadingRunnable();
        // Daten aus der Datenbank laden
        CompletableFuture.runAsync(new HighscoreRunnable(this.runnableManager, this.mainFrame, points));
    }

}
