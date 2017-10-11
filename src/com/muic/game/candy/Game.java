package com.muic.game.candy;

import javax.swing.*;
import java.awt.*;

public class Game extends Canvas implements Runnable {

    private boolean running = false;
    private Thread thread;

    private synchronized void start(){
        if(running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if(!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    @Override
    public void run() {

    }

    public static void main(String args[]){
        Game game = new Game();
        game.setPreferredSize(new Dimension(700,600));
        game.setMaximumSize(new Dimension(700,600));
        game.setMinimumSize(new Dimension(700,600));

        JFrame frame = new JFrame();
        frame.setTitle("Candy Crush");
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }


}
