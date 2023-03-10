package org.innovation.dybroadcastParser;

import com.google.protobuf.InvalidProtocolBufferException;
import org.innovation.dybroadcastParser.proto.DanmuvoWSS;
import org.innovation.dybroadcastParser.proto.WSS;
import org.innovation.dybroadcastParser.util.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class OfflineExecutor {
    public static void main(String[] args) {

        File file=new File(System.getProperty("user.dir")+"/data/res.bin");

        final byte[] data = readFile(file);
        try {
            //最外层解析 try catch 不指定ws路径也可以，解析失败不会崩溃
            //proto解析外层
            WSS.WssResponse wss = WSS.WssResponse.parseFrom(data);
//                                System.out.println(wss);
            //GZIP解压data数据
            final byte[] uncompress = Utils.uncompress(wss.getData());
            //解析data
            DanmuvoWSS.Response response = DanmuvoWSS.Response.parseFrom(uncompress);
            final List<DanmuvoWSS.Message> messagesList = response.getMessagesList();
//                                System.out.println(messagesList);
            //根据message区分message类型解析
            messagesList.forEach(item->{
                if ("WebcastChatMessage".equals(item.getMethod())) {
                    try {
                        DanmuvoWSS.ChatMessage chatMessage = DanmuvoWSS.ChatMessage.parseFrom(item.getPayload());
                        System.out.println("[弹幕消息]" + chatMessage.getUser().getNickname() + "(" + chatMessage.getUser().getId() + ")" + "说：" + chatMessage.getContent()  +"\n");
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            });
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
    public static byte[] readFile(File sourceFile) {

        if (sourceFile.isFile() &&sourceFile.exists()) {
            long fileLength = sourceFile.length();
            if (fileLength !=0) {
                try {
                    BufferedInputStream fis = new BufferedInputStream(
                            new FileInputStream(sourceFile));
                    byte[] b = new byte[(int) fileLength];

                    while (fis.read(b) != -1) {
                    }

                    fis.close();
                    fis = null;

                    return b;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }
}
