package com.gossip;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}
