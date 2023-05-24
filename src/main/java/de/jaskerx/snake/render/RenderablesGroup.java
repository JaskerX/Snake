package de.jaskerx.snake.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RenderablesGroup {

    private final List<Renderable> renderables;

    public RenderablesGroup(Renderable... renderables) {
        this.renderables = Collections.synchronizedList(new ArrayList<>(Arrays.asList(renderables)));
    }

    public void addRenderable(Renderable renderable) {
        this.renderables.add(renderable);
    }

    // erstes Object aus der Liste zurückgeben (wenn nicht vorhanden null)
    public Renderable getFirst() {
        return this.renderables.size() > 0 ? this.renderables.get(0) : null;
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

}
