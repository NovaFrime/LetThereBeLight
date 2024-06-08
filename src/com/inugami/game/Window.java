package com.inugami.game;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window() {
        setTitle("Let There Be Light");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setContentPane(new GamePanel(1920,1080));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
