package wcc.seckill.exception;

/**
 * @author wangcc
 * @decription: 重复秒杀异常（运行期异常）
 * @date 2018/2/7 14:50
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
