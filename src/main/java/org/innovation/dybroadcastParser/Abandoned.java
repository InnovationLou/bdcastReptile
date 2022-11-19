package org.innovation.dybroadcastParser;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.io.IOUtils;
import org.innovation.dybroadcastParser.proto.DanmuvoWSS;
import org.innovation.dybroadcastParser.proto.WSS;
import org.innovation.dybroadcastParser.util.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class Abandoned {

    public static void main(String[] args) throws IOException {
        InputStream in=null;
        try {
            String rootPath = System.getProperty("user.dir");
            String filename="test.bin";
            File dir = new File(rootPath+"\\src\\main\\resources\\data\\");
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    // 这里需要将 参数dir,name构造为 File之后再判断是否为目录
                    if (!new File(dir,name).isDirectory() && name.matches("webcast3-ws-web-lf.douyin.com_.*_server.bin")) {
                        return true;
                    }
                    return false;
                }
            });
            Iterator<File> iterator = Arrays.stream(files).iterator();
            while (iterator.hasNext()){
                System.out.println("解析----"+iterator.next().getName());
                in = Files.newInputStream(Paths.get(iterator.next().getAbsolutePath()));
                //            byte[] data = webSocketFrame.binary();
                byte[] data = IOUtils.toByteArray(in);
                //最外层解析 try catch 不指定ws路径也可以，解析失败不会崩溃
                //proto解析外层
                WSS.WssResponse wss = WSS.WssResponse.parseFrom(data);
//                System.out.println(wss);
                //GZIP解压data数据
                byte[] uncompress = Utils.uncompress(wss.getData());

                //解析data
                DanmuvoWSS.Response response = DanmuvoWSS.Response.parseFrom(uncompress);
                List<DanmuvoWSS.Message> messagesList = response.getMessagesList();
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
//                System.out.println(messagesList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            in.close();
        }
    }
}
