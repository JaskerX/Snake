package de.jaskerx.snake.render;

import de.jaskerx.snake.render.entity.Apple;
import de.jaskerx.snake.render.entity.Text;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RenderablesManager {

    private final Map<String, RenderablesGroup> renderablesGroups;

    public RenderablesManager() {
        this.renderablesGroups = Collections.synchronizedMap(new HashMap<>());
    }

    public void addRenderable(String group, Renderable renderable) {
        if(this.renderablesGroups.containsKey(group)) {
            this.renderablesGroups.get(group).addRenderable(renderable);
            return;
        }
        this.renderablesGroups.put(group, new RenderablesGroup(renderable));
    }

    // Apfel neu positionieren
    public void replaceApple() {
        Random random = new Random();
        this.renderablesGroups.put("apple", new RenderablesGroup(new Apple(random.nextInt(29) * 30, random.nextInt(19) * 30)));
    }

    // Punktestand erhöhen
    public void increasePoints() {
        Text text = (Text) this.renderablesGroups.get("points").getFirst();
        text.setText(String.valueOf(Integer.parseInt(text.getText()) + 1));
    }

    public Map<String, RenderablesGroup> getRenderablesGroups() {
        return renderablesGroups;
    }

}
