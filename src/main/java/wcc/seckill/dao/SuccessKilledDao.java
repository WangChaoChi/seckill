package wcc.seckill.dao;

import org.apache.ibatis.annotations.Param;
import wcc.seckill.entity.SuccessKilled;

/**
 * @author wangcc
 * @decription:
 * @date 2018/1/28 17:56
 */
public interface SuccessKilledDao {

    /**插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    /**根据id查询successKilled 并携带秒杀产品对象
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

}
