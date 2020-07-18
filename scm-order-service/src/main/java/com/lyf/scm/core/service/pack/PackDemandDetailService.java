package com.lyf.scm.core.service.pack;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.pack.PackDemandDetailDTO;
import com.lyf.scm.core.api.dto.pack.SkuAndCombineDTO;
import com.lyf.scm.core.api.dto.pack.SkuParamDTO;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuAttributeInfo;
import com.lyf.scm.core.remote.item.dto.SkuTypeDTO;

import java.util.List;

/**
 * @Author zys
 * @Remarks
 * @date 2020/7/6
 */
public interface PackDemandDetailService {


    /**
     * 根据销售单号查询需求单成品商品列表
     * @param requireCode
     * @return
     */
    List<PackDemandDetailDTO> queryFinishProductSkuDetail(String requireCode);

    /**
     * 批量保存或修改成品明细
     *
     * @param packDemandDetailDTOList
     * @param userId
     * @return void
     * @author Lucky
     * @date 2020/7/7  15:37
     */
   void batchSavePackDemandComponent(List<PackDemandDetailDTO> packDemandDetailDTOList, Long userId);

    /**
     * 查询商品信息
     * @param skuParamDTO
     * @return
     */
    PageInfo<SkuAttributeInfo> pageSkuInfo(SkuParamDTO skuParamDTO);

   /**
    * 根据商家id,skuCode,unitCode查询商品信息及组合品子品信息
    * @param speList
    * @param packType
    * @return
    */
    SkuAndCombineDTO queryCombinesBySkuCodeAndUnitCode(List<ParamExtDTO> speList, Integer packType);

    /**
     * 商品类型
     * @return
     */
    List<SkuTypeDTO> skuType();

}