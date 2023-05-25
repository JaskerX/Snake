package de.jaskerx.snake.render;

import de.jaskerx.snake.Direction;
import de.jaskerx.snake.render.entity.SnakePart;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SnakeObject implements Renderable {

    private final SnakePart front;
    private final SnakePart back;
    private final List<SnakePart> middleParts;

    public SnakeObject(double startX, double startY) {
        this.front = new SnakePart(startX, startY);
        this.back = new SnakePart(startX, startY, 0, 30);
        this.middleParts = Collections.synchronizedList(new ArrayList<>());
        this.addSection();
        this.addSection();
    }

    public void addSection() {
        SnakePart lastSnakePart = this.middleParts.size() == 0 ? this.front : getLastMiddlePart();
        SnakePart snakePart;
        // Neues Teil abhängig vom letzten Teil erzeugen
        switch (lastSnakePart.getDirection()) {
            case RIGHT:
                snakePart = new SnakePart(lastSnakePart.getX() - 30, lastSnakePart.getY(), lastSnakePart.getDirection());
                break;
            case LEFT:
                snakePart = new SnakePart(lastSnakePart.getX() + 30, lastSnakePart.getY(), lastSnakePart.getDirection());
                break;
            case DOWN:
                snakePart = new SnakePart(lastSnakePart.getX(), lastSnakePart.getY() - 30, lastSnakePart.getDirection());
                break;
            case UP:
                snakePart = new SnakePart(lastSnakePart.getX(), lastSnakePart.getY() + 30, lastSnakePart.getDirection());
                break;
            default: return;
        }
        // Neues Teil zur Schlange hinzufügen
        this.middleParts.add(snakePart);
        this.back.setX(this.back.getX() - 30);
    }

    public void setDirection(Direction direction) {
        // Richtung des Anfangs und damit der Schlange ändern
        this.front.setDirection(direction);
        // Anfang passend zum Raster verschieben
        if(direction.isHorizontal()) {
            double moveY;
            if(this.front.getY() % 30 <= 15) {
                moveY = -(this.front.getY() % 30);
            } else {
                moveY = 30 - (this.front.getY() % 30);
            }
            this.front.setY(this.front.getY() + moveY);
        } else {
            double moveX;
            if(this.front.getX() % 30 <= 15) {
                moveX = -(this.front.getX() % 30);
            } else {
                moveX = 30 - (this.front.getX() % 30);
            }
            this.front.setX(this.front.getX() + moveX);
        }
    }

    public void render(Graphics2D graphics2D) {
        // Alle Schlangenteile zeichnen
        this.front.render(graphics2D);
        this.back.render(graphics2D);
        this.middleParts.forEach(snakePart -> snakePart.render(graphics2D));
    }

    public void move() {
        final double moveValue = 1;
        // Falls das Feld am Anfang oder am Ende vollständig ausgefüllt ist, wird dieses durch ein ganzes Teil ersetzt und in die jeweilge Richtung verschoben
        if((this.front.getDirection().isHorizontal() && this.front.getWidth() == 30) || (!this.front.getDirection().isHorizontal() && this.front.getHeight() == 30)) {
            // Anfang verschieben
            switch (this.front.getDirection()) {
                case RIGHT: {
                    this.front.setX(this.front.getX() + 30);
                    this.front.setWidth(0);
                    this.front.setHeight(30);}
                    break;
                case LEFT:
                    this.front.setX(this.front.getX());
                    this.front.setWidth(0);
                    this.front.setHeight(30);
                    break;
                case DOWN:
                    this.front.setY(this.front.getY() + 30);
                    this.front.setWidth(30);
                    this.front.setHeight(0);
                    break;
                case UP:
                    this.front.setY(this.front.getY());
                    this.front.setWidth(30);
                    this.front.setHeight(0);
                    break;
            }

            // Ende verschieben
            switch (this.back.getDirection()) {
                case RIGHT:
                    this.back.setX(this.back.getX() + 30);
                    break;
                case LEFT:
                    this.back.setX(this.back.getX());
                    break;
                case DOWN:
                    this.back.setY(this.back.getY());
                    break;
                case UP:
                    this.back.setY(this.back.getY() - 30);
                    break;
            }
            this.back.setWidth(30);
            this.back.setHeight(30);

            // Neues Teil hinzufügen und dafür letztes löschen
            if(this.middleParts.size() > 0) {
                SnakePart lastMiddlePart = getLastMiddlePart();
                SnakePart newPart;
                switch (this.front.getDirection()) {
                    case RIGHT:
                        newPart = new SnakePart(this.front.getX() - 30, this.front.getY(), this.front.getDirection());
                        this.middleParts.add(0, newPart);
                        break;
                    case DOWN:
                        this.middleParts.add(0, new SnakePart(this.front.getX(), this.front.getY() - 30, this.front.getDirection()));
                        break;
                    case LEFT:
                    case UP:
                        this.middleParts.add(0, new SnakePart(this.front.getX(), this.front.getY(), this.front.getDirection()));
                        break;
                }
                this.middleParts.remove(lastMiddlePart);
            }
        }

        // Wird immer ausgeführt
        // Anfang verbreitern, um Bewegungsanimation zu erzeugen
        switch (this.front.getDirection()) {
            case RIGHT:
                this.front.setWidth(this.front.getWidth() + moveValue);
                break;
            case LEFT:
                this.front.setWidth(this.front.getWidth() + moveValue);
                this.front.setX(this.front.getX() - moveValue);
                break;
            case DOWN:
                this.front.setHeight(this.front.getHeight() + moveValue);
                break;
            case UP:
                this.front.setHeight(this.front.getHeight() + moveValue);
                this.front.setY(this.front.getY() - moveValue);
                break;
        }

        // Ende verkleinern, um Bewegungsanimation zu erzeugen
        SnakePart lastSnakePart = this.middleParts.size() == 0 ? this.front : getLastMiddlePart();
        switch (lastSnakePart.getDirection()) {
            case RIGHT:
                this.back.setWidth(this.back.getWidth() - moveValue);
                this.back.setY(lastSnakePart.getY());
                this.back.setX(lastSnakePart.getX() - this.back.getWidth());
                break;
            case LEFT:
                this.back.setWidth(this.back.getWidth() - moveValue);
                this.back.setY(lastSnakePart.getY());
                this.back.setX(lastSnakePart.getX() + 30);
                break;
            case DOWN:
                this.back.setHeight(this.back.getHeight() - moveValue);
                this.back.setY(lastSnakePart.getY() - this.back.getHeight());
                this.back.setX(lastSnakePart.getX());
                break;
            case UP:
                this.back.setHeight(this.back.getHeight() - moveValue);
                this.back.setY(lastSnakePart.getY() + 30);
                this.back.setX(lastSnakePart.getX());
                break;
        }
    }

    public boolean checkSelfCollision() {
        // für jedes Teil überprüfen, ob es sich an der gleichen Stelle wie der Anfang befindet
        for(int i = 1; i < this.middleParts.size(); i++) {
            if(this.front.getX() == this.middleParts.get(i).getX() && this.front.getY() == this.middleParts.get(i).getY()) {
                // wenn Position gleich, gibt es eine Kollision
                return true;
            }
        }
        return false;
    }

    public SnakePart getLastMiddlePart() {
        return this.middleParts.get(this.middleParts.size() - 1);
    }

    public SnakePart getFront() {
        return front;
    }

}
