package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.render.entity.Entity;
import de.jaskerx.snake.render.RenderablesManager;
import de.jaskerx.snake.render.entity.Text;

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
        RenderablesManager renderablesManager = this.mainFrame.getRenderablesManager();

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
                            Text text = new Text(250 + i * 40, resultSet.getInt("score") + (resultSet.getInt("score") == 1 ? " Punkt   (" : " Punkte   (") + resultSetAmount.getInt(1) + " mal)", 30, this.mainFrame);
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
        renderablesManager.getRenderablesGroups().clear();

        // Entities hinzufügen
        toAdd.add(new Text(80, "Du hast verloren! Drücke [Enter], um neu zu starten!", 30, this.mainFrame));
        toAdd.add(new Text(200, "Top 5 erreichte Punkte:", 37, this.mainFrame));
        for(Entity entity : toAdd) {
            renderablesManager.addRenderable("highscore", entity);
        }
    }

}
