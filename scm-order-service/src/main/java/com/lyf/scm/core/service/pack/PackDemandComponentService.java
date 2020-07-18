package com.lyf.scm.core.service.pack;

import java.util.List;

import com.lyf.scm.core.api.dto.notify.StockNotifyDetailDTO;
import com.lyf.scm.core.api.dto.pack.DemandAllotDTO;
import com.lyf.scm.core.api.dto.pack.PackDemandComponentDTO;
import com.lyf.scm.core.domain.entity.pack.PackDemandComponentE;

/**
 * @Description: 包装需求单明细原料接口
 * <p>
 * @Author: wwh 2020/7/7
 */
public interface PackDemandComponentService {

    /**
     * 根据需求编码查询需求单明细原料列表
     * @Author wuyuanhang 2020/7/8
     * @param recordCode
     * @return
     */
    List<PackDemandComponentDTO> queryDemandComponentByRecordCode(String recordCode);

    /**
     * 调拨时根据需求编码查询需求单明细原料列表
     * @Author wuyuanhang 2020/7/9
     * @param recordCode
     * @return
     */
    List<PackDemandComponentDTO> queryDemandComponentByRecordCodeAllot(String recordCode);

    /**
     * 根据需求单明细原料创建调拨单
     *
     *	@author WWH 2020/7/8
     * @param demandAllotDTO
     */
    void createDemandAllot(DemandAllotDTO demandAllotDTO);
    
    /**
     * 统计需求单明细原料已锁定数量
     * 
     * @author WWH 2020/7/8
     * @param packDemandComponentEList
     */
    void countLockqty(List<PackDemandComponentE> packDemandComponentEList);
    

    /**
     * 批量保存原料明细
     *
     * @param packDemandComponentDTOList
     * @param userId
     * @return void
     * @author Lucky
     * @date 2020/7/7  15:36
     */
    void batchSavePackDemandComponent(List<PackDemandComponentDTO> packDemandComponentDTOList, Long userId);

    /**
     * 调拨出库通知
     * 
     * @author WWH 2020/7/9
     * @param detailList
     */
	void allotOutNotify(List<StockNotifyDetailDTO> detailList);

	/**
	 * 同步需求单领料状态给包装系统
	 * 
	 * @param requireCode
	 * @param pickStaus
	 */
	void asyncPickStatus(String requireCode, Integer pickStaus);

}