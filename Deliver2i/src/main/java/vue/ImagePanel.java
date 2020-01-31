package vue;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JLayeredPane {

    private BufferedImage image;

    public ImagePanel(String name) {
        try {
            image = ImageIO.read(new File(name));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(VueSolution.BG_COLOR);
        g.fillRect(0,0,getWidth(),getHeight());
        g.drawImage(image, 0, 0, this);
    }

}