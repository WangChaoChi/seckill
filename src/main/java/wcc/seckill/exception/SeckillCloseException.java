package wcc.seckill.exception;

/**
 * @author wangcc
 * @decription: 秒杀已关闭的异常
 * @date 2018/2/7 14:53
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
