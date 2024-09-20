package Module;

import java.io.File;
import javax.swing.*;

public class GetImage {
//   use swing to plot the image
    public static void plotImage(String path) {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(path);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        String path = "/D:/CameraAI/Lam.jpg";
        File file = new File(path);
        if (file.exists()) {
            plotImage(path);
        } else {
            System.out.println("File not found!");
        }
    }
}
