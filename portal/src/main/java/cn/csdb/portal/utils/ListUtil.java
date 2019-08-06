package cn.csdb.portal.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: DataSync
 * @description: list utils
 * @author: xiajl
 * @create: 2018-11-27 16:02
 **/
public class ListUtil {

    public static List<String> addDataToList(List<String> plainList){
        List<String> result = new ArrayList<String>();
        if(plainList.size() == 0)
        {
            result = null;
        }
        else if (plainList.size() == 1){
            result.add(plainList.get(0)) ;
        }else if (plainList.size() == 2){
            result.add(plainList.get(0)) ;
            result.add(plainList.get(1)) ;
        }
        else
        {
            result.add(plainList.get(0)) ;
            for (int i=1;i<=plainList.size()-2;i++)
            {
                result.add(plainList.get(i)) ;
            }
            result.add(plainList.get(plainList.size() -1 )) ;
        }
        return result;
    }

    public static List<String> transFormList(List<String> list){
        List<String> newList = new ArrayList<String>();
        for (String tmp : list){
            String ss =tmp.replace("\"","").replace("[","").replace("]","");
            newList.add(ss);
        }
        List<String> result = new ArrayList<String>();
        result = addDataToList(newList);
        return result;
    }

}
