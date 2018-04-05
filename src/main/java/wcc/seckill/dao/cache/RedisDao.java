package wcc.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import wcc.seckill.entity.Seckill;

/**
 * @author wangcc
 * @decription:
 * @date 2018/4/1 14:49
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    //得到Seckill的schema
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(Long seckillId) {
        //redis操作逻辑
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckillId;
            //并没有实现内部序列化操作
            //get ->byte[] -> 反序列化 -> Object(Seckill)
            //采用自定义序列化
            //protostuff : pojo
            byte[] bytes = jedis.get(key.getBytes());
            //缓存中获取到
            if (bytes != null) {
                //空对象
                Seckill seckill = schema.newMessage();
                //seckill被序列化
                ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                return seckill;
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {

        //put Object(Seckill) -> 序列化（byte[]）
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckill.getSeckillId();

            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //超时缓存
            int timeOut = 60 * 60;//一小时（单位是秒）
            String result = jedis.setex(key.getBytes(), timeOut, bytes);

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
