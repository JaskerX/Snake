package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.entity.Entity;
import de.jaskerx.snake.entity.EntityManager;
import de.jaskerx.snake.entity.Text;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HighscoreRunnable implements Runnable {

    private final RunnableManager runnableManager;
    private final MainFrame mainFrame;
    private final int score;

    public HighscoreRunnable(RunnableManager runnableManager, MainFrame mainFrame, int score) {
        this.runnableManager = runnableManager;
        this.mainFrame = mainFrame;
        this.score = score;
    }

    @Override
    public void run() {
        BufferedImage bufferedImage = new BufferedImage(this.mainFrame.WIDTH_PX, this.mainFrame.HEIGHT_PX, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        EntityManager entityManager = this.mainFrame.getEntityManager();

        // Highscore-Daten laden
        List<Entity> toAdd = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:snake.db")) {

            // Tabelle erstellen
            try(PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS points ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'score' INTEGER)")) {
                preparedStatement.executeUpdate();
            }

            // Erreichte Punktzahl einfügen
            try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO points (score) VALUES (?)")) {
                preparedStatement.setInt(1, this.score);
                preparedStatement.executeUpdate();
            }

            // Top 5 Punktzahlen abfragen
            int i = 0;
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT score FROM points GROUP BY score ORDER BY score DESC LIMIT 5")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    try(PreparedStatement preparedStatementAmount = connection.prepareStatement("SELECT COUNT(*) FROM points WHERE score = ?")) {
                        preparedStatementAmount.setInt(1, resultSet.getInt("score"));
                        ResultSet resultSetAmount = preparedStatementAmount.executeQuery();
                        if(resultSetAmount.next()) {
                            Text text = new Text(250 + i * 40, i + 1 + ":   " + resultSet.getInt("score") + (resultSet.getInt("score") == 1 ? " Punkt   (" : " Punkte   (") + resultSetAmount.getInt(1) + " mal)", 30, this.mainFrame);
                            toAdd.add(text);
                            i++;
                        }
                    }
                }
            }

            // Die durchschnittliche Punktzahl (gerundet und genau) abfragen
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT ROUND(AVG(score)), AVG(score) FROM points")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    Text avgTitle = new Text(500 - (5 - i) * 40, "Durchschnittliche Punkte:", 37, this.mainFrame);
                    Text avgDisplay = new Text(550 - (5 - i) * 40, resultSet.getInt(1) + (resultSet.getInt(1) == 1 ? " Punkt (" : " Punkte (") + resultSet.getFloat(2) + ")", 30, this.mainFrame);
                    toAdd.add(avgTitle);
                    toAdd.add(avgDisplay);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ladebildschirm beenden
        this.runnableManager.stopLoadingRunnable();
        entityManager.getEntityGroups().clear();

        // Entities hinzufügen
        toAdd.add(new Text(80, "Du hast verloren! Drücke [Enter], um neu zu starten!", 30, this.mainFrame));
        toAdd.add(new Text(200, "Top 5 erreichte Punkte:", 37, this.mainFrame));
        for(Entity entity : toAdd) {
            entityManager.addEntity("highscore", entity);
        }

        // Neues Bild erstellen und als neues Bild setzen
        entityManager.drawEntities(graphics2D);
        this.mainFrame.setImage(bufferedImage);
    }

}
