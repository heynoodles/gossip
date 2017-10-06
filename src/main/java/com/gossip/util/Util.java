package com.gossip.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxin.wei
 */
public class Util {

    public static String readFile(String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return Charset.forName("UTF-8").decode(ByteBuffer.wrap(encoded)).toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static String unifyPath(String filename) {
        return unifyPath(new File(filename));
    }

    public static String unifyPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (Exception e) {
            return "";
        }
    }

    public static List<String> getFilePaths(String filepath) throws IOException {
        List<String> result = new ArrayList<>();
        File file = new File(unifyPath(filepath));

        String[] fileList = file.list();
        for (int i = 0; i < fileList.length; i++) {
            File curFile = new File(filepath + "/" + fileList[i]);
            result.add(curFile.getCanonicalPath());
        }

        return result;
    }
}
