package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.entity.Circle;
import de.jaskerx.snake.entity.EntityManager;
import de.jaskerx.snake.entity.Text;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LoadingRunnable implements Runnable {

    private final String text;
    private final MainFrame mainFrame;

    public LoadingRunnable(String text, MainFrame mainFrame) {
        this.text = text;
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        BufferedImage bufferedImage = new BufferedImage(this.mainFrame.WIDTH_PX, this.mainFrame.HEIGHT_PX, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        EntityManager entityManager = this.mainFrame.getEntityManager();
        // Infotext als Entity hinzufügen
        if(!entityManager.getEntityGroups().containsKey("info")) {
            Text text = new Text(200, this.text, 40, this.mainFrame);
            entityManager.addEntity("info", text);
        }
        // Kreise als Entities hinzufügen
        if(!entityManager.getEntityGroups().containsKey("progress")) {
            Circle circle1 = new Circle(390, 350, 15, Color.WHITE, 255, Circle.AnimationMode.REMOVE);
            Circle circle2 = new Circle(440, 350, 15, Color.WHITE, 205, Circle.AnimationMode.ADD);
            Circle circle3 = new Circle(490, 350, 15, Color.WHITE, 155, Circle.AnimationMode.ADD);
            entityManager.addEntity("progress", circle1);
            entityManager.addEntity("progress", circle2);
            entityManager.addEntity("progress", circle3);
        }

        // Animation der Kreise (verblassen)
        entityManager.getEntityGroups().get("progress").getEntities().forEach(entity -> {
            Circle circle = (Circle) entity;
            circle.changeOpacity();
        });

        // Neues Bild als Bild setzen
        entityManager.drawEntities(graphics2D);
        this.mainFrame.setImage(bufferedImage);
    }

}
