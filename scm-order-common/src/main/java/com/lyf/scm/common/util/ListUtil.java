package com.lyf.scm.common.util;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.AlikAssert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: List工具类
 * <p>
 * @Author: chuwenchao  2019/12/27
 */
public class ListUtil {

    /**
     * @Description: 获取list最大页数 <br>
     *
     * @Author chuwenchao 2020/1/2
     * @param list
     * @return 
     */
    public static int getPageNum(List list, int pageSize) {
        AlikAssert.isTrue(pageSize > 0, ResCode.ORDER_ERROR_1002, "pageSize不能小于等于0");
        if(CollectionUtils.isEmpty(list)) {
            return 0;
        }
        int total = list.size();
        int maxPage = total % pageSize == 0 ? total/pageSize : total/pageSize + 1;
        return maxPage;
    }

    /**
     * @Description: 按页获取List <br>
     *
     * @Author chuwenchao 2019/12/27
     * @param list
     * @param page 从1开始
     * @param pageSize
     * @return 
     */
    public static List getPageList(List list, int page, int pageSize) {
        List pageList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)) {
            return pageList;
        }
        int total = list.size();
        int maxPage = total % pageSize == 0 ? total/pageSize : total/pageSize + 1;
        if(page > maxPage) {
            return pageList;
        }
        if(page*pageSize > total) {
            pageList = list.subList((page - 1)*pageSize, total);
        } else {
            pageList = list.subList((page - 1)*pageSize, page*pageSize);
        }
        return pageList;
    }


}
