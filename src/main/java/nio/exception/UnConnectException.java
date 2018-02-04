package nio.exception;

/**
 * Created by 吴樟 on www.haixiangzhene.xyz
 * 2018/1/27.
 */
public class UnConnectException extends java.lang.Exception {

    public UnConnectException(String message) {
        super(message);
    }

    public UnConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
