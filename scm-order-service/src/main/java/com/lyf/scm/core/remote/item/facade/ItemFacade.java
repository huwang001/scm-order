package com.lyf.scm.core.remote.item.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.lyf.scm.core.api.dto.pack.SkuParamDTO;
import com.lyf.scm.core.remote.item.dto.*;
import com.rome.arch.core.exception.RomeException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.item.ItemRemoteService;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * 商品中心接口
 *
 * @author zhangxu
 * @date 2020/4/9
 */
@Slf4j
@Component
public class ItemFacade {

    private ParamValidator validator = ParamValidator.INSTANCE;

    private final Integer PAGE = 1;
    private final int PAGE_SIZE = 100;

    @Resource
    private ItemRemoteService itemRemoteService;

    @Value("${app.merchantId}")
    private Long merchantId;


    /**
     * 批量通过skuCodes查询sku信息
     *
     * @param skuCodeList
     * @return
     */
    public List<SkuInfoExtDTO> skuListBySkuCodes(List<String> skuCodeList) {
        List<SkuInfoExtDTO> skuInfoExtDTOList = new ArrayList<SkuInfoExtDTO>();
        if (CollectionUtils.isNotEmpty(skuCodeList)) {
            // 集合分割处理
            List<List<String>> partList = ListUtils.partition(skuCodeList, PAGE_SIZE);
            Response<List<SkuInfoExtDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<String> subList = partList.get(i);
                resp = itemRemoteService.skuListBySkuCodes(subList);
                ResponseValidateUtils.validResponse(resp);
                skuInfoExtDTOList.addAll(resp.getData());
            }
        }
        return skuInfoExtDTOList;
    }

    /**
     * 批量通过SkuCode和merchantId查询基础单位换算关系
     *
     * @param paramExtDTO
     * @return
     */
    public List<SkuUnitExtDTO> unitsBySkuCodeAndMerchantId(List<ParamExtDTO> paramExtDTO) {
        List<SkuUnitExtDTO> skuUnitExtDTOList = new ArrayList<SkuUnitExtDTO>();
        if (CollectionUtils.isNotEmpty(paramExtDTO)) {
            // 集合分割处理
            List<List<ParamExtDTO>> partList = ListUtils.partition(paramExtDTO, PAGE_SIZE);
            Response<List<SkuUnitExtDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<ParamExtDTO> subList = partList.get(i);
                resp = itemRemoteService.unitsBySkuCodeAndMerchantId(subList);
                ResponseValidateUtils.validResponse(resp);
                skuUnitExtDTOList.addAll(resp.getData());
            }
        }
        return skuUnitExtDTOList;
    }

    /**
     * 根据门店编号和skuCodes查询门店所对应的进货权限（从SAP查询）
     *
     * @param storeCode 经三方沟通，该字段实际上就是工厂编码
     * @param codes
     * @return
     */
    public List<StorePurchaseAccessDTO> getStoreAccessFromSAPBySkuCodesAndStoreCode(String storeCode, List<String> codes) {
        List<StorePurchaseAccessDTO> storePurchaseAccessDTOList = new ArrayList<StorePurchaseAccessDTO>();
        if (StringUtils.isNotBlank(storeCode) && CollectionUtils.isNotEmpty(codes)) {
            // 集合分割处理
            List<List<String>> partList = ListUtils.partition(codes, PAGE_SIZE);
            Response<List<StorePurchaseAccessDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<String> subList = partList.get(i);
                resp = itemRemoteService.getStoreAccessFromSAPBySkuCodesAndStoreCode(storeCode, subList);
                ResponseValidateUtils.validResponse(resp);
                storePurchaseAccessDTOList.addAll(resp.getData());
            }
        }
        return storePurchaseAccessDTOList;
    }

    /**
     * 批量通过SkuCode和merchantId查询基础单位换算关系
     *
     * @param skuCodeList
     * @return
     */
    public List<SkuUnitExtDTO> querySkuUnits(List<String> skuCodeList) {
        List<SkuUnitExtDTO> skuUnitExtDTOList = new ArrayList<SkuUnitExtDTO>();
        if (CollectionUtils.isNotEmpty(skuCodeList)) {
            List<ParamExtDTO> paramList = new ArrayList<>();
            for (String skuCode : skuCodeList) {
                ParamExtDTO dto = new ParamExtDTO();
                dto.setMerchantId(merchantId);
                dto.setSkuCode(skuCode);
                paramList.add(dto);
            }
            skuUnitExtDTOList = this.unitsBySkuCodeAndMerchantId(paramList);
        }
        return skuUnitExtDTOList;
    }

    /**
     * 批量通过skuIdList查询sku信息
     *
     * @param skuIdList
     * @return
     */
    public List<SkuInfoExtDTO> skuBySkuIds(List<Long> skuIdList) {
        List<SkuInfoExtDTO> skuInfoExtDTOList = new ArrayList<SkuInfoExtDTO>();
        if (CollectionUtils.isNotEmpty(skuIdList)) {
            // 集合分割处理
            List<List<Long>> partList = ListUtils.partition(skuIdList, PAGE_SIZE);
            Response<PageInfo<SkuInfoExtDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<Long> subList = partList.get(i);
                List<ParamExtDTO> paramList = new ArrayList<ParamExtDTO>();
                for (Long skuId : subList) {
                    ParamExtDTO dto = new ParamExtDTO();
                    dto.setMerchantId(merchantId);
                    dto.setSkuId(skuId);
                    paramList.add(dto);
                }
                resp = itemRemoteService.skuBySkuIds(paramList);
                ResponseValidateUtils.validResponse(resp);
                skuInfoExtDTOList.addAll(resp.getData().getList());
            }
        }
        return skuInfoExtDTOList;
    }

    /**
     * 批量通过skuCodeList查询sku信息
     *
     * @param skuCodeList
     * @return
     */
    public List<SkuInfoExtDTO> skuBySkuCodes(List<String> skuCodeList) {
        List<SkuInfoExtDTO> skuInfoExtDTOList = new ArrayList<SkuInfoExtDTO>();
        if (CollectionUtils.isNotEmpty(skuCodeList)) {
            // 集合分割处理
            List<List<String>> partList = ListUtils.partition(skuCodeList, PAGE_SIZE);
            Response<PageInfo<SkuInfoExtDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<String> subList = partList.get(i);
                List<ParamExtDTO> paramList = new ArrayList<ParamExtDTO>();
                for (String skuCode : subList) {
                    ParamExtDTO dto = new ParamExtDTO();
                    dto.setMerchantId(merchantId);
                    dto.setSkuCode(skuCode);
                    paramList.add(dto);
                }
                resp = itemRemoteService.skuBySkuCodes(paramList);
                ResponseValidateUtils.validResponse(resp);
                skuInfoExtDTOList.addAll(resp.getData().getList());
            }
        }
        return skuInfoExtDTOList;
    }

    /**
     * 批量通过skuCode和unitCode查询组合品信息
     *
     * @param * @param null
     * @return
     * @author Lucky
     * @date 2020/7/9  13:13
     */
    public List<SkuUnitAndCombineExtDTO> queryCombinesBySkuCodeAndUnitCode(List<ParamExtDTO> speList, Long mId) {
        List<SkuUnitAndCombineExtDTO> skuInfoExtDTOList = new ArrayList<SkuUnitAndCombineExtDTO>();
        if (CollectionUtils.isNotEmpty(speList)) {
            // 集合分割处理
            List<List<ParamExtDTO>> partList = ListUtils.partition(speList, PAGE_SIZE);
            Response<List<SkuUnitAndCombineExtDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<ParamExtDTO> subList = partList.get(i);
                for (ParamExtDTO paramExtDTO : subList) {
                    if (mId != null) {
                        paramExtDTO.setMerchantId(mId);
                    } else {
                        paramExtDTO.setMerchantId(merchantId);
                    }
                }
                resp = itemRemoteService.querySkuUnitAndCombinesBySkuCodeAndUnitCodeAndMerchantId(subList);
                ResponseValidateUtils.validResponse(resp);
                skuInfoExtDTOList.addAll(resp.getData());
            }
        }
        return skuInfoExtDTOList;
    }

    /**
     * 批量通过SkuId和单位Code单位查询基础单位换算关系
     *
     * @param speList
     * @param mId
     * @return
     */
    public List<SkuUnitExtDTO> unitsBySkuIdAndUnitCode(List<ParamExtDTO> speList, Long mId) {
        List<SkuUnitExtDTO> skuUnitExtDTOList = new ArrayList<SkuUnitExtDTO>();
        if (CollectionUtils.isNotEmpty(speList)) {
            // 集合分割处理
            List<List<ParamExtDTO>> partList = ListUtils.partition(speList, PAGE_SIZE);
            Response<PageInfo<SkuUnitExtDTO>> resp;
            for (int i = 0; i < partList.size(); i++) {
                List<ParamExtDTO> subList = partList.get(i);
                for (ParamExtDTO paramExtDTO : subList) {
                    if (mId != null) {
                        paramExtDTO.setMerchantId(mId);
                    } else {
                        paramExtDTO.setMerchantId(merchantId);
                    }
                }
                resp = itemRemoteService.unitsBySkuIdAndUnitCode(subList);
                ResponseValidateUtils.validResponse(resp);
                skuUnitExtDTOList.addAll(resp.getData().getList());
            }
        }
        return skuUnitExtDTOList;
    }

    public List<SkuUnitExtDTO> unitsBySkuIdAndUnitCodeAndMerchantId(List<ParamExtDTO> paramList) {
        long time = System.currentTimeMillis();
        List<SkuUnitExtDTO> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(paramList)) {
            List<List<ParamExtDTO>> splitList = ListUtils.partition(paramList, PAGE_SIZE);
            for (List<ParamExtDTO> tempList : splitList) {
                resultList.addAll(unitsBySkuIdAndUnitCode(tempList, null));
            }
        }
        if (System.currentTimeMillis() - time > 2000) {
            log.error("请求item-unitsByFrontDetail, 执行耗时【" + (System.currentTimeMillis() - time) + "】, 参数:" + JSON.toJSONString(paramList));
        }
        return resultList;
    }

    /**
     * 通过skuCode + skuUnit + mId 查询商品信息
     *
     * @param paramList
     * @param mId
     * @return
     */
    public List<SkuUnitExtDTO> unitsBySkuCodeAndUnitCodeAndMerchantId(List<ParamExtDTO> paramList, Long mId) {
        List<SkuUnitExtDTO> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(paramList)) {
            return resultList;
        }
        List<List<ParamExtDTO>> partList = ListUtils.partition(paramList, PAGE_SIZE);
        Response<PageInfo<SkuUnitExtDTO>> response;
        for (List<ParamExtDTO> subList : partList) {
            for (ParamExtDTO paramExtDTO : subList) {
                if (mId != null) {
                    paramExtDTO.setMerchantId(mId);
                } else {
                    paramExtDTO.setMerchantId(merchantId);
                }
            }
            response = itemRemoteService.unitsBySkuCodeAndUnitCodeAndMerchantId(subList, PAGE);
            ResponseValidateUtils.validResponse(response);
            resultList.addAll(response.getData().getList());
        }
        return resultList;
    }

    /**
     * 根据商品CODE、单位类型查询单位信息
     *
     * @param skuCodes
     * @param type
     * @return
     */
    public List<SkuUnitExtDTO> querySkuUnitsBySkuCodeAndType(List<String> skuCodes, Long type) {
        return this.querySkuUnitsBySkuCodeAndType(skuCodes, type, merchantId);
    }

    /**
     * 根据商品CODE、单位类型、商家ID查询单位信息
     *
     * @param skuCodes
     * @param type
     * @param merchantId
     * @return
     */
    public List<SkuUnitExtDTO> querySkuUnitsBySkuCodeAndType(List<String> skuCodes, Long type, Long merchantId) {
        long time = System.currentTimeMillis();
        List<SkuUnitExtDTO> resultList = new ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(skuCodes) && type != null) {
            List<QuerySkuParamExtDTO> paramList = new ArrayList<>();
            for (String skuCode : skuCodes) {
                QuerySkuParamExtDTO dto = new QuerySkuParamExtDTO();
                dto.setMerchantId(merchantId);
                dto.setSkuCode(skuCode);
                paramList.add(dto);
            }
            List<List<QuerySkuParamExtDTO>> splitList = ListUtils.partition(paramList, PAGE_SIZE);
            for (List<QuerySkuParamExtDTO> tempList : splitList) {
                Response<List<SkuUnitExtDTO>> response = itemRemoteService.querySkuUnits(tempList, PAGE);

                ResponseValidateUtils.validResponse(response);

                List<SkuUnitExtDTO> skuUnitExtDTOList = response.getData();
                //相同单位类型的单位信息可能有多个
                skuUnitExtDTOList = skuUnitExtDTOList.stream().filter(skuUnitExtDTO -> skuUnitExtDTO != null && type.equals(skuUnitExtDTO.getType())).collect(Collectors.toList());
                resultList.addAll(skuUnitExtDTOList);

            }
        }
        if (System.currentTimeMillis() - time > 2000) {
            log.error("请求item-querySkuUnitsByType, 执行耗时【" + (System.currentTimeMillis() - time) + "】, 参数skuCodes:" + JSON.toJSONString(skuCodes) + ", type:" + JSON.toJSONString(type));
        }
        return resultList;
    }


    /**
     * 根据门店编号和skuCodes查询门店所对应的进货权限（基于商品中心）
     *
     * @param storePurchaseParamDTOList
     */
    public List<StorePurchaseDTO> getStorePurchaseAccessBySkuCodesAndStoreCode(List<StorePurchaseParamDTO> storePurchaseParamDTOList) {
        long time = System.currentTimeMillis();
        List<StorePurchaseDTO> resultList = new ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(storePurchaseParamDTOList)) {
            for (StorePurchaseParamDTO storePurchaseParamDTO : storePurchaseParamDTOList) {
                List<String> skuCodes = storePurchaseParamDTO.getSkuCodes();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(skuCodes)) {
                    List<List<String>> splitList = ListUtils.partition(skuCodes, PAGE_SIZE);
                    for (List<String> tempList : splitList) {
                        //组装请求参数
                        storePurchaseParamDTO.setSkuCodes(tempList);
                        List<StorePurchaseParamDTO> paramDTOList = new ArrayList<StorePurchaseParamDTO>();
                        paramDTOList.add(storePurchaseParamDTO);
                        Response<List<StorePurchaseDTO>> response = itemRemoteService.getStorePurchaseAccessBySkuCodesAndStoreCode(paramDTOList);

                        ResponseValidateUtils.validResponse(response);

                        resultList.addAll(response.getData());

                    }
                }
            }
        }
        if (System.currentTimeMillis() - time > 2000) {
            log.error("请求item-getStorePurchaseAccessBySkuCodesAndStoreCode, 执行耗时【" + (System.currentTimeMillis() - time) + "】, 参数:" + JSON.toJSONString(storePurchaseParamDTOList));
        }
        return resultList;
    }


    /**
     * 批量通过skuCode、渠道编码、销售单位集合获取Sku/上下架/价格/单位信息
     *
     * @param storeSaleParamDTOList
     */
    public List<StoreSalePowerDTO> skusByManyParam(List<StoreSaleParamDTO> storeSaleParamDTOList) {
        long time = System.currentTimeMillis();
        List<StoreSalePowerDTO> resultList = new ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(storeSaleParamDTOList)) {
            List<List<StoreSaleParamDTO>> splitList = ListUtils.partition(storeSaleParamDTOList, PAGE_SIZE);
            for (List<StoreSaleParamDTO> tempList : splitList) {
                Response<PageInfo<StoreSalePowerDTO>> response = itemRemoteService.skusByManyParam(tempList);

                ResponseValidateUtils.validResponse(response);
                resultList.addAll(response.getData().getList());

            }
        }
        if (System.currentTimeMillis() - time > 2000) {
            log.error("请求item-skusByManyParam, 执行耗时【" + (System.currentTimeMillis() - time) + "】, 参数:" + JSON.toJSONString(storeSaleParamDTOList));
        }
        return resultList;
    }

    /**
     * 查询商品信息
     *
     * @param param
     * @return
     */
     public PageInfo<SkuAttributeInfo> pageSkuInfo(SkuParamDTO param){
        long time = System.currentTimeMillis();
        PageInfo<SkuAttributeInfo> skuInfoExtDTOS = new PageInfo<>();
        if(param != null){
            if(param.getSkuCodes() ==null){
                param.setSkuCodes(new ArrayList<>());
            }
            log.info("入参：>>>{}" ,JSON.toJSONString(param));
            Response<PageInfo<SkuAttributeInfo>> resp = itemRemoteService.pageSkuInfo(param.getSkuCodes(),param.getSkuType(),param.getPageNum(),param.getPageSize());
            log.info("出参：>>>{}",JSON.toJSONString(resp.getData()));
            ResponseValidateUtils.validResponse(resp);
            return resp.getData();
        }
        if (System.currentTimeMillis() - time > 5000){
            log.error("请求item-skuListPageByChannelCode,执行耗时【" +(System.currentTimeMillis() - time)+"】,参数："+JSON.toJSONString(param));
        }
        return skuInfoExtDTOS;
    }



    /**
     * 根据商品id和分类ID查询商品
     * @param skuIds
     * @return
     */
    public List<SkuInfoExtDTO> skusBySkuId(List<Long> skuIds ){
        return  this.skusBySkuId(skuIds , merchantId);
    }

    /**
     * 根据商品id和分类ID查询商品
     * @param skuIds
     * @return
     */
    public List<SkuInfoExtDTO> skusBySkuId(List<Long> skuIds ,Long merchantId ){
        //log.error("请求item-skusBySkuId, 参数:" + JSON.toJSONString(skuIds));
        long time = System.currentTimeMillis();
        List<SkuInfoExtDTO> resultList = new ArrayList<>();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(skuIds)) {
            List<QuerySkuParamExtDTO> paramList = new ArrayList<>();
            for (Long skuId : skuIds){
                QuerySkuParamExtDTO dto = new QuerySkuParamExtDTO();
                dto.setMerchantId(merchantId);
                dto.setSkuId(skuId);
                paramList.add(dto);
            }
            List<List<QuerySkuParamExtDTO>> splitList = splitList(paramList);
            for (List<QuerySkuParamExtDTO> tempList : splitList) {
                Response<PageInfo<SkuInfoExtDTO>> rep = itemRemoteService.skusBySkuIdAndMerchantId(tempList, PAGE);
                if (! validator.validResponse(rep)) {
                    throw new RomeException(ResCode.ORDER_ERROR_3002, ResCode.ORDER_ERROR_3002_DESC);
                }else{
                    resultList.addAll(rep.getData().getList());
                }
            }
        }
        if(System.currentTimeMillis() - time > 2000) {
            log.error("请求item-skusBySkuId, 执行耗时【" + (System.currentTimeMillis() - time) + "】, 参数:" + JSON.toJSONString(skuIds));
        }
        return resultList;
    }

    /**
     * 对list进行切片
     * @param list
     * @param <T>
     * @return
     */
    public <T> List<List<T>> splitList(List<T> list) {
        List<List<T>> listArray = new ArrayList<>();
        if (! validator.validCollection(list)) {return listArray;}
        if (list.size() <= PAGE_SIZE) {
            listArray.add(list);
            return listArray;
        }
        List<T> subList = new ArrayList<>();
        listArray.add(subList);
        for (int i=0; i<list.size(); i++) {
            subList.add(list.get(i));
            if (subList.size() == PAGE_SIZE) {
                if (i != list.size()-1) {
                    subList = new ArrayList<>();
                    listArray.add(subList);
                }
            }
        }
        return listArray;
    }

    /**
     * 商品类型
     * @return
     */
    public List<SkuTypeDTO> skuType(){

        Response<List<SkuTypeDTO> >skuTypeDTOS = itemRemoteService.skuType();
        ResponseValidateUtils.validResponse(skuTypeDTOS);
        return skuTypeDTOS.getData();
    }
}