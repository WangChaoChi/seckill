package wcc.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wcc.seckill.entity.Seckill;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wangcc
 * @decription:
 * @date 2018/1/28 22:45
 */

/*配置spring和junit整合
    spring-test,junit
 */

//junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao 实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount="+updateCount);

    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getSeckillName());
        System.out.println(seckill);
    }


    /*
    * Caused by: org.apache.ibatis.binding.BindingException:
    * Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
    * */
    //List<Seckill> queryAll(int offset,int limit);
    //java没有保存形参的记录：queryAll(int offset,int limit)——>queryAll(arg0,arg1)
    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        System.out.println(seckills);
    }

}