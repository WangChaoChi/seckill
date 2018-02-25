package wcc.seckill.service;

import wcc.seckill.dto.Exposer;
import wcc.seckill.dto.SeckillExecution;
import wcc.seckill.entity.Seckill;
import wcc.seckill.exception.RepeatKillException;
import wcc.seckill.exception.SeckillCloseException;
import wcc.seckill.exception.SeckillException;

import java.util.List;

/**
 * @author wangcc
 * @decription: 站在“使用者”的角度设计接口
 * 三个方面：方法定义的粒度，参数，返回类型（return类型/异常）
 * @date 2018/2/7 13:47
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开始时输出秒杀接口地址
     * 否则输出秒杀时间和接口时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     *  @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
}
