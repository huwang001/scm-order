package com.lyf.scm.admin.remote.stockFront.facade;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.stockFront.WhAllocationRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.RealWarehouseParamDTO;
import com.lyf.scm.admin.remote.stockFront.dto.RealWarehouseStockDTO;
import com.lyf.scm.admin.remote.stockFront.dto.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WhAllocationDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WhAllocationPageDTO;
import com.lyf.scm.admin.remote.template.WhAllocationExportTemplate;
import com.lyf.scm.admin.remote.template.WhAllocationTemplateDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import com.rome.user.common.utils.UserContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 类WhAllocationFacade的实现描述：门店调拨
 *
 * @author sunyj 2019/5/22 11:57
 */
@Component
@Slf4j
public class WhAllocationFacade {

    @Autowired
    private WhAllocationRemoteService whAllocationRemoteService;

    /**
     * 运营平台查询列表功能
     *
     * @param frontRecord
     * @return
     */
    public PageInfo<WhAllocationPageDTO> queryList(WhAllocationPageDTO frontRecord) {
        Response<PageInfo<WhAllocationPageDTO>> response = whAllocationRemoteService.queryList(frontRecord);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 保存仓库调拨前置单
     *
     * @param dto
     */
    public void saveWhAllocation(WhAllocationDTO dto) {
        Response response = whAllocationRemoteService.saveWhAllocation(dto);
        ResponseValidateUtils.validResponse(response);
    }

    /**
     * 修改仓库调拨前置单
     *
     * @param dto
     */
    public void updateWhAllocation(WhAllocationDTO dto) {
        Response response = whAllocationRemoteService.updateWhAllocation(dto);
        ResponseValidateUtils.validResponse(response);

    }

    /**
     * 更加调拨单id查询（预）下市商品code集合
     *
     * @param id
     */
    public List<String> queryMarketDownSku(Long id) {
        Response<List<String>> response = whAllocationRemoteService.queryMarketDownSku(id);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 确认调拨申请
     *
     * @param id
     */
    public String confimWhAllocation(Long id, Long userId) {
        Response response = whAllocationRemoteService.confimWhAllocation(id, userId);
        ResponseValidateUtils.validResponse(response);
        return (String) response.getData();
    }

    /**
     * 取消调拨申请
     *
     * @param id
     */
    public void cancleWhAllocation(Long id, Long userId, Integer isForceCancle) {
        Response response = whAllocationRemoteService.cancleWhAllocation(id, userId, isForceCancle);
        ResponseValidateUtils.validResponse(response);
    }

    /**
     * 查询分配比例
     *
     * @param recordId
     * @return
     */
    public WhAllocationPageDTO queryAllocConfigInfo(Long recordId) {
        Response<WhAllocationPageDTO> response = whAllocationRemoteService.queryAllocConfigInfo(recordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询分配比例
     *
     * @param recordId
     * @return
     */
    public WhAllocationPageDTO queryOutAllocConfigInfo(Long recordId) {
        Response<WhAllocationPageDTO> response = whAllocationRemoteService.queryOutAllocConfigInfo(recordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 配置单据级别的sku实仓虚仓分配比例
     *
     * @param config
     */
    public void allotConfig(List<RecordRealVirtualStockSyncRelationDTO> config) {
        Long userId = UserContext.getUserId();
        if (!CollectionUtils.isEmpty(config) && userId != null) {
            config.forEach(dto -> {
                dto.setCreator(userId);
                dto.setModifier(userId);
                // allotType=1按比例分配 2按绝对值分配
                if (Integer.valueOf(2).equals(dto.getAllotType())) {
                    dto.setConfigSyncRate(dto.getConfigAbsolute());
                }
            });
        }
        Response response = whAllocationRemoteService.allotConfig(config);
        ResponseValidateUtils.validResponse(response);
    }

    /**
     * 初始化新增页面
     *
     * @return
     */
    public WhAllocationPageDTO initAddPage() {
        Response<WhAllocationPageDTO> response = whAllocationRemoteService.initAddPage();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据仓库ID查询实仓库存
     *
     * @param dto
     * @return
     */
    public PageInfo<RealWarehouseStockDTO> queryStockByWhIdForWhAllot(RealWarehouseStockDTO dto) {
        Response<PageInfo<RealWarehouseStockDTO>> response = whAllocationRemoteService.queryStockByWhIdForWhAllot(dto);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 初始化编辑页面
     *
     * @return
     */
    public WhAllocationPageDTO initEditPage(Long id) {
        Response<WhAllocationPageDTO> response = whAllocationRemoteService.initEditPage(id);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 导入excel文件
     *
     * @param file
     * @return
     */
    public void importFile(List<WhAllocationTemplateDTO> dataList, Long userId) {
        Response response = whAllocationRemoteService.importWhAllocation(dataList, userId);
        ResponseValidateUtils.validResponse(response);

    }

    /**
     * 差异创建
     *
     * @param id
     * @param userId
     */
    public String createDisparityAllot(Long id, Long userId) {
        Response response = whAllocationRemoteService.createDisparityAllot(id, userId);
        ResponseValidateUtils.validResponse(response);
        return (String) response.getData();
    }

    /**
     * 运营平台导出功能
     *
     * @param frontRecord
     * @return
     */
    public List<WhAllocationExportTemplate> exportWhallot(WhAllocationPageDTO frontRecord) {
        Response<List<WhAllocationExportTemplate>> response = whAllocationRemoteService.exportWhallot(frontRecord);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public List<WhAllocationPageDTO> queryAllocConfigInfoByRecords(List<Long> recordIds) {
        Response<List<WhAllocationPageDTO>> response = whAllocationRemoteService
                .queryAllocConfigInfoByRecords(recordIds);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public List<WhAllocationPageDTO> queryOutAllocConfigInfoByRecords(List<Long> recordIds) {
        Response<List<WhAllocationPageDTO>> response = whAllocationRemoteService
                .queryOutAllocConfigInfoByRecords(recordIds);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据条件查询实仓信息-运营平台查询接口,不分页
     *
     * @param paramDTO
     * @return
     */
    public List<RealWarehouse> queryForAdmin(RealWarehouseParamDTO paramDTO) {
        Response<List<RealWarehouse>> response = whAllocationRemoteService.queryForAdmin(paramDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

}
