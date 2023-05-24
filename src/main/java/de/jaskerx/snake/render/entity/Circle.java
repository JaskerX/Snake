package de.jaskerx.snake.render.entity;

import java.awt.*;

public class Circle extends Entity {

    private Color color;
    private int opacity;
    private AnimationMode animationMode;

    public Circle(double x, double y, double radius, Color color) {
        super(x, y, radius * 2, radius * 2);
        this.color = color;
        this.opacity = 255;
        this.animationMode = AnimationMode.REMOVE;
    }

    public Circle(double x, double y, double radius, Color color, int opacity, AnimationMode animationMode) {
        super(x, y, radius * 2, radius * 2);
        this.color = color;
        this.opacity = opacity;
        this.animationMode = animationMode;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        this.color = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.opacity);
        graphics2D.setColor(this.color);
        graphics2D.fillOval((int) this.x, (int) this.y, (int) this.width, (int) this.height);
    }

    public void changeOpacity() {
        this.opacity += this.animationMode.equals(AnimationMode.ADD) ? 5 : -5;
        if(this.opacity == 0) {
            this.animationMode = AnimationMode.ADD;
        }
        if(this.opacity == 255) {
            this.animationMode = AnimationMode.REMOVE;
        }
    }

    public enum AnimationMode {
        ADD,
        REMOVE
    }

}
