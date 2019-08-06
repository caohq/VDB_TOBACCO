package cn.csdb.portal.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiajl on 2017/9/19.
 */
public class MyMappingExceptionResolver extends SimpleMappingExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(MyMappingExceptionResolver.class);

    public MyMappingExceptionResolver() {
        setWarnLogCategory(MyMappingExceptionResolver.class.getName());
    }

    @Override
    public String buildLogMessage(Exception e, HttpServletRequest req) {
        logger.error("内部错误", e);
        return "内部错误：" + e.getLocalizedMessage();
    }
}
