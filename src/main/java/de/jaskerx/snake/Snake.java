package de.jaskerx.snake;

import de.jaskerx.snake.runnable.RunnableManager;

public class Snake {

    public static boolean debug = false;

    public static void main(String[] args) {
        for(String arg : args) {
            if (arg.equalsIgnoreCase("debug")) {
                debug = true;
                break;
            }
        }
        MainFrame mainFrame = new MainFrame();
        RunnableManager runnableManager = new RunnableManager(mainFrame);

        mainFrame.startNewGame(runnableManager);
    }

}
