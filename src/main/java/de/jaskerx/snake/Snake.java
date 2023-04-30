package de.jaskerx.snake;

import de.jaskerx.snake.runnable.RunnableManager;

public class Snake {

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        RunnableManager runnableManager = new RunnableManager(mainFrame);

        mainFrame.startNewGame(runnableManager);
    }

}
