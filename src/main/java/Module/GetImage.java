package Module;

import java.io.File;
import javax.swing.*;

public class GetImage {
    public static byte[] getImage(String path) {
        byte[] image = null;
        try {
            File file = new File(path);
            image = new byte[(int) file.length()];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
//test and plot the image
    public static void main(String[] args) {
        String path = "\"D:\\CameraAI\\Lam.jpg\"";
        byte[] image = getImage(path);
        System.out.println(image);
    }
}
