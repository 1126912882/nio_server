package nio.message;

import java.io.Serializable;

/**
 * Created by 吴樟 on www.haixiangzhene.xyz
 * 2018/1/27.
 */
public abstract class Message implements Serializable {

    private Type type;

    public enum Type{
        PING,CONTENT,START,END
    }

    public Message(Type type) {
        this.type = type;
    }
}
