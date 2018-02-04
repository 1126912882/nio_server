package quorum.util;

import nio.message.PingMessage;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by 吴樟 on www.haixiangzhene.xyz
 * 2018/1/27.
 */
public class ObjectUtil {


    public static void main(String[] args) throws IOException {
        ObjectUtil.writeObject(new PingMessage());
    }

    public static byte[] writeObject(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            byte[] bt = byteArrayOutputStream.toByteArray();
            return bt;
        }catch (IOException e){
            e.printStackTrace();
            throw new IOException("could not let object to byte");
        }finally {
            if (byteArrayOutputStream!=null){
                byteArrayOutputStream.close();
            }
            if (objectOutputStream!=null){
                objectOutputStream.close();
            }
        }
    }



}
