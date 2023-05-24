package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.render.entity.Circle;
import de.jaskerx.snake.render.RenderablesManager;
import de.jaskerx.snake.render.entity.Text;

import java.awt.*;

public class LoadingRunnable implements Runnable {

    private final String text;
    private final MainFrame mainFrame;

    public LoadingRunnable(String text, MainFrame mainFrame) {
        this.text = text;
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        RenderablesManager renderablesManager = this.mainFrame.getRenderablesManager();
        // Infotext hinzufügen
        if(!renderablesManager.getRenderablesGroups().containsKey("info")) {
            Text text = new Text(200, this.text, 40, this.mainFrame);
            renderablesManager.addRenderable("info", text);
        }
        // Kreise hinzufügen
        if(!renderablesManager.getRenderablesGroups().containsKey("progress")) {
            Circle circle1 = new Circle(390, 350, 15, Color.WHITE, 255, Circle.AnimationMode.REMOVE);
            Circle circle2 = new Circle(440, 350, 15, Color.WHITE, 205, Circle.AnimationMode.ADD);
            Circle circle3 = new Circle(490, 350, 15, Color.WHITE, 155, Circle.AnimationMode.ADD);
            renderablesManager.addRenderable("progress", circle1);
            renderablesManager.addRenderable("progress", circle2);
            renderablesManager.addRenderable("progress", circle3);
        }

        // Animation der Kreise (verblassen)
        renderablesManager.getRenderablesGroups().get("progress").getRenderables().forEach(entity -> {
            Circle circle = (Circle) entity;
            circle.changeOpacity();
        });
    }

}
