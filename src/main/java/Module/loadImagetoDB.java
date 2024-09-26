package Module;

import java.io.FileInputStream;
import java.sql.PreparedStatement;

public class loadImagetoDB {
    // load image into user profile in database (avatar)
    public void loadImg(String path, int id) {
        try {
            FileInputStream fis = new FileInputStream(path);
            PreparedStatement ps = ExampleDAO.getConnection().prepareStatement("update users set avatar=? where id=?");
            ps.setBinaryStream(1, fis, fis.available());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new loadImagetoDB().loadImg("src/main/image/Lam.jpg", 6);
//update Users set avatar = load_file('D:/LocoQuiz/src/main/image/Hoan.jpg') where id = 1;
        new loadImagetoDB().loadImg("src/main/image/Hoan.jpg", 1);
        new loadImagetoDB().loadImg("src/main/image/Huy.jpg", 2);
        new loadImagetoDB().loadImg("src/main/image/Nam.jpg", 3);
        new loadImagetoDB().loadImg("src/main/image/Dang.jpg", 4);
        new loadImagetoDB().loadImg("src/main/image/Dat.jpg", 5);
    }
}