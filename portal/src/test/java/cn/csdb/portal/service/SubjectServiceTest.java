/*
package cn.csdb.portal.service;

import cn.csdb.portal.model.Subject;
import cn.csdb.portal.repository.SubjectDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

*/
/**
 * @program: DataSync
 * @description: a test class for subjectservice
 * @author: xiajl
 * @create: 2018-10-22 16:03
 **//*

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-mvc.xml"})

public class SubjectServiceTest {
    private Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);

    @Resource
    private SubjectDao subjectDao;

    @Test
    public void test(){
        String subjectCode = "AA01";
        Subject subject = subjectDao.findBySubjectCode(subjectCode);
        logger.info("\n\n"+ subject.getSubjectName() + ":" + subject.getBrief());
    }

    */
/**
     * Function Description: 
     *
     * @param:  测试保存subject对像
     * @return: 
     * @auther: Administrator
     * @date:   2018/10/25 16:27
     *//*

    
    @Test
    public void save(){
        Subject subject = new Subject();
        subject.setSubjectName("bbbxxx");
        subject.setSubjectCode("AB002");
        subjectDao.save(subject);
    }
}
*/
