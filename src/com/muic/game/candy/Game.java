package com.muic.game.candy;
//update this
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    private boolean running = false;
    private Thread thread;
    private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
    private BufferedImage background = null;
    BufferedImage fish = null;
    private int x = 0;
    private int[][] board = new int[7][6];
    Random rand = new Random();
    private boolean initRen = true;

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

    private void init(){
        BufferedImageLoader loader = new BufferedImageLoader();
        requestFocus();
        try{
            background = loader.loadImage("background.png");
            fish = loader.loadImage("red.png");
        }catch(Exception e){
            e.printStackTrace();
        }
        for(int i = 0;i < 7;i++){
            for(int j = 0;j < 6;j++){
                int  n = rand.nextInt(6) + 1;
                board[i][j] = n;
            }
        }
    }

    private void render() throws IOException {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        Candies candies = new Candies();
        g.drawImage(background, 0, 0, this);
        g.drawRect(32,57,85,75);
        candies.render(board, g, this);
        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        init();
        long lastTime = System.nanoTime();
        final double amoutOfTicks = 10.0;
        double ns = 1000000000 / amoutOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                tick();
                updates++;
                delta--;
            }
            try {
                render();
            } catch (IOException e) {
                e.printStackTrace();
            }
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + " Ticks,Fps " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {

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
