package cn.csdb.portal.service;

import cn.csdb.portal.model.MetaTemplate;
import cn.csdb.portal.repository.MetaTemplateDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: DataExplore
 * @description: metaTemplate Service
 * @author: xiajl
 * @create: 2019-04-24 10:21
 **/
@Service
public class MetaTemplateService {
    @Resource
    private MetaTemplateDao metaTemplateDao;

    /**
     * 增加保存元数据模板
     * @param metaTemplate
     */
    public void add(MetaTemplate metaTemplate){
        metaTemplateDao.add(metaTemplate);
    }

    /**
     * 根据id获取元数据模板实体信息
     * @param id
     * @return
     */
    public MetaTemplate get(String id){
        return metaTemplateDao.get(id);
    }

    /**
     * 根据元数据模板名称获取元数据模板实体信息
     * @param name
     * @return
     */
    public MetaTemplate findByName(String name){
        return metaTemplateDao.findByName(name);
    }

    /**
     * 获取指定subjectCode中所有的元数据模板信息
     * @param subjectCode
     * @return
     */
    public List<MetaTemplate> getList(String subjectCode){
        return metaTemplateDao.getList(subjectCode);
    }


    public List<MetaTemplate> getListByPage(String subjectCode, int pageNo, int pageSize){
        List<MetaTemplate> list = metaTemplateDao.getListByPage(subjectCode,(pageNo-1)*pageSize,pageSize);
        return list;
    }

    public long countByPage(String subjectCode){
        return metaTemplateDao.countByPage(subjectCode);
    }

    public List<MetaTemplate> getAllList(String subjectCode) {
        return metaTemplateDao.getAllList(subjectCode);
    }

    public void deleteById(String id){
        metaTemplateDao.deleteById(id);
    }

}
