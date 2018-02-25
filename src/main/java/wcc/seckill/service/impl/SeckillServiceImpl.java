package wcc.seckill.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import wcc.seckill.dao.SeckillDao;
import wcc.seckill.dao.SuccessKilledDao;
import wcc.seckill.dto.Exposer;
import wcc.seckill.dto.SeckillExecution;
import wcc.seckill.entity.Seckill;
import wcc.seckill.entity.SuccessKilled;
import wcc.seckill.enums.SeckillStateEnum;
import wcc.seckill.exception.RepeatKillException;
import wcc.seckill.exception.SeckillCloseException;
import wcc.seckill.exception.SeckillException;
import wcc.seckill.service.SeckillService;

import java.util.Date;
import java.util.List;

/**
 * @author wangcc
 * @decription:
 * @date 2018/2/7 15:28
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;
    //nd5盐值定义；用于混淆MD5加密
    private final String slat = "seek32&^((jkljdfl-((0d++_(@1kljfUo&%^$ikjfl";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前时间
        Date now = new Date();
        //秒杀已经结束或未开启
        if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
        }

        //转化特定字符串过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "|" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    /*
    * 使用注解控制事务方法的优点：
    * 1：开发团队达成一致约定，明确标注事务方法的编程风格。
    * 2：保证事务方法执行的时间经可能短，不要穿插其他网络操作（RPC/HTTP请求）或者剥离到事务方法外部
    * 3：不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
    * */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        try {
            if (md5 == null || !md5.equals(getMD5(seckillId))) {
                throw new SeckillException("seckill data rewrite");
            }
            //执行秒杀操作 ：减库存、记录购买记录
            Date now = new Date();

            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId, now);
            if (updateCount <= 0) {
                //没有更新到记录，秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                //唯一：seckillId userPhone
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }

            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
            //所有的编译期异常 转化为运行期异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }
}
