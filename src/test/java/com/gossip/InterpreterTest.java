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

        List<String> filePaths = Util.getFilePaths("tests");
        for (String filePath : filePaths) {
            new Interpreter(filePath)
                .interp();
        }
//
//
//
//        List<String> files = new ArrayList<>();
//        files.add("tests/test.gossip");
//        files.add("tests/test1.gossip");
//        files.add("tests/test2.gossip");
//
//        new Interpreter("tests/test.gossip")
//            .interp();
//
//        new Interpreter("tests/test1.gossip")
//            .interp();
//
//        new Interpreter("tests/test2.gossip")
//            .interp();
//
//        new Interpreter("tests/cons.gossip")
//            .interp();
//
//        new Interpreter("tests/fibonacci.gossip")
//            .interp();
    }



}
