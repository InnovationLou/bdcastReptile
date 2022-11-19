package org.innovation.dybroadcastParser;

import org.innovation.dybroadcastParser.catcher.DanmuCatcher;
import org.innovation.dybroadcastParser.catcher.ProductCatcher;

import java.io.IOException;

public class Executor {


    public static void main(String[] args) {
        try {
            ProductCatcher.testProduct();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
