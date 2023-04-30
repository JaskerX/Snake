package de.jaskerx.snake.runnable;

import de.jaskerx.snake.MainFrame;
import de.jaskerx.snake.SnakeObject;

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
        this.scheduledExecutorGame.scheduleAtFixedRate(new GameRunnable(this.mainFrame, snakeObject, this), 0, 15, TimeUnit.MILLISECONDS);
    }

    public void startLoadingRunnable() {
        if(this.scheduledExecutorLoading != null) {
            this.scheduledExecutorLoading.shutdownNow();
        }
        this.scheduledExecutorLoading = Executors.newSingleThreadScheduledExecutor();
        this.scheduledExecutorLoading.scheduleAtFixedRate(new LoadingRunnable(this.mainFrame), 0, 10, TimeUnit.MILLISECONDS);
    }

    public void stopGameRunnable() {
        this.scheduledExecutorGame.shutdownNow();
    }

    public void stopLoadingRunnable() {
        this.scheduledExecutorLoading.shutdownNow();
    }

}
