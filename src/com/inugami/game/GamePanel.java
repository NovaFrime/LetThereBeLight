package com.inugami.game;

import com.inugami.game.states.GameStateManager;
import com.inugami.game.utils.KeyHandler;
import com.inugami.game.utils.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    private final int width;
    private final int height;

    private BufferedImage image;
    private Graphics2D g;

    private Thread thread;
    private boolean running = false;

    private MouseHandler mouse;
    private KeyHandler key;

    private GameStateManager gsm;

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    public void init() {
        running = true;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();

        mouse = new MouseHandler();
        key = new KeyHandler();

        gsm = new GameStateManager();
    }

    public void run() {
        init();
        final double GSUC = 1000000000.0; // Game State Update Constant
        final double GAME_HERTZ = 60.0;
        final double TBU = GSUC / GAME_HERTZ; // Time before update

        final int MUBR = 5; // Most update before render

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;

        final double TARGET_FPS = 60.0;
        final double TTBR = GSUC / TARGET_FPS; // Total Time before render
        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdateTime / GSUC);
        int oldFrameCount = 0;

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;
            while ((now - lastUpdateTime > TBU) && (updateCount < MUBR)) {
                update();
                input(mouse,key);
                lastUpdateTime += TBU;
                updateCount++;
            }
            if (now - lastUpdateTime > TBU) {
                lastUpdateTime = now - TBU;
            }

            input(mouse,key);
            render();
            repaint();

            lastRenderTime = now;
            frameCount++;

            int thisSecond = (int) (lastUpdateTime / GSUC);
            if (thisSecond > lastSecondTime) {
                if (frameCount != oldFrameCount) {
                    System.out.println("New sec:" + thisSecond + " " + frameCount);
                    oldFrameCount = frameCount;
                }
                frameCount = 0;
                lastSecondTime = thisSecond;
            }
            while ((now - lastRenderTime < TTBR) && (now - lastUpdateTime < TBU)) {
                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                now = System.nanoTime();
            }
        }
    }

    public void update() {
        // Update game state
        gsm.update();
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        // Handle input
        gsm.input(mouse, key);
    }

    public void render() {
        if (g != null) {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, width, height);
            gsm.render(g);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }
    }
}
