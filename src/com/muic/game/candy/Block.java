package com.muic.game.candy;

public class Block {

    private int x;
    private int y;
    private int value;
    private int[] pos;
    private int VelX = 0;

    public Block(int x,int y,int value){
        pos = getPosition(x,y);
        this.x = pos[0];
        this.y = pos[1];
        this.value = value;
    }

    public void tick(){
        x+=VelX;
    }

    public void setVelX(int VelX){
        this.VelX = VelX;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int[] getPosition(int x,int y){
        int[] a = new int[2];
        if(x == 0){a[0] = 31;}
        if(x == 1){a[0] = 31+90;}
        if(x == 2){a[0] = 31+(90*2)-10;}
        if(x == 3){a[0] = 31+(90*3)-13;}
        if(x == 4){a[0] = 31+(90*4)-16;}
        if(x == 5){a[0] = 31+(90*5)-22;}

        if(y == 0){a[1] = 58;}
        if(y == 1){a[1] = 58+75;}
        if(y == 2){a[1] = 58+(75*2)-5;}
        if(y == 3){a[1] = 58+(75*3)-7;}
        if(y == 4){a[1] = 58+(75*4)-14;}
        if(y == 5){a[1] = 58+(75*5)-18;}
        if(y == 6){a[1] = 58+(75*6)-21;}
        return a;
    }

}
