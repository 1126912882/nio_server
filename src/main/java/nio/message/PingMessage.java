package nio.message;

import java.io.Serializable;

/**
 * Created by 吴樟 on www.haixiangzhene.xyz
 * 2018/1/27.
 */
public class PingMessage extends Message {


    public PingMessage() {
        super(Type.PING);
    }
}
