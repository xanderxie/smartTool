package tool;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FileUtils {


    public static List<File> dicFiles(String path) {
        return dicFiles(path, null);
    }

    public static List<File> dicFiles(String path, Predicate<String> nameFilter) {
        File file = new File(path);
        if (file.isFile()) {
            List<File> files = new ArrayList<>();
            files.add(file);
            return files;
        }
        File[] fileArray;
        if (nameFilter == null) {
            fileArray = file.listFiles();
        } else {
            fileArray = file.listFiles(((dir, name) -> nameFilter.test(name)));
        }
        return fileArray == null ? new ArrayList<>() : Arrays.asList(fileArray);
    }


    public static boolean writeFile(String path, String filename, String content) {
        File file = new File(path + File.separator + filename);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
            return true;
        } catch (IOException e) {
            return false;
        }

    }


    public static Document readXml2document(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        SAXReader reader = new SAXReader();
        try {
            return reader.read(filename);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document readXml2document(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        SAXReader reader = new SAXReader();
        try {
            return reader.read(file);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
