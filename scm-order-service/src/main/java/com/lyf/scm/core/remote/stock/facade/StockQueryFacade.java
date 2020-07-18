package com.lyf.scm.core.remote.stock.facade;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import com.lyf.scm.core.api.dto.reverse.ReceiverRecordDTO;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.api.dto.stockFront.BatchStockChangeFlowDTO;
import com.lyf.scm.core.remote.stock.StockQueryRemoteService;
import com.lyf.scm.core.remote.stock.dto.*;
import com.rome.arch.core.clientobject.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 通用库存查询
 * <p>
 * @Author: chuwenchao  2020/6/12
 */
@Slf4j
@Component
@AllArgsConstructor
public class StockQueryFacade {

    private final int PAGE_SIZE = 100;
    
    @Resource
    private StockQueryRemoteService stockQueryRemoteService;

    /**
     * 根据skuId集合、虚仓编码查询虚仓库存列表
     *
     * @param queryVirtualWarehouseStockDTO
     * @return
     */
    public List<VirtualWarehouseStock> queryVwStockByVwCode(QueryVirtualWarehouseStockDTO queryVirtualWarehouseStockDTO) {
        List<VirtualWarehouseStock> virtualWarehouseStockList = new ArrayList<VirtualWarehouseStock>();
        if (queryVirtualWarehouseStockDTO != null) {
            // 集合分割处理
            List<List<Long>> partList = ListUtils.partition(queryVirtualWarehouseStockDTO.getSkuIds(), PAGE_SIZE);
            Response<List<VirtualWarehouseStock>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<Long> subList = partList.get(i);
                queryVirtualWarehouseStockDTO.setSkuIds(subList);
                resp = stockQueryRemoteService.queryVwStockByVwCode(queryVirtualWarehouseStockDTO);
                ResponseValidateUtils.validResponse(resp);
                virtualWarehouseStockList.addAll(resp.getData());
            }
        }
        return virtualWarehouseStockList;
    }

    /**
     * 根据仓库和商品信息查询实仓库存
     *
     * @param realStock
     * @return
     */
    public List<SkuStockDTO> queryRealStockBySkuInfo(QueryRealStockDTO realStock) {
        List<SkuStockDTO> skuStockDTOS = new ArrayList<>();
        if (realStock != null) {
            // 集合分割处理
            List<List<BaseSkuInfoDTO>> partList = ListUtils.partition(realStock.getBaseSkuInfoDTOS(), PAGE_SIZE);
            Response<List<SkuStockDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<BaseSkuInfoDTO> subList = partList.get(i);
                realStock.setBaseSkuInfoDTOS(subList);
                resp = stockQueryRemoteService.queryRealStockBySkuInfo(realStock);
                ResponseValidateUtils.validResponse(resp);
                skuStockDTOS.addAll(resp.getData());
            }
        }
        return skuStockDTOS;
    }

    /**
     * 根据工程编码+仓库外部编码 查询库存信息
     */
    public List<RealWarehouseStockDTO> queryRealStockByFactoryAndRealWarehouseCode(String factoryCode, String realWarehouseCode) {
        List<RealWarehouseStockDTO> realWarehouseStockDTOList = new ArrayList<>();
        if (StringUtils.isNotEmpty(factoryCode) && StringUtils.isNotEmpty(realWarehouseCode)) {
            Response<List<RealWarehouseStockDTO>> resp = stockQueryRemoteService.queryRealStockByFactoryAndRealWarehouseCode(realWarehouseCode, factoryCode);
            ResponseValidateUtils.validResponse(resp);
            return resp.getData();
        }
        return realWarehouseStockDTOList;
    }

    /**
     * @param skuInfoList
     * @return
     * @Description: 批量虚仓库存查询接口 最大支持100，超过截取 <br>
     * @Author chuwenchao 2020/6/11
     */
    public List<SkuStockForVw> queryVmListByVmGroupCodes(List<SkuInfoForVw> skuInfoList) {
        List<SkuStockForVw> result = new ArrayList<>();
        List<List<SkuInfoForVw>> partList = ListUtils.partition(skuInfoList, PAGE_SIZE);
        for (int i = 0; i < partList.size(); i++) {
            Response<List<SkuStockForVw>> resp = stockQueryRemoteService.queryVmListByVmGroupCodes(partList.get(i));
            ResponseValidateUtils.validResponse(resp);
            result.addAll(resp.getData());
        }
        return result;
    }

    /**
     * 根据出入库单查询批次信息
     */
    public List<BatchStockChangeFlowDTO> queryBatchInfoByRecordCode(String recordCode) {
        List<BatchStockChangeFlowDTO> batchStockChangeFlowDTOList = new ArrayList<>();
        if (StringUtils.isNotEmpty(recordCode)) {
            Response<List<BatchStockChangeFlowDTO>> resp = stockQueryRemoteService.queryBatchInfoByRecordCode(recordCode);
            ResponseValidateUtils.validResponse(resp);
            batchStockChangeFlowDTOList = resp.getData();
        }
        return batchStockChangeFlowDTOList;
    }


    /**
     * 根据入库单据编码或收货单编码查询收货单(包含明细)
     * @param recordCode        入库单编码
     * @param wmsRecordCode     收货单编码
     * @return
     */
    public List<ReceiverRecordDTO> getReceiverRecordListByRecordCode(String recordCode, String wmsRecordCode){
        Response<List<ReceiverRecordDTO>> response = stockQueryRemoteService.getReceiverRecordListByRecordCode(recordCode, wmsRecordCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

}