package com.lyf.scm.admin.remote.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.pack.*;
import com.lyf.scm.admin.remote.DemandRemoteService;
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
public class DemandFacade {
    @Resource
    DemandRemoteService demandRemoteService;

    /**
     * 页面创建需求单
     *
     * @param * @param truckingOrderDTO
     * @return java.lang.String
     * @author Lucky
     * @date 2020/7/7  21:13
     */
    public String createDemand(DemandDTO demandDTO) {
        Response<String> response = demandRemoteService.createDemand(demandDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 导入创建需求单
     *
     * @param * @param truckingOrderDTO
     * @return java.lang.String
     * @author Lucky
     * @date 2020/7/7  21:13
     */
    public void batchCreateDemand(List<DemandBatchDTO> demandBatchDTOList, Long userId) {
        Response response = demandRemoteService.batchCreateDemand(demandBatchDTOList, userId);
        ResponseValidateUtils.validResponse(response);
    }

    /**
     * 功能描述
     *
     * @param * @param recordCode
     * @return com.lyf.scm.admin.dto.pack.DemandDetailAndComponentDTO
     * @author Lucky
     * @date 2020/7/8  14:28
     */
    public DemandDetailAndComponentDTO queryDemandDetailAndComponent(String recordCode) {
        Response<DemandDetailAndComponentDTO> response = demandRemoteService.queryDemandDetailAndComponent(recordCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();

    }

    public PageInfo<PackDemandResponseDTO> queryPackDemandPage(QueryPackDemandDTO condition) {
        Response<PageInfo<PackDemandResponseDTO>> response = demandRemoteService.queryPackDemandPage(condition);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public PackDemandResponseDTO queryPackDemandDetail(String recordCode) {
        Response<PackDemandResponseDTO> response = demandRemoteService.queryPackDemandDetail(recordCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 需求单确认状态，并下发包装系统
     * @param demandConfirmFromPageDTO
     */
    public List<DemandConfirmedInfoDTO> updatePackDemandStatus(DemandConfirmFromPageDTO demandConfirmFromPageDTO) {
        Response<List<DemandConfirmedInfoDTO>>  response = demandRemoteService.demandRecordStatusConfirmed(demandConfirmFromPageDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 批量取消需求单
     *
     * @param recordCodes
     * @return
     */
    public List<DemandCancelResponseDTO> batchCancelDemand(List<String> recordCodes) {
        Response<List<DemandCancelResponseDTO>> response = demandRemoteService.batchCancelDemand(recordCodes);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 商品信息展示
     * @param skuParamDTO
     * @return
     */
    public PageInfo<SkuAttributeInfo> pageSkuInfo(SkuParamDTO skuParamDTO){
        Response<PageInfo<SkuAttributeInfo>> response = demandRemoteService.pageSkuInfo(skuParamDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询商品信息及组合品子品信息
     * @param param
     * @return
     */
    public SkuAndCombineDTO queryCombinesBySkuCodeAndUnitCode(List<ParamExtDTO> param,Integer packType){
        Response<SkuAndCombineDTO> resp =demandRemoteService.queryCombinesBySkuCodeAndUnitCode(param,packType);
        ResponseValidateUtils.validResponse(resp);
        return resp.getData();
    }

    /**
     * 查询待导出数据
     * @param condition
     * @return
     */
    public List<PackDemandResponseDTO> queryPackDemandExport(QueryPackDemandDTO condition) {
        Response<List<PackDemandResponseDTO>> response = demandRemoteService.queryPackDemandExport(condition);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public PageInfo<PackDemandResponseDTO> queryPackDemandList(QueryPackDemandDTO condition) {
        Response<PageInfo<PackDemandResponseDTO>> response = demandRemoteService.queryPackDemandList(condition);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询商品类型
     * @return
     */
    public List<SkuTypeDTO> skuType(){
        Response<List<SkuTypeDTO>> response = demandRemoteService.skuType();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

}
