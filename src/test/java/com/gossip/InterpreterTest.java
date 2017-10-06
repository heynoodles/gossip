package com.gossip;

import com.gossip.util.GossipException;
import com.gossip.util.Util;

import java.io.IOException;
import java.util.List;

/**
 * @author gaoxin.wei
 */
public class InterpreterTest {

    public static void main(String[] args) throws GossipException, IOException {
        new Interpreter("tests/cons.gossip").interp();
//        test_all();
    }

    private static void test_all() throws IOException, GossipException {
        List<String> filePaths = Util.getFilePaths("tests");
        for (String filePath : filePaths) {
            new Interpreter(filePath)
                .interp();
        }
    }

}
