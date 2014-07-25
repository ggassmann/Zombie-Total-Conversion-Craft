package net.gigimoi.zombietc.helpers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class WebHelper {
    public static void download_zip_file(URL from, String file_to) {
        try {
            URLConnection conn = from.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("content-type", "binary/data");
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(file_to + "tmp.zip");

            byte[] b = new byte[1024];
            int count;

            while ((count = in.read(b)) > 0) {
                out.write(b, 0, count);
            }
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
