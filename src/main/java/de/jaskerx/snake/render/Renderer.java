package de.jaskerx.snake.render;

import de.jaskerx.snake.MainFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Renderer {

    private final MainFrame mainFrame;
    private boolean render;
    private Map<String, RenderablesGroup> lastRendered;

    public Renderer(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.render = false;
    }

    public void startRendering() {
        this.render = true;
        long maxMillisBeforeNextImage = 5;

        new Thread(() -> {
            while(this.render) {
                LocalDateTime localDateTimeBefore = LocalDateTime.now();

                // Wenn zu rendernde Objekte die selben sind wie beim letzten Bild, wird das Bild nicht aktualisiert
                if(this.lastRendered == null || !this.lastRendered.values().equals(this.mainFrame.getRenderablesManager().getRenderablesGroups().values())) {
                    BufferedImage bufferedImage = new BufferedImage(this.mainFrame.WIDTH_PX, this.mainFrame.HEIGHT_PX, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics2D = bufferedImage.createGraphics();
                    this.mainFrame.getRenderablesManager().getRenderablesGroups().forEach((name, renderablesGroup) -> {
                        renderablesGroup.getRenderables().forEach(renderable -> renderable.render(graphics2D));
                    });
                    // Bild aktualisieren
                    this.mainFrame.setImage(bufferedImage);
                    // zuletzt gerenderte Objekte abspeichern
                    this.lastRendered = Collections.synchronizedMap(new HashMap<>(this.mainFrame.getRenderablesManager().getRenderablesGroups()));
                }
                LocalDateTime localDateTimeNow = LocalDateTime.now();

                long millisBeforeNextImage = maxMillisBeforeNextImage;
                long difference = ChronoUnit.MILLIS.between(localDateTimeNow, localDateTimeBefore.plus(maxMillisBeforeNextImage, ChronoUnit.MILLIS));
                if(difference < 0) {
                    millisBeforeNextImage = maxMillisBeforeNextImage + difference;
                }

                try {
                    Thread.sleep(millisBeforeNextImage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopRendering() {
        this.render = false;
    }

}
