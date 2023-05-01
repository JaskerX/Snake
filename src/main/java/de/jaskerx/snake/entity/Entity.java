package de.jaskerx.snake.entity;

import java.awt.*;

public abstract class Entity {

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Graphics2D graphics2D);

    // Überprüfen, ob eine Kollision mit einer anderen Entity besteht
    public boolean checkCollision(Entity entity) {
        double xLeft = this.x;
        double xRight = this.x + this.width;
        double yBottom = this.y + this.height;
        double yTop = this.y;
        double xLeftCheck = entity.getX() + 1;
        double xRightCheck = entity.getX() + entity.getWidth() - 1;
        double yBottomCheck = entity.getY() + entity.getHeight() - 1;
        double yTopCheck = entity.getY() + 1;

        return (((yTop <= yBottomCheck && yBottom >= yBottomCheck) || (yTop <= yTopCheck && yBottom >= yTopCheck))
                && ((xLeft <= xLeftCheck && xRight >= xLeftCheck) || (xLeft <= xRightCheck && xRight >= xRightCheck)));
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

}
