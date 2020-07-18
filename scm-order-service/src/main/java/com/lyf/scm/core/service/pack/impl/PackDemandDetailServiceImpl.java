package com.lyf.scm.core.service.pack.impl;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.CombineTypeEnum;
import com.lyf.scm.common.enums.SkuUnitTypeEnum;
import com.lyf.scm.common.enums.pack.PackTypeEnum;
import com.lyf.scm.core.api.dto.pack.*;
import com.lyf.scm.core.api.dto.pack.PackDemandDetailDTO;
import com.lyf.scm.core.api.dto.pack.SkuParamDTO;
import com.lyf.scm.core.domain.convert.pack.PackDemandDetailConvertor;
import com.lyf.scm.core.domain.entity.pack.PackDemandDetailE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.mapper.pack.PackDemandDetailMapper;
import com.lyf.scm.core.mapper.pack.PackDemandMapper;
import com.lyf.scm.core.remote.item.dto.*;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.service.pack.PackDemandDetailService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author zys
 * @Remarks
 * @date 2020/7/6
 */
@Slf4j
@Service("PackDemandDetailService")
public class PackDemandDetailServiceImpl implements PackDemandDetailService {

    @Resource
    private PackDemandDetailMapper packDemandDetailMapper;
    @Resource
    private PackDemandDetailConvertor packDemandDetailConvertor;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private PackDemandMapper packDemandMapper;

    /**
     * @Author zys
     * @param detailDTOList
     * @param userId
     */
    @Override
    @Transactional
    public void batchSavePackDemandComponent(List<PackDemandDetailDTO> detailDTOList, Long userId) {

          //参数校验
          if (CollectionUtils.isEmpty(detailDTOList)) {
              log.warn("参数错误");
              throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
          }
          //商品校验
          List<String> skuCodes = detailDTOList.stream().map(PackDemandDetailDTO::getSkuCode).distinct().collect(Collectors.toList());
          List<SkuInfoExtDTO> skuInfoList = itemFacade.skuBySkuCodes(skuCodes);
          if (skuInfoList.size() != skuCodes.size()) {  //商品异常
              log.warn("商品不存在");
              throw new RomeException(ResCode.ORDER_ERROR_7671, ResCode.ORDER_ERROR_7671_DESC);
          }
          //根据需求单code删除已有数据
          String recordCode = detailDTOList.get(0).getRecordCode();
          packDemandDetailMapper.deletePackDemandDetailByRequireCode(recordCode);
          //设置创建人 更新人
          for (PackDemandDetailDTO detail : detailDTOList) {
              detail.setCerator(userId);
              detail.setModifier(userId);
          }
          List<PackDemandDetailE> detailEList = packDemandDetailConvertor.convertDTOList2EList(detailDTOList);
          //插入成品商品数据
          packDemandDetailMapper.insertFinishProduct(detailEList);

    }

