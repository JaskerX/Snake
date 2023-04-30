package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.SnakeObject;
import de.jaskerx.snake.entity.EntityManager;
import de.jaskerx.snake.entity.SnakePart;
import de.jaskerx.snake.entity.Text;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        BufferedImage bufferedImage = new BufferedImage(this.mainFrame.WIDTH_PX, this.mainFrame.HEIGHT_PX, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        EntityManager entityManager = this.mainFrame.getEntityManager();
        SnakePart front = this.snakeObject.getFront();

        // Berührt der Anfang der Schlange den Apfel?
        if(front.checkCollision(entityManager.getEntityGroups().get("apple").getFirst())) {
            this.snakeObject.addSection();
            entityManager.replaceApple();
            entityManager.increasePoints();
        }

        // Ist die Schlange außerhalb des Bildes?
        if(front.getX() < -15 || front.getX() >= this.mainFrame.WIDTH_PX || front.getY() < -15 || front.getY() >= this.mainFrame.HEIGHT_PX) {
            this.runnableManager.stopGameRunnable();
            int points = Integer.parseInt(((Text) this.mainFrame.getEntityManager().getEntityGroups().get("points").getFirst()).getText());
            entityManager.getEntityGroups().clear();
            this.runnableManager.startLoadingRunnable();
            CompletableFuture.runAsync(new HighscoreRunnable(this.runnableManager, this.mainFrame, points));
            return;
        }

        this.snakeObject.move();
        this.snakeObject.draw(graphics2D);

        entityManager.drawEntities(graphics2D);

        this.mainFrame.setImage(bufferedImage);
    }

}
