package cn.csdb.portal.repository;

import cn.csdb.portal.model.Group;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.ThemesGallery;
import com.alibaba.fastjson.JSONObject;
import com.ctc.wstx.util.StringUtil;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class ThemeDao {

    @Resource
    private MongoTemplate mongoTemplate;

    /** 
    * @Description: 主题库列表--烟草
    * @Param: [] 
    * @return: java.util.List<cn.csdb.portal.model.ThemesGallery> 
    * @Author: zcy
    * @Date: 2019/8/7 
    */ 
    public List<ThemesGallery> selectAllThemesGallery(String themeName,int pageNo,int pageSize){
        QueryBuilder queryBuilder=new QueryBuilder();
        if(StringUtils.isNotEmpty(themeName)){
            queryBuilder=queryBuilder.and("themeName").regex(Pattern.compile("^.*"+themeName+".*$"));
        }
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip((pageNo-1)*pageSize );
        basicQuery.limit(pageSize);
        return mongoTemplate.find(basicQuery, ThemesGallery.class);

    }

    public int countThemeList(String themeName){
        QueryBuilder queryBuilder=new QueryBuilder();
        if(StringUtils.isNotEmpty(themeName)){
            queryBuilder=queryBuilder.and("themeName").regex(Pattern.compile("^.*"+themeName+".*$"));
        }
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        List<ThemesGallery> list=mongoTemplate.find(basicQuery,ThemesGallery.class);
        return list.size();
    }

/**
* @Description: 新增主题库
* @Param: [themesGallery]
* @return: com.alibaba.fastjson.JSONObject
* @Author: zcy
* @Date: 2019/8/7
*/
    public JSONObject addTheme(ThemesGallery themesGallery){
//        判断themeCode是否重复
       List<ThemesGallery> list=mongoTemplate.find(new Query(Criteria.where("themeCode").is(themesGallery.getThemeCode())),ThemesGallery.class);
       JSONObject jsonObject=new JSONObject();
        if(list.size()==0){
//            windows测试路径,部署时注释
            String path="G:\\"+themesGallery.getThemeCode();
//            String path="/home/"+themesGallery.getThemeCode();
            themesGallery.setFilePath(path);
            mongoTemplate.insert(themesGallery);

//            创建主题库文件夹，/home/themecode
            File f1=new File(path);
            if(!f1.exists())
            {
                f1.mkdirs();
            }
            jsonObject.put("result", "ok");

        }else{
            jsonObject.put("result", "exist");
        }
return jsonObject;
    }

/**
* @Description: 根据id查找主题库
* @Param: [id]
* @return: cn.csdb.portal.model.ThemesGallery
* @Author: zcy
* @Date: 2019/8/7
*/
    public ThemesGallery findById(String id){
        return mongoTemplate.findById(id,ThemesGallery.class);
    }

    /** 
    * @Description: 根据id修改主题库信息
    * @Param: [themesGallery] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/8 
    */ 
    public JSONObject updateThemesGalleryById(ThemesGallery themesGallery){
        JSONObject jsonObject=new JSONObject();
//        判断themeCode是否重复
        List<ThemesGallery> list=mongoTemplate.find(new Query(Criteria.where("themeName").is(themesGallery.getThemeName())),ThemesGallery.class);
        if(list.size()==0){
            Update update = Update.update("id", themesGallery.getId()).set("themeName", themesGallery.getThemeName())
                                                                      .set("themeCode",themesGallery.getThemeCode())
                                                                      .set("describe",themesGallery.getDescribe())
                                                                      .set("createTime",themesGallery.getCreateTime());
            mongoTemplate.updateFirst(new Query(Criteria.where("id").is(themesGallery.getId())),update,ThemesGallery.class);
            jsonObject.put("result", "ok");
        }else{
            jsonObject.put("result", "exist");
        }

        return jsonObject;
    }

    public List<ThemesGallery> getAll(){
        return mongoTemplate.findAll(ThemesGallery.class);
    }

    /** 
    * @Description: 根据id删除主题库 
    * @Param: [id] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/8 
    */ 
    public JSONObject deleteThemeById(String id){
        JSONObject jsonObject=new JSONObject();
        ThemesGallery themesGallery=mongoTemplate.findOne(new Query(Criteria.where("id").is(id)),ThemesGallery.class);
        List<Subject> list=mongoTemplate.find(new Query(Criteria.where("themeCode").is(themesGallery.getThemeCode())),Subject.class);
        if(list.size()==0){
//            删除文件夹
            File file=new File(themesGallery.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            mongoTemplate.remove(new Query(Criteria.where("id").is(id)),ThemesGallery.class);
            jsonObject.put("result","ok");
        }else{
            jsonObject.put("result","no");
        }
        return jsonObject;
    }
}
