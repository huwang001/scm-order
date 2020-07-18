package com.lyf.scm.core.service.reverse;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.reverse.*;
import java.util.List;

/**
 * @Description: 冲销单接口对象
 * <p>
 * @Author: wwh 2020/7/16
 */
public interface ReverseService {


    /**
     *
     * @Description 创建/编辑冲销单据
     * @author huangyl
     * @Date 2020/7/17
     * @param reverseInfoDTO
     * @return String
     **/
    String createReverse(ReverseInfoDTO reverseInfoDTO);

    /**
     * @Description 修改单据为确认状态
     * @author huangyl
     * @Date 2020/7/17
     * @param recordCode userId
     * @return void
     **/
    void updateReverseRecordStatusConfirmed(String recordCode,Long userId);

    /**
     * @Description 分页查询冲销单
     * @author huwang
     * @Date 2020/7/17 10:18
     * @param queryReverseDTO
     * @return com.github.pagehelper.PageInfo<com.lyf.scm.core.api.dto.reverse.ReverseReponseDTO>
     **/
    PageInfo<ReverseReponseDTO> queryReversePage(QueryReverseDTO queryReverseDTO);



    /**
     * 根据出库单据编码查询出库单(包含明细)
     * @param recordCode
     * @return
     */
    ReverseDTO queryWarehouseRecordByRecordCode(String recordCode);

    /**
     * 根据入库单据编码查询收货单(包含明细)
     * @param recordCode        单据编码
     * @param wmsRecordCode     收货单编码
     * @return
     */
    List<ReceiverRecordDTO> queryReceiverRecordByRecordCode(String recordCode, String wmsRecordCode);

    /**
     * @Description 查看冲销单详情
     * @author huwang
     * @Date 2020/7/17 11:58
     * @param recordCode
     * @return com.lyf.scm.core.api.dto.reverse.ReverseReponseDTO
     **/
    ReverseDTO queryReverseDetail(String recordCode);

    /**
      * @Description 取消冲销单
      * @author huwang
      * @Date 2020/7/17 14:04
      * @param recordCode
      * @return void
      **/
    void cancelReverse(String recordCode);

    /**
     * 冲销过账回调
     * @param recordCode    单据编号
     * @param voucherCode   SAP凭证号
     * @return
     */
    Boolean reverseSapNotify(String recordCode, String voucherCode);
}