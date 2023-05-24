package de.jaskerx.snake.render.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Apple extends Entity {

    private final Image image;

    public Apple(double x, double y) {
        super(x, y, 30, 30);
        try {
            this.image = ImageIO.read(getClass().getResource("/apple.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(Graphics2D graphics2D) {
        graphics2D.drawImage(this.image, (int) this.x, (int) this.y, (int) this.width, (int) this.height, null);
    }

}
