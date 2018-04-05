package wcc.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wcc.seckill.dto.Exposer;
import wcc.seckill.dto.SeckillExecution;
import wcc.seckill.entity.Seckill;
import wcc.seckill.exception.RepeatKillException;
import wcc.seckill.exception.SeckillCloseException;
import wcc.seckill.exception.SeckillException;
import wcc.seckill.service.SeckillService;

import java.util.List;


/**
 * @author wangcc
 * @decription:
 * @date 2018/2/21 12:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info("list={}", seckills);
    }

    @Test
    public void getById() throws Exception {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckillId{}",seckill);
    }


    //完整的测试逻辑
    @Test
    public void testSeckillLogic() throws Exception {
        Exposer exposer = seckillService.exportSeckillUrl(1003);
        if (exposer.isExposed()) {
            logger.info("exposer={}",exposer);
            long id = 1003;
            long phone = 15785954275L;
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, exposer.getMd5());
                logger.info("result={}",seckillExecution);
            } catch (RepeatKillException e) {
                logger.info(e.getMessage());
            }catch (SeckillCloseException e) {
                logger.info(e.getMessage());
            }
        }else {
            logger.warn("exposer={}",exposer);
        }

    }

    @Test
    public void executeSeckillProcedure(){
        long seckillId = 1001;
        long phone = 14425253647L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution=seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());

        }

    }

}