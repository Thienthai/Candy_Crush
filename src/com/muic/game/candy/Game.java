package com.muic.game.candy;
//update this
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable,MouseListener{

    private boolean running = false;
    private int change = 5;
    private Thread thread;
    private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
    private BufferedImage background = null;
    BufferedImage fish = null;
    private int x = 0;
    private Block[][] board = new Block[7][6];
    Random rand = new Random();
    BufferedImageLoader loader = new BufferedImageLoader();
    private boolean initRen = true;
    private BufferedImage red = loader.loadImage("red.png");
    private BufferedImage yellow = loader.loadImage("yellow.png");
    private BufferedImage blue = loader.loadImage("blue.png");
    private BufferedImage brown = loader.loadImage("brown.png");
    private BufferedImage green = loader.loadImage("green.png");
    private BufferedImage purple = loader.loadImage("purple.png");
    private int getMouseXpos = -1;
    private int getMouseYpos = -1;
    private int isSwitch = 0;
    private boolean getDes = true;
    private boolean getOri = true;
    private Block switchOrigin = null;
    private Block getSwitchDes = null;
    private boolean switchSelect = false;

    public Game() throws IOException {
        addMouseListener(this);
    }

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
                Block b = new Block(j,i,n);
                board[i][j] = b;
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
//        Candies candies = new Candies();
        g.drawImage(background, 0, 0, this);
//        g.drawRect(32,57,85,75);
//        candies.render(board, g, this);
        for(int i = 0;i<7;i++){
            for(int j = 0;j<6;j++){
                Block b = board[i][j];
                if(b.getValue() == 1){
                    setPosition(g,b,red);
                    //g.drawImage(red,b.getX(),b.getY(),this);
                }
                if(b.getValue() == 2){
                    setPosition(g,b,blue);
                    //g.drawImage(blue,b.getX(),b.getY(),this);
                }
                if(b.getValue() == 3){
                    setPosition(g,b,green);
                    //g.drawImage(green,b.getX(),b.getY(),this);
                }
                if(b.getValue() == 4){
                    setPosition(g,b,yellow);
                    //g.drawImage(yellow,b.getX(),b.getY(),this);
                }
                if(b.getValue() == 5){
                    setPosition(g,b,brown);
                    //g.drawImage(brown,b.getX(),b.getY(),this);
                }
                if(b.getValue() == 6){
                    setPosition(g,b,purple);
                    //g.drawImage(purple,b.getX(),b.getY(),this);
                }
            }
        }
//        if(switchOrigin != null && getSwitchDes != null){
//            SwitchRender sr = new SwitchRender();
//            sr.switchFunc(switchOrigin,getSwitchDes,board);
//        }
        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        init();
        //Block b = board[0][0];
        long lastTime = System.nanoTime();
        final double amoutOfTicks = 60.0;
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
                //b.setVelX(1);
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
        if(isSwitch != 0) {
            if(isSwitch == 1) {
                switchOrigin = board[getMouseYpos][getMouseXpos];
            }else if(isSwitch == 2){
                getSwitchDes = board[getMouseYpos][getMouseXpos];
                System.out.println("get error " + getSwitchDes.getX());
                switchOrigin.setSlidepoint(new Point(getSwitchDes.getX(),getSwitchDes.getY()));
                getSwitchDes.setSlidepoint(new Point(switchOrigin.getX(),getSwitchDes.getY()));
//                //switchSelect = true;
                switchOrigin.setSwitchtrig(true);
                getSwitchDes.setSwitchtrig(true);
//                System.out.println("switch triggered");
                isSwitch = 0;
            }
        }

        for(int i = 0;i < 7;i++){
            for(int j = 0;j < 6;j++){
                board[i][j].tick();
            }
        }
        //if(sw){
            //System.out.println("come here");
//            switchOrigin.setSlidepoint(getSwitchDes.getSlidepoint().y,getSwitchDes.getSlidepoint().x);
//            getSwitchDes.setSlidepoint(switchOrigin.getSlidepoint().y,getSwitchDes.getSlidepoint().x);
//            switchOrigin = null;
//            getSwitchDes = null;
//            isSwitch = false;
        //}
    }

    public static void main(String args[]) throws IOException {
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


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        System.out.println(e.getX()+" "+e.getY());
        if(34 <= e.getX() && e.getX() <= 118){
            getMouseXpos = 0;
        }else if(121 <= e.getX() && e.getX() <= 201){
            getMouseXpos = 1;
        }else if(206 <= e.getX() && e.getX() <= 287){
            getMouseXpos = 2;
        }else if(291<= e.getX() && e.getX() <= 372){
            getMouseXpos = 3;
        }else if(377 <= e.getX() && e.getX() <= 455){
            getMouseXpos = 4;
        }else if(464 <= e.getX() && e.getX() <= 541){
            getMouseXpos = 5;
        }

        if(61 <= e.getY() && e.getY() <= 131){
            getMouseYpos = 0;
        }else if(137 <= e.getY() && e.getY() <= 204){
            getMouseYpos = 1;
        }else if(208 <= e.getY() && e.getY() <= 273){
            getMouseYpos = 2;
        }else if(281 <= e.getY() && e.getY() <= 346){
            getMouseYpos = 3;
        }else if(350 <= e.getY() && e.getY() <= 415){
            getMouseYpos = 4;
        }else if(420 <= e.getY() && e.getY() <= 486){
            getMouseYpos = 5;
        }else if(491 <= e.getY() && e.getY() <= 561){
            getMouseYpos = 6;
        }
        isSwitch += 1;
        System.out.println(getMouseXpos + " " + getMouseYpos);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setPosition(Graphics g,Block b,BufferedImage img){
        if(!b.isSwitchtrig){
            g.drawImage(img,b.getX(),b.getY(),this);
        }else{
            //System.out.println("switch trg = " + b.getSlidepoint().x + " " + b.getSlidepoint().y);
            Point des = b.getSlidepoint();
            if(b.getX() > des.x){
                if(b.getX() - 5 < des.x){
                    b.setVelX(b.getX() - des.x);
                    b.setSwitchtrig(false);
                }else {
                    //System.out.println("come here");
                    b.setVelX(-5);
                    //b.setVelY(-1);
                    g.drawImage(img, b.getX(), b.getY(), this);
                }
            }else if(b.getX() > des.x){
                if(b.getX() - 5 < des.x){
                    b.setVelX(b.getX() - des.x);
                    b.setSwitchtrig(false);
                }else {
                    //System.out.println("come here");
                    b.setVelX(-5);
                    //b.setVelY(1);
                    g.drawImage(img, b.getX(), b.getY(), this);
                }
            }else if(b.getX() < des.x){
                if(b.getX() + 5 > des.x){
                    b.setVelX(des.x - b.getX());
                    b.setSwitchtrig(false);
                }else {
                    //System.out.println("come here");
                    b.setVelX(5);
                    ///b.setVelY(1);
                    g.drawImage(img, b.getX(), b.getY(), this);
                }
            }else if(b.getX() < des.x){
                if(b.getX() + 5 > des.x){
                    b.setVelX(des.x - b.getX());
                    b.setSwitchtrig(false);
                }else {
                    b.setVelX(5);
                    //b.setVelY(-1);
                    g.drawImage(img, b.getX(), b.getY(), this);
                }
            }else if(b.getX() == des.x){
                //System.out.println("come here");
                b.setVelX(0);
                b.setVelY(0);
                b.setSwitchtrig(false);
            }
        }
    }
}
