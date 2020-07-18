package com.lyf.scm.admin.remote.reverse.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.reverse.*;
import com.lyf.scm.admin.remote.reverse.ReverseRemoteService;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Slf4j
@Component
public class ReverseFacade {
    @Resource
    ReverseRemoteService reverseRemoteService;


    /**
      * @Description 分页查询冲销单
      * @author huwang
      * @Date 2020/7/17 16:08
      * @param queryReverseDTO
      * @return PageInfo<ReverseReponseDTO>
      **/
    public PageInfo<ReverseReponseDTO> queryReversePage(QueryReverseDTO queryReverseDTO) {
        Response<PageInfo<ReverseReponseDTO>> response = reverseRemoteService.queryReversePage(queryReverseDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * @Description 查询冲销单详情
     * @author huwang
     * @Date 2020/7/17 16:08
     * @param recordCode
     * @return ReverseDTO
     **/
    public ReverseDTO queryReverseDetail(String recordCode) {
        Response<ReverseDTO> response = reverseRemoteService.queryReverseDetail(recordCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * @Description 取消冲销单
     * @author huwang
     * @Date 2020/7/17 16:08
     * @param recordCodes
     * @return CancelReverseDTO
     **/
    public List<CancelReverseDTO> cancelReverse(List<String> recordCodes) {
        Response<List<CancelReverseDTO>> response = reverseRemoteService.cancelReverse(recordCodes);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据出库单据编码查询出库单(包含明细)
     * @param recordCode
     * @return
     */
    public ReverseDTO queryWarehouseRecordByRecordCode(String recordCode){
        Response<ReverseDTO> response = reverseRemoteService.queryWarehouseRecordByRecordCode(recordCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据入库单据编码查询收货单(包含明细)
     * @param recordCode
     * @return
     */
    public List<ReceiverRecordDTO> queryReceiverRecordByRecordCode(String recordCode){
        Response<List<ReceiverRecordDTO>> response = reverseRemoteService.queryReceiverRecordByRecordCode(recordCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * @Description 创建/编辑冲销单
     * @author huangyl
     * @Date 2020/7/18
     * @param reverseInfoDTO
     * @return String
     **/
    public String createReverse(ReverseInfoDTO reverseInfoDTO) {
        Response<String> response = reverseRemoteService.createReverse(reverseInfoDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }



    /**
     * @Description 确认冲销单
     * @author huangyl
     * @Date 2020/7/18
     * @param reverseConfirmFromPageDTO
     * @return ReverseConfirmedInfoDTO
     **/
    public List<ReverseConfirmedInfoDTO> confirmReverse(ReverseConfirmFromPageDTO reverseConfirmFromPageDTO) {
        Response<List<ReverseConfirmedInfoDTO>> response = reverseRemoteService.confirmReverse(reverseConfirmFromPageDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
