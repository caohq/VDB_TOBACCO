package cn.csdb.portal.service;

import cn.csdb.portal.model.ThemesGallery;
import cn.csdb.portal.repository.ThemeDao;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ThemeService {
    @Resource
    private ThemeDao themeDao;

    public List<ThemesGallery> selectAllThemesGallery(String themeName,int pageNo,int pageSize){
        return themeDao.selectAllThemesGallery(themeName,pageNo,pageSize);
    }
    public int countThemeList(String themeName){
        return themeDao.countThemeList(themeName);
    }

    public JSONObject addTheme(ThemesGallery themesGallery){
       return themeDao.addTheme(themesGallery);
    }

    public ThemesGallery findById(String id){
        return themeDao.findById(id);
    }

    public JSONObject updateThemesGalleryById(ThemesGallery themesGallery){
        return themeDao.updateThemesGalleryById(themesGallery);
    }

    public List<ThemesGallery> getAll(){
        return themeDao.getAll();
    }

    public JSONObject deleteThemeById(String id){
       return themeDao.deleteThemeById(id);
    }
}
