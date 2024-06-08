package com.inugami.game.states;

import com.inugami.game.utils.KeyHandler;
import com.inugami.game.utils.MouseHandler;

import java.awt.*;

public class PlayState extends GameState {

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }
    public void update() {

    }
    public void input(MouseHandler mouse, KeyHandler key) {

    }
    public void render(Graphics2D g) {
        g.setColor(Color.RED);
//Tiles testing
//        for (int i = 0; i < 1920; i+=32)
//            for (int j = 0; j < 1080; j += 32) {
//                g.drawRect(i, j, 32, 32);
//            }

        g.drawRect(0, 0, 32, 32);
    }
}
