package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;
    private float opacity = 0.5f; // Set the opacity level (0.0 to 1.0)

    public BackgroundPanel() {
        try {
            // Load the image as a resource from the JAR
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/GUI/luffy.jpg"));
            if (backgroundImage == null) {
                throw new IOException("Image not found: /GUI/luffy.jpg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
