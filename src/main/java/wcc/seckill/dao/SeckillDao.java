package wcc.seckill.dao;

import org.apache.ibatis.annotations.Param;
import wcc.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangcc
 * @decription:
 * @date 2018/1/28 17:49
 */
public interface SeckillDao {

    /**减库存
     * @param seckillId
     * @param killTime
     * @return 如果返回的值>1 则表示更新的记录条数
     */
    int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime") Date killTime);

    /**根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);

}
