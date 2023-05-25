package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.Snake;
import de.jaskerx.snake.render.SnakeObject;

import java.util.concurrent.*;

public class RunnableManager {

    private final MainFrame mainFrame;
    private ScheduledExecutorService scheduledExecutorGame;
    private ScheduledExecutorService scheduledExecutorLoading;

    public RunnableManager(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void startGameRunnable(SnakeObject snakeObject) {
        if(this.scheduledExecutorGame != null) {
            this.scheduledExecutorGame.shutdownNow();
        }
        this.scheduledExecutorGame = Executors.newSingleThreadScheduledExecutor();
        long periodDefault = 7;
        long period = (long) (Snake.debug ? (periodDefault / 0.5) : periodDefault);
        if(System.getProperty("speed") != null) {
            period = (long) (periodDefault / Double.parseDouble(System.getProperty("speed")));
        }
        this.scheduledExecutorGame.scheduleAtFixedRate(new GameRunnable(this.mainFrame, snakeObject, this), 0, period, TimeUnit.MILLISECONDS);
    }

    public void startLoadingRunnable() {
        if(this.scheduledExecutorLoading != null) {
            this.scheduledExecutorLoading.shutdownNow();
        }
        this.scheduledExecutorLoading = Executors.newSingleThreadScheduledExecutor();
        this.scheduledExecutorLoading.scheduleAtFixedRate(new LoadingRunnable("Statistiken werden geladen", this.mainFrame), 0, 5, TimeUnit.MILLISECONDS);
    }

    public void stopGameRunnable() {
        this.scheduledExecutorGame.shutdownNow();
    }

    public void stopLoadingRunnable() {
        this.scheduledExecutorLoading.shutdownNow();
    }

}
