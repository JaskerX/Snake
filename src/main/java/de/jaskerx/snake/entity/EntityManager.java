package de.jaskerx.snake.entity;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EntityManager {

    Map<String, EntityGroup> entityGroups;

    public EntityManager() {
        this.entityGroups = new HashMap<>();
    }

    public void addEntity(String group, Entity entity) {
        if(this.entityGroups.containsKey(group)) {
            this.entityGroups.get(group).addEntity(entity);
            return;
        }
        this.entityGroups.put(group, new EntityGroup(entity));
    }

    public void replaceApple() {
        Random random = new Random();
        this.entityGroups.put("apple", new EntityGroup(new Apple(random.nextInt(29) * 30, random.nextInt(19) * 30)));
    }

    public void increasePoints() {
        Text text = (Text) this.entityGroups.get("points").getFirst();
        text.setText(String.valueOf(Integer.parseInt(text.getText()) + 1));
    }

    public void drawEntities(Graphics2D graphics2D) {
        entityGroups.values().forEach(entityGroup -> entityGroup.drawEntities(graphics2D));
    }

    public Map<String, EntityGroup> getEntityGroups() {
        return entityGroups;
    }

}
