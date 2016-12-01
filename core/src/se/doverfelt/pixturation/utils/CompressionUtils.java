package se.doverfelt.pixturation.utils;

import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by rickard on 2016-12-01.
 */
public class CompressionUtils {

    public static String toGzBase64(String s) throws IOException {
        StringReader reader = new StringReader(s);
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(byteout);
        for (int read = reader.read(); read != -1; read = reader.read()) {
            gzip.write(read);
        }
        gzip.close();
        char[] chars = Base64Coder.encode(byteout.toByteArray());
        StringBuilder builder = new StringBuilder();
        builder.append(chars);
        return builder.toString();
    }

    public static String fromGzBase64(String s) throws IOException {
        ByteArrayInputStream bytes = new ByteArrayInputStream(Base64Coder.decode(s));
        GZIPInputStream gzip = new GZIPInputStream(bytes);
        StringBuilder builder = new StringBuilder();
        while (gzip.available() > 0) {
            builder.append((char) gzip.read());
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

}
