package com.lyf.scm.core.remote.stock.facade;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.CheckVirWarehouseCodeEnum;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.remote.stock.StockDisparityRemoteService;
import com.lyf.scm.core.remote.stock.dto.BatchCreateRecordDTO;
import com.lyf.scm.core.remote.stock.dto.BatchResultDTO;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.service.disparity.impl.DisparityBeanUtil;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StockDisparityRecordFacade
 * @Description 库存差异外观
 * @Author Lin.Xu
 * @Date 2020/7/17 15:38
 * @Version 1.0.0
 */
@Slf4j
@Component
public class StockDisparityRecordFacade {

    @Resource
    private StockDisparityRemoteService stockDisparityRemoteService;

    /**
     * @Description 门店确认收货批量拒收
     * @Author Lin.Xu
     * @Date 15:50 2020/7/17
     * @Param [putInNos]
     * @return java.util.List<com.lyf.scm.core.remote.stock.dto.BatchResultDTO>
     **/
    public List<BatchResultDTO> rejectShopReceipt(List<String> putInNos){
        Response<List<BatchResultDTO>> backResp = stockDisparityRemoteService.rejectShopReceipt(putInNos);
        ResponseValidateUtils.validResponse(backResp);
        return backResp.getData();
    }


    
    /**
     * @Description 差异处理:批量创建出入库单
     * @Author Lin.Xu
     * @Date 15:42 2020/7/17
     * @Param [putInRecords, outRecords]
     **/
    public void createOutRecord(List<WarehouseRecordE> putInRecords, List<WarehouseRecordE> outRecords, Map<String, String> outOrderNoMap){
        BatchCreateRecordDTO batchCreateRecordDTO = new BatchCreateRecordDTO();
        if(putInRecords.size() > 0) {
        	List<InWarehouseRecordDTO> inList = DisparityBeanUtil.convertPutInStockWarehouseRs(putInRecords, outOrderNoMap);
        	inList.forEach(e -> {
        		//校验出库单虚仓编码是否可设置
        		CheckVirWarehouseCodeEnum.checkVirWarehouseCode(e.getVirWarehouseCode(), e.getRecordType());
        	});
            batchCreateRecordDTO.setInList(inList);

        }
        if(outRecords.size() > 0){
        	List<OutWarehouseRecordDTO> outList = DisparityBeanUtil.convertOutStockWarehouseRs(outRecords);
        	outList.forEach(e -> {
        		//校验出库单虚仓编码是否可设置
        		CheckVirWarehouseCodeEnum.checkVirWarehouseCode(e.getVirWarehouseCode(), e.getRecordType());
        	});
            batchCreateRecordDTO.setOutList(outList);
        }
        //发起调用
        log.warn("发起过账推库存参数：" + JSONObject.toJSONString(batchCreateRecordDTO));
        Response stockResp = stockDisparityRemoteService.createOutRecord(batchCreateRecordDTO);
        if(null == stockResp || !"0".equals(stockResp.getCode())){
            String errorMsg = null == stockResp ? "推送库存中心'差异处理:批量创建出入库单'失败" : stockResp.getMsg().toString();
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC + ":" + errorMsg);
        }
    }

}
