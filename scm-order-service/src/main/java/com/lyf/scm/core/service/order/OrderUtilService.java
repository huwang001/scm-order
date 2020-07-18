package com.lyf.scm.core.service.order;

/**
 * @Description: 全局订单号服务 <br>
 *
 * @Author chuwenchao 2020/6/12
 */
public interface OrderUtilService {

    /**
     * @Description: 获取订单号（带日期） <br>
     *
     * @Author chuwenchao 2020/7/2
     * @param prefix
     * @return
     */
    String queryOrderCode(String prefix);

    /**
     * @Description: 雪花算法获取订单序列号 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param prefix
     * @return
     */
    String queryOrderCodeBySnow(String prefix);

    String queryOrderCodeByDate(String prefix);

}
