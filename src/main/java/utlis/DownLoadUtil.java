package utlis;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @author BeamStark
 * @date 2023-03-10-23:30
 */
@Slf4j
public class DownLoadUtil {

    static String filePath = "/";

    @SneakyThrows
    public static void downloadImages(String videoAddress, String desc) {
        int byteRead;
        URL url = new URL(videoAddress);
        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        File fileSavePath = new File(filePath + desc + ".jpg");
        File fileParent = fileSavePath.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        if (fileSavePath.exists()) {
            fileSavePath.delete();
        }
        FileOutputStream fs = new FileOutputStream(fileSavePath);
        byte[] buffer = new byte[1024];
        while ((byteRead = inStream.read(buffer)) != -1) {
            fs.write(buffer, 0, byteRead);
        }
        inStream.close();
        fs.close();
    }
}
