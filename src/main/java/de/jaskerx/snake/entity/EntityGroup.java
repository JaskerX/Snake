package de.jaskerx.snake.entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityGroup {

    List<Entity> entities;

    public EntityGroup(Entity... entities) {
        this.entities = new ArrayList<>(Arrays.asList(entities));
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void drawEntities(Graphics2D graphics2D) {
        this.entities.forEach(entity -> entity.draw(graphics2D));
    }

    public Entity getFirst() {
        return this.entities.size() > 0 ? this.entities.get(0) : null;
    }

    public List<Entity> getEntities() {
        return entities;
    }

}
