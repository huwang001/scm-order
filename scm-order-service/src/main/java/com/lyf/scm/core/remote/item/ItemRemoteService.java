package com.lyf.scm.core.remote.item;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.remote.item.dto.*;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品中心远程调用接口
 *
 * @author zhangxu
 * @date 2020/4/9
 */
@FeignClient(value = "item-core")
public interface ItemRemoteService {

    /**
     * 批量通过skuCodes查询sku信息
     *
     * @param skuCodeList
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/skuListBySkuCodes", method = RequestMethod.POST)
    Response<List<SkuInfoExtDTO>> skuListBySkuCodes(@RequestBody List<String> skuCodeList);

    /**
     * 批量通过SkuCode和merchantId查询基础单位换算关系
     */
    @RequestMapping(value = "/api/v1/item/external/unitsBySkuCodeAndMerchantId", method = RequestMethod.POST)
    Response<List<SkuUnitExtDTO>> unitsBySkuCodeAndMerchantId(@RequestBody List<ParamExtDTO> paramExtDTO);

    /**
     * 根据门店编号和skuCodes查询门店所对应的进货权限
     *
     * @param storeCode
     * @param skuCodes
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/getStoreAccessFromSAPBySkuCodesAndStoreCode", method = RequestMethod.POST)
    Response<List<StorePurchaseAccessDTO>> getStoreAccessFromSAPBySkuCodesAndStoreCode(@RequestParam("storeCode") String storeCode, @RequestParam("skuCodes") List<String> skuCodes);

    /**
     * 批量通过skuIdList查询sku信息
     *
     * @param paramList
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/skusBySkuIdAndMerchantId", method = RequestMethod.POST)
    Response<PageInfo<SkuInfoExtDTO>> skuBySkuIds(@RequestBody List<ParamExtDTO> paramList);

    /**
     * 批量通过skuCodeList查询sku信息
     *
     * @param paramList
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/skuListBySkuCodeAndMerchantId", method = RequestMethod.POST)
    Response<PageInfo<SkuInfoExtDTO>> skuBySkuCodes(@RequestBody List<ParamExtDTO> paramList);

    /**
     * 根据商家id,skuCode,unitCode查询商品信息及组合品子品信息
     *
     * @param paramList
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/querySkuUnitAndCombinesBySkuCodeAndUnitCodeAndMerchantId", method = RequestMethod.POST)
    Response<List<SkuUnitAndCombineExtDTO>> querySkuUnitAndCombinesBySkuCodeAndUnitCodeAndMerchantId(@RequestBody List<ParamExtDTO> paramList);

    /**
     * 批量通过SkuId和单位Code单位查询基础单位换算关系
     *
     * @param paramList
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/unitsBySkuIdAndUnitCodeAndMerchantId", method = RequestMethod.POST)
    Response<PageInfo<SkuUnitExtDTO>> unitsBySkuIdAndUnitCode(@RequestBody List<ParamExtDTO> paramList);

    /**
     * 批量通过skuCode和单位code查询基础单位换算关系
     */
    @RequestMapping(value = "/api/v1/item/external/unitsBySkuCodeAndUnitCodeAndMerchantId", method = RequestMethod.POST)
    Response<PageInfo<SkuUnitExtDTO>> unitsBySkuCodeAndUnitCodeAndMerchantId(@RequestBody List<ParamExtDTO> paramExtDTO, @RequestParam("pageNum") Integer pageNum);

    /**
     * 根据门店编号和skuCodes查询门店所对应的进货权限
     *
     * @param storePurchaseParamDTOList
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/getStorePurchaseAccessBySkuCodesAndStoreCode", method = RequestMethod.POST)
    Response<List<StorePurchaseDTO>> getStorePurchaseAccessBySkuCodesAndStoreCode(@RequestBody List<StorePurchaseParamDTO> storePurchaseParamDTOList);


    /**
     * 查询sku所有单位
     *
     * @param paramList
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/unitsBySkuCodeAndMerchantId", method = RequestMethod.POST)
    Response<List<SkuUnitExtDTO>> querySkuUnits(@RequestBody List<QuerySkuParamExtDTO> paramList, @RequestParam("pageNum") Integer pageNum);

    /**
     * 批量通过skuCode、渠道编码、销售单位集合获取Sku/上下架/价格/单位信息
     *
     * @param storeSaleParamDTOList
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/skusByManyParam", method = RequestMethod.POST)
    Response<PageInfo<StoreSalePowerDTO>> skusByManyParam(@RequestBody List<StoreSaleParamDTO> storeSaleParamDTOList);

    /**
     * 查询商品信息
     * @param skuCodes
     * @param skuType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/skuListPageByChannelCode", method = RequestMethod.GET)
    Response<PageInfo<SkuInfoExtDTO>> skuListPageByChannelCode(@RequestParam("channelCode") String channelCode, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);


    /**
     * 根据商品id和分类ID查询商品(只查询100条)
     * @param paramList
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/api/v1/item/external/skusBySkuIdAndMerchantId",method = RequestMethod.POST)
    Response<PageInfo<SkuInfoExtDTO>> skusBySkuIdAndMerchantId(@RequestBody List<QuerySkuParamExtDTO> paramList, @RequestParam("pageNum") Integer pageNum);

    @RequestMapping(value = "/api/v1/item/sku/pageSkuInfo", method = RequestMethod.POST)
    Response<PageInfo<SkuAttributeInfo>> pageSkuInfo(@RequestBody List<String> skuCodes, @RequestParam("skuType") String skuType, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);

    /**
     * 商品类型
     * @return
     */
    @RequestMapping(value="/api/v1/item/external/skuType",method = RequestMethod.GET)
    Response<List<SkuTypeDTO>> skuType();
}