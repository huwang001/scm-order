package com.lyf.scm.admin.remote.stockFront;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.stockFront.dto.RealWarehouseParamDTO;
import com.lyf.scm.admin.remote.stockFront.dto.RealWarehouseStockDTO;
import com.lyf.scm.admin.remote.stockFront.dto.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WhAllocationDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WhAllocationPageDTO;
import com.lyf.scm.admin.remote.template.WhAllocationExportTemplate;
import com.lyf.scm.admin.remote.template.WhAllocationTemplateDTO;
import com.rome.arch.core.clientobject.Response;

/**
 * 类WhAllocationService的实现描述：仓库调拨
 *
 * @author sunyj 2019/5/13 11:01
 */
@FeignClient(value = "scm-order-service")
public interface WhAllocationRemoteService {

    /**
     * 保存仓库调拨前置单
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/addWhAllocationApply", method = RequestMethod.POST)
    Response saveWhAllocation(WhAllocationDTO dto);

    /**
     * 保存仓库调拨前置单
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/updateWhAllocationApply", method = RequestMethod.POST)
    Response updateWhAllocation(WhAllocationDTO dto);

    @RequestMapping(value = "/order/v1/wh_allocation/queryMarketDownSku", method = RequestMethod.POST)
    Response queryMarketDownSku(@RequestParam("id") Long id);

    /**
     * 确认调拨申请
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/confimWhAllocation", method = RequestMethod.POST)
    Response confimWhAllocation(@RequestParam(value = "id") Long id, @RequestParam(value = "userId") Long userId);

    /**
     * 取消调拨申请
     *
     * @param id
     * @param userId
     * @param isForceCancle
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/cancleWhAllocation", method = RequestMethod.POST)
    Response cancleWhAllocation(@RequestParam(value = "id") Long id, @RequestParam(value = "userId") Long userId,
                                @RequestParam(value = "isForceCancle") Integer isForceCancle);

    /**
     * 运营平台查询列表功能
     *
     * @param frontRecord
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/listPage", method = RequestMethod.POST)
    Response<PageInfo<WhAllocationPageDTO>> queryList(WhAllocationPageDTO frontRecord);

    /**
     * 查询入库分配比例
     *
     * @param recordId
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/queryAllocConfigInfo/{recordId}", method = RequestMethod.GET)
    Response<WhAllocationPageDTO> queryAllocConfigInfo(@PathVariable(value = "recordId") Long recordId);

    /**
     * 查询出库分配比例
     *
     * @param recordId
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/queryOutAllocConfigInfo/{recordId}", method = RequestMethod.GET)
    Response<WhAllocationPageDTO> queryOutAllocConfigInfo(@PathVariable(value = "recordId") Long recordId);

    /**
     * 初始化add页面
     *
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/initAddPage", method = RequestMethod.GET)
    Response<WhAllocationPageDTO> initAddPage();

    /**
     * 根据仓库ID查询实仓库存
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/queryStockByWhIdForWhAllot", method = RequestMethod.POST)
    Response<PageInfo<RealWarehouseStockDTO>> queryStockByWhIdForWhAllot(@RequestBody RealWarehouseStockDTO dto);

    /**
     * 配置单据级别的sku实仓虚仓分配比例
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/allotConfig", method = RequestMethod.POST)
    Response allotConfig(@RequestBody List<RecordRealVirtualStockSyncRelationDTO> config);

    /**
     * 初始化编辑页面
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/initEditPage", method = RequestMethod.GET)
    Response<WhAllocationPageDTO> initEditPage(@RequestParam("id") Long id);

    /**
     * 导入调拨单
     *
     * @param dataList
     * @param userId
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/importWhAllocation", method = RequestMethod.POST)
    Response importWhAllocation(@RequestBody List<WhAllocationTemplateDTO> dataList,
                                @RequestParam("userId") Long userId);

    @RequestMapping(value = "/order/v1/wh_allocation/createDisparityAllot", method = RequestMethod.POST)
    Response createDisparityAllot(@RequestParam("id") Long id, @RequestParam("userId") Long userId);

    /**
     * 运营平台导出功能
     *
     * @param frontRecord
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/exportWhallot", method = RequestMethod.POST)
    Response<List<WhAllocationExportTemplate>> exportWhallot(WhAllocationPageDTO frontRecord);

    @RequestMapping(value = "/order/v1/wh_allocation/queryAllocConfigInfoByRecords", method = RequestMethod.POST)
    Response<List<WhAllocationPageDTO>> queryAllocConfigInfoByRecords(@RequestBody List<Long> recordIds);

    @RequestMapping(value = "/order/v1/wh_allocation/queryOutAllocConfigInfoByRecords", method = RequestMethod.POST)
    Response<List<WhAllocationPageDTO>> queryOutAllocConfigInfoByRecords(@RequestBody List<Long> recordIds);

    @RequestMapping(value = "/order/v1/wh_allocation/queryForAdmin", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryForAdmin(@RequestBody RealWarehouseParamDTO dto);

}
