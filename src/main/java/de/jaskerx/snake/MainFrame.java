package de.jaskerx.snake;

import de.jaskerx.snake.entity.EntityManager;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {

    private final JLabel jLabel;
    public final int WIDTH_PX = 900;
    public final int HEIGHT_PX = 600;
    private final EntityManager entityManager;

    public MainFrame() {
        super();
        this.jLabel = new JLabel();
        this.entityManager = new EntityManager();
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.updateJLabel(new BufferedImage(this.WIDTH_PX, this.HEIGHT_PX, BufferedImage.TYPE_INT_RGB));
        this.add(this.jLabel);
        this.pack();
    }

    // Bild aktualisieren
    public void setImage(BufferedImage bufferedImage) {
        this.updateJLabel(bufferedImage);
        this.jLabel.repaint();
    }

    private void updateJLabel(BufferedImage bufferedImage) {
        this.jLabel.setIcon(new ImageIcon(bufferedImage));
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
