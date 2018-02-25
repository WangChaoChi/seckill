package wcc.seckill.exception;

/**
 * @author wangcc
 * @decription:
 * @date 2018/2/7 14:56
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
