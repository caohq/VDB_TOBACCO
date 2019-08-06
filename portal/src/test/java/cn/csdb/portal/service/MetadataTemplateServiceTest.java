package cn.csdb.portal.service;

import cn.csdb.portal.model.MetadataTemplate;
import cn.csdb.portal.repository.MetadataTemplateDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: DataSync
 * @description: metadataTemplate Service Test
 * @author: xiajl
 * @create: 2019-03-12 15:28
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-mvc.xml"})

public class MetadataTemplateServiceTest {
    @Resource
    private MetadataTemplateDao metadataTemplateDao;
    private Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);

    @Test
    public void testGetList(){
        String extField = "ext_";
        String extFieldName="共享";
        List<MetadataTemplate> list = metadataTemplateDao.getList(extField,extFieldName);
        logger.info(String.valueOf(list.size()));
        System.out.println(String.valueOf(list.size()));
    }

    @Test
    public void testFindAll(){
        List<MetadataTemplate> list = metadataTemplateDao.findAll();
        for (MetadataTemplate metadataTemplate : list)
        logger.info(metadataTemplate.getExtField() + "--->" +metadataTemplate.getExtFieldName());
    }
}
