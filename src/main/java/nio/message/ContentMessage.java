package nio.message;

/**
 * Created by 吴樟 on www.haixiangzhene.xyz
 * 2018/1/27.
 */
public class ContentMessage extends Message {

    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public ContentMessage(Type type) {
        super(type);
    }
}
