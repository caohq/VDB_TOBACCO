package cn.csdb.portal.service;

import cn.csdb.portal.model.MetadataTemplate;
import cn.csdb.portal.repository.MetadataTemplateDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: DataSync
 * @description: metadata template service
 * @author: xiajl
 * @create: 2019-03-08 15:17
 **/
@Service
public class MetadataTemplateService {

    @Resource
    private MetadataTemplateDao metadataTemplateDao;

    public  List<MetadataTemplate> getAll(){
        return metadataTemplateDao.getAll();
    }

    public void save(MetadataTemplate metadataTemplate){
        metadataTemplateDao.save(metadataTemplate);
    }

    public void delete(String id){
        metadataTemplateDao.delete(id);
    }

    public MetadataTemplate get(String id){
        return metadataTemplateDao.get(id);
    }

    //根据字段名、字段中文名称查询
    public  List<MetadataTemplate> getList(String extField, String extFieldName){

        return metadataTemplateDao.getList(extField,extFieldName);
    }


    public boolean isExist(String extField)
    {
        return metadataTemplateDao.exist(extField);
    }

    public Integer getMaxSortOrder()
    {
        return metadataTemplateDao.getMaxSortOrder();
    }
}