    /**
     * 查询需求单成品商品
     * @param requireCode
     * @return
     * @Author zys
     */
    @Override
    public List<PackDemandDetailDTO> queryFinishProductSkuDetail(String requireCode) {
        //查询需求单信息
        PackDemandE packDemandE = packDemandMapper.queryByRecordCode(requireCode);
        //根据需求单号查询成品商品明细
        List<PackDemandDetailE> detailEList = packDemandDetailMapper.queryDemandDetailByRequireCode(requireCode);
        if (CollectionUtils.isEmpty(detailEList)) {
            log.warn("需求单明细不存在");
            throw new RomeException(ResCode.ORDER_ERROR_7702, ResCode.ORDER_ERROR_7702_DESC);
        }
        //转换
        List<PackDemandDetailDTO> detailDTOS = packDemandDetailConvertor.convertEList2DTOList(detailEList);
        detailDTOS.forEach(detail ->{
            detail.setPackType(packDemandE.getPackType());
            detail.setPackTypeName(PackTypeEnum.getDescByType(detail.getPackType()));
        });
        // 查询sku对应的单位以及单位和基础单位的转换比例
        List<String> skuCodeList = detailDTOS.stream().map(PackDemandDetailDTO::getSkuCode).collect(Collectors.toList());
        List<SkuUnitExtDTO> skuUnitExtDTOList = itemFacade.querySkuUnits(skuCodeList);
        skuUnitExtDTOList = skuUnitExtDTOList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SkuUnitExtDTO::getSkuUnitCodeByPrimaryKey))), ArrayList::new));
        Map<String, List<SkuUnitExtDTO>> skuUnitMap = skuUnitExtDTOList.stream().collect(Collectors.groupingBy((SkuUnitExtDTO::getSkuCode)));
        detailDTOS.forEach(detail ->{
            if (skuUnitMap.containsKey(detail.getSkuCode())){
                detail.setSkuUnitList(skuUnitMap.get(detail.getSkuCode()));
            }
        });
        return detailDTOS;
    }

    /**
     * 查询商品信息
     * @param skuParamDTO
     * @return
     * @Author zys
     */
    @Override
    public PageInfo<SkuAttributeInfo> pageSkuInfo(SkuParamDTO skuParamDTO) {

        PageInfo<SkuAttributeInfo> skuList= itemFacade.pageSkuInfo(skuParamDTO);
        if (CollectionUtils.isEmpty(skuList.getList())){
            throw new RomeException(ResCode.ORDER_ERROR_7672,ResCode.ORDER_ERROR_7672_DESC);
        }
        //获取商品信息列表
        List<SkuAttributeInfo> skuAttributeInfos = skuList.getList();
        //获取商品code集合
        List<String> skuCodes = skuAttributeInfos.stream().map(SkuAttributeInfo::getSkuCode).collect(Collectors.toList());
        //获取单位信息
        List<SkuUnitExtDTO> skuUnitExtDTOList = itemFacade.querySkuUnits(skuCodes);
        Map<String,List<SkuUnitExtDTO>> skuMap = skuUnitExtDTOList.stream().collect(Collectors.groupingBy(unit ->unit.getSkuCode()));
        //存放单位信息
        for (SkuAttributeInfo sku:skuAttributeInfos) {
            List<SkuUnitExtDTO>  skuUnitExtList = skuMap.get(sku.getSkuCode());
            List<SkuUnitExtDTO> unitExtDTOS = skuUnitExtList.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUnitCode()))),ArrayList::new));
            sku.setSkuUnitExtDTO(unitExtDTOS);
        }
        return skuList;
    }

    /**
     * 根据商家id,skuCode,unitCode查询成品信息及组合品子品信息
     * @param speList
     * @param packType
     * @return
     * @Author zys
     */
    @Override
    public SkuAndCombineDTO queryCombinesBySkuCodeAndUnitCode(List<ParamExtDTO> speList,Integer packType) {

        List<SkuUnitAndCombineExtDTO>  list = null;
        List<ParamExtDTO> paramExtDTOList = new ArrayList<ParamExtDTO>();
        list = itemFacade.queryCombinesBySkuCodeAndUnitCode(speList,null);
        if(packType.equals(PackTypeEnum.SELF_COMPOSE.getType()) || packType.equals(PackTypeEnum.UN_SELF_COMPOSE.getType()) || packType.equals(PackTypeEnum.DEVAN.getType())){
            list.forEach(skuCombine ->{
                List<SkuCombineSimpleDTO> skuCombineSimpleDTOS = new ArrayList<>();
                UnitAndBaseUnitInfoDTO unitAndBaseUnitInfoDTO = skuCombine.getUnitInfo().get(0);
                SkuCombineSimpleDTO skuCombineSimpleDTO = new SkuCombineSimpleDTO();
                skuCombineSimpleDTO.setCombineSkuCode(skuCombine.getSkuCode());
                if(packType == PackTypeEnum.DEVAN.getType()){  //拆箱 成品商品编码和组件商品编码相同
                    skuCombineSimpleDTO.setSkuCode(skuCombine.getSkuCode());
                }
                skuCombineSimpleDTO.setCombineSkuUnitCode(unitAndBaseUnitInfoDTO.getBasicUnitCode());
                skuCombineSimpleDTO.setCombineSkuUnitName(unitAndBaseUnitInfoDTO.getBasicUnitName());
                skuCombineSimpleDTOS.add(skuCombineSimpleDTO);
                skuCombine.setCombineInfo(skuCombineSimpleDTOS);
            });
        }
        //获取所有组件商品的skuCode和unitCode
        for (SkuUnitAndCombineExtDTO skuUnitAndCombineExtDTO : list) {
            if (!skuUnitAndCombineExtDTO.getCombineType().equals(CombineTypeEnum.RAW_MATERIAL.getType())) {
                for (SkuCombineSimpleDTO skuCombineSimpleDTO : skuUnitAndCombineExtDTO.getCombineInfo()) {
                    ParamExtDTO paramExtDTO = new ParamExtDTO();
                    paramExtDTO.setUnitCode(skuCombineSimpleDTO.getCombineSkuUnitCode());
                    paramExtDTO.setSkuCode(skuCombineSimpleDTO.getCombineSkuCode());
                    paramExtDTOList.add(paramExtDTO);
                }
            }
        }
        //查询商品单位信息
        List<SkuUnitExtDTO> unitExtDTOS = itemFacade.unitsBySkuCodeAndUnitCodeAndMerchantId(paramExtDTOList,null);
        List<SkuUnitExtDTO> unitExtDTOS1 = itemFacade.unitsBySkuCodeAndMerchantId(paramExtDTOList);
        Map<String, SkuUnitExtDTO> combineSkuUnitMap = unitExtDTOS.stream()
                .collect(Collectors.toMap((skuUnit) -> unitGroupKey(skuUnit), Function.identity(), (v1, v2) -> v1));
        //获取所有组件商品skucode
        List<String> allCombineSkuCodes = new ArrayList<>();
        list.forEach(combineExtDTO -> {
            //获取所有组件商品code
            List<String> combineSkuCodes = combineExtDTO.getCombineInfo().stream().map(SkuCombineSimpleDTO::getCombineSkuCode).collect(Collectors.toList());
            allCombineSkuCodes.addAll(combineSkuCodes);
        });
        //根据code查询商品信息
        List<SkuInfoExtDTO> skuInfoExtDTOS = itemFacade.skuListBySkuCodes(allCombineSkuCodes);
        //组件商品name,单位赋值
        Map<String, SkuInfoExtDTO> map = skuInfoExtDTOS.stream().collect(Collectors.toMap(unit ->unit.getSkuCode(),Function.identity(),(v1, v2) ->v1));
        list.forEach(combineExtDTO -> {
            for (SkuCombineSimpleDTO comSimple : combineExtDTO.getCombineInfo()) {
                SkuInfoExtDTO info = map.get(comSimple.getCombineSkuCode());
                if(info!= null){
                    comSimple.setCombineSkuName(info.getName());
                }
                SkuUnitExtDTO unitExt = combineSkuUnitMap.get(comSimple.getCombineSkuCode() + comSimple.getCombineSkuUnitCode());
                if(unitExt != null){
                    comSimple.setCombineSkuUnitName(unitExt.getBasicUnitName());
                }
                //获取运输单位对象 type = 3
                SkuUnitExtDTO transportUnit = unitExtDTOS1.stream().filter(unit -> {
                    if (unit.getType() == SkuUnitTypeEnum.TRANSPORT_UNIT.getId() &&
                        unit.getSkuCode().equals(comSimple.getCombineSkuCode()))
                    {
                        return true;
                    }
                    return false ;
                }).findFirst().orElse(null);
                if (transportUnit != null){
                    comSimple.setBoxUnitRate(transportUnit.getScale());
                    comSimple.setTransportUnitName(transportUnit.getUnitName());
                }
            }
        });

        SkuAndCombineDTO  skuAndCombineDTO = new SkuAndCombineDTO();
        List<PackDemandDetailDTO> packDemandDetailDTOS  = new ArrayList<>();
        List<PackDemandComponentDTO> packDemandComponentDTOS = new ArrayList<>();
        list.forEach(sku ->{
            PackDemandDetailDTO packDemandDetailDTO = new PackDemandDetailDTO();
            packDemandDetailDTO.setSkuName(sku.getSkuName());
            packDemandDetailDTO.setSkuCode(sku.getSkuCode());
            packDemandDetailDTO.setBasicScale(sku.getUnitInfo().get(0).getScale());
            packDemandDetailDTO.setBasicUnitCode(sku.getUnitInfo().get(0).getBasicUnitCode());
            packDemandDetailDTO.setBasicUnitName(sku.getUnitInfo().get(0).getBasicUnitName());
            packDemandDetailDTOS.add(packDemandDetailDTO);

            sku.getCombineInfo().forEach(combine ->{
                PackDemandComponentDTO packDemandComponentDTO= new PackDemandComponentDTO();
                packDemandComponentDTO.setSkuCode(combine.getCombineSkuCode());
                packDemandComponentDTO.setParentSkuCode(combine.getSkuCode());
                packDemandComponentDTO.setSkuName(combine.getCombineSkuName());
                packDemandComponentDTO.setUnit(combine.getCombineSkuUnitName());
                packDemandComponentDTO.setBomQty(combine.getNum());
                packDemandComponentDTO.setTransportUnitName(combine.getTransportUnitName());
                packDemandComponentDTO.setBoxUnitRate(combine.getBoxUnitRate());
                packDemandComponentDTO.setUnitCode(combine.getCombineSkuUnitCode());
                packDemandComponentDTOS.add(packDemandComponentDTO);
            });
        });
        skuAndCombineDTO.setPackDemandDetailDTOList(packDemandDetailDTOS);
        skuAndCombineDTO.setPackDemandComponentDTOList(packDemandComponentDTOS);

        return skuAndCombineDTO;
    }

    /**
     * @Author zys
     * @remark 查询商品类型
     * @return
     */
    @Override
    public List<SkuTypeDTO> skuType() {

        List<SkuTypeDTO>  skuTypeDTOS = itemFacade.skuType();

        return skuTypeDTOS;
    }

    /**
     * @param skuUnitExtDTO
     * @return
     * @description SkuCode+UnitCode唯一
     */
    private static String unitGroupKey(SkuUnitExtDTO skuUnitExtDTO) {
        return skuUnitExtDTO.getSkuCode() + skuUnitExtDTO.getUnitCode();
    }
}
