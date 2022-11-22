package org.innovation.dybroadcastParser.dispatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Innovation
 */
public class PipeStream extends Thread{
    InputStream is;
    OutputStream os;

    public PipeStream(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {
        byte[] buffer=new byte[1024];
        int len;
        try {
            while ((len=is.read(buffer))>=0){
                os.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
