package cn.csdb.portal.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

/**
 * @program: DataSync
 * @description:
 * @author: huangwei
 * @create: 2018-10-15 10:57
 **/
@Service
public class ConfigPropertyService {
    private Properties portalProperties;

    @PostConstruct
    private void initWac() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/jdbc.properties");
        portalProperties = PropertiesLoaderUtils.loadProperties(classPathResource);
    }

    public String getProperty(String propertyName){
        return portalProperties.getProperty(propertyName).toString();
    }
}
