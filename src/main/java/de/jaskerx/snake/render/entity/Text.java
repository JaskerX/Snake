package de.jaskerx.snake.render.entity;

import de.jaskerx.snake.MainFrame;

import java.awt.*;

public class Text extends Entity {

    private final MainFrame mainFrame;
    private final int fontSize;
    private String text;

    public Text(int y, String text, int fontSize, MainFrame mainFrame) {
        super(0, y, mainFrame.getWidth(), 20);
        this.text = text;
        this.mainFrame = mainFrame;
        this.fontSize = fontSize;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(new Font(null, Font.PLAIN, this.fontSize));
        graphics2D.drawString(this.text, (this.mainFrame.getWidth() - graphics2D.getFontMetrics().stringWidth(this.text)) / 2, (int) this.y);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
