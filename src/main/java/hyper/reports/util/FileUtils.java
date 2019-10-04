package hyper.reports.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static String readFile(File file) throws IOException {
        String data = null;
        if (file.exists()) {
            data = new String(Files.readAllBytes(file.toPath()));
        }
        return data;
    }

    public static String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        return readFile(file);
    }

    public static void writeFile(String content, String fileName) throws IOException {
        new File(fileName).createNewFile();
        byte[] strToBytes = content.getBytes();
        Path path = Paths.get(fileName);
        Files.write(path, strToBytes);
    }

}
