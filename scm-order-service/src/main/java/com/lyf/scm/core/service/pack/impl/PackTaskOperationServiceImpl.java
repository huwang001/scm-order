package com.lyf.scm.core.service.pack.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.pack.PackTypeEnum;
import com.lyf.scm.core.api.dto.pack.OperationReturnDTO;
import com.lyf.scm.core.api.dto.pack.PackTaskOperationDTO;
import com.lyf.scm.core.api.dto.pack.TaskFinishPageDTO;
import com.lyf.scm.core.api.dto.pack.TaskFinishResponseDTO;
import com.lyf.scm.core.domain.convert.pack.PackTaskFinishConvertor;
import com.lyf.scm.core.domain.entity.pack.PackDemandDetailE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishE;
import com.lyf.scm.core.mapper.pack.PackDemandDetailMapper;
import com.lyf.scm.core.mapper.pack.PackDemandMapper;
import com.lyf.scm.core.mapper.pack.PackTaskFinishMapper;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.user.dto.EmployeeInfoDTO;
import com.lyf.scm.core.remote.user.facade.UserFacade;
import com.lyf.scm.core.service.pack.PackTaskOperationService;
import com.lyf.scm.core.service.pack.TaskFinishRecordService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhanglong
 */
@Slf4j
@Service("packTaskOperationService")
public class PackTaskOperationServiceImpl implements PackTaskOperationService {

    @Resource
    private PackTaskFinishConvertor packTaskFinishConvertor;
    @Resource
    private PackDemandMapper packDemandMapper;
    @Resource
    private PackDemandDetailMapper packDemandDetailMapper;
    @Resource
    private PackTaskFinishMapper packTaskFinishMapper;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private UserFacade userFacade;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private TaskFinishRecordService taskFinishRecordService;

    @Override
    public List<OperationReturnDTO> createPackTaskOperationOrder(List<PackTaskOperationDTO> operationList) {
        if (CollectionUtils.isEmpty(operationList)) {
            log.info("包装任务完成清单同步-参数为空");
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        List<String> taskOperationCodeList = operationList.stream().map(PackTaskOperationDTO::getTaskDetailOperateCode).collect(Collectors.toList());
        List<PackTaskFinishE> existsTaskFinishList = packTaskFinishMapper.batchQueryByTaskOperationCodes(taskOperationCodeList);
        List<String> existsOperationCode = existsTaskFinishList.stream().map(PackTaskFinishE::getTaskDetailOperateCode).collect(Collectors.toList());
        List<PackTaskOperationDTO> newOperationList = operationList.stream().filter(operation ->
                !existsOperationCode.contains(operation.getTaskDetailOperateCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(newOperationList)) {
            log.info("操作单号已经存在，直接返回");
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "-操作单号已经存在");
        }

        //转换成E对象
        List<PackTaskFinishE> packTaskFinishEList = packTaskFinishConvertor.packTaskOperationDto2EList(newOperationList);

        //批量查询需求单信息
        List<String> requireCodeList = packTaskFinishEList.stream().map(PackTaskFinishE::getRequireCode).distinct().collect(Collectors.toList());
        List<PackDemandE> packDemandEList = packDemandMapper.batchQueryByRecordCode(requireCodeList);
        Map<String, PackDemandE> demandCode2EntityMap = packDemandEList.stream().collect(Collectors.toMap(PackDemandE::getRecordCode, Function.identity(), (key1, key2) -> key1));

        //处理包装任务完成清单
        OperationReturnDTO returnDTO = null;
        List<OperationReturnDTO> operationReturnDTOList = new ArrayList<>();
        RealWarehouse realWarehouse = null;
        Map<String, RealWarehouse> realWarehouseCode2InfoMap = new HashMap<>();
        for (PackTaskFinishE finishE : packTaskFinishEList) {
            returnDTO = new OperationReturnDTO();
            returnDTO.setTaskDetailOperateCode(finishE.getTaskDetailOperateCode());
            try {
                realWarehouse = realWarehouseCode2InfoMap.get(finishE.getWarehouseCode());
                if (null == realWarehouse) {
                    realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(finishE.getWarehouseCode());
                    if (null == realWarehouse) {
                        throw new RomeException(ResCode.ORDER_ERROR_7707, ResCode.ORDER_ERROR_7707_DESC);
                    }
                    realWarehouseCode2InfoMap.put(finishE.getWarehouseCode(), realWarehouse);
                }
                finishE.setOutRealWarehouseId(realWarehouse.getId());
                finishE.setOutFactoryCode(realWarehouse.getFactoryCode());
                finishE.setOutRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
                //处理单个 任务完成单
                taskFinishRecordService.saveTaskFinishRecord(finishE, demandCode2EntityMap.get(finishE.getRequireCode()));
                returnDTO.setStatus(0);
            } catch (Exception e) {
                returnDTO.setStatus(1);
                returnDTO.setMessage(e.getMessage());
                log.info("保存任务完成单异常：", e);
            } finally {
                operationReturnDTOList.add(returnDTO);
            }
        }
        return operationReturnDTOList;
    }

    @Override
    public PageInfo<TaskFinishResponseDTO> queryPackTaskFinishRecord(TaskFinishPageDTO finishPage) {
        //设置包装仓 查询条件
        if (StringUtils.isNotEmpty(finishPage.getWarehouseCode())) {
            RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(finishPage.getWarehouseCode());
            if (null == realWarehouse) {
                PageInfo<TaskFinishResponseDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
                personPageInfo.setTotal(0);
                return personPageInfo;
            }
            finishPage.setRealWarehouseId(realWarehouse.getId());
        }
        //分页查询 任务完成单
        Page page = PageHelper.startPage(finishPage.getPageNum(), finishPage.getPageSize());
        List<PackTaskFinishE> finishEList = packTaskFinishMapper.queryTaskFinishPage(finishPage);
        List<TaskFinishResponseDTO> responseDTOList = packTaskFinishConvertor.finishE2DTo(finishEList);
        if (CollectionUtils.isEmpty(responseDTOList)) {
            PageInfo<TaskFinishResponseDTO> personPageInfo = new PageInfo<>(new ArrayList<>());
            personPageInfo.setTotal(0);
            return personPageInfo;
        }

        //查询渠道名称
        List<String> channelCodeList = responseDTOList.stream().map(TaskFinishResponseDTO::getChannelCode).distinct().collect(Collectors.toList());
        //批量查询渠道信息
        List<ChannelDTO> channelInfoList = new ArrayList<>();
        try {
            if (CollectionUtils.isNotEmpty(channelCodeList)) {
                channelInfoList = baseFacade.queryChannelInfoByChannelCodeList(channelCodeList);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        Map<String, ChannelDTO> channelInfoMap = channelInfoList.stream().collect(Collectors.toMap(ChannelDTO::getChannelCode, Function.identity(), (v1, v2) -> v1));

        //查询员工-用户信息
        List<Long> userIdList = responseDTOList.stream().map(TaskFinishResponseDTO::getModifier).distinct().collect(Collectors.toList());
        List<EmployeeInfoDTO> employeeInfoDTOList = new ArrayList<>();
        try {
            if (CollectionUtils.isNotEmpty(userIdList)) {
                employeeInfoDTOList = userFacade.getEmployeeInfoByUserIds(userIdList);
            }
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        Map<Long, EmployeeInfoDTO> employeeInfoDTOMap = employeeInfoDTOList.stream()
                .collect(Collectors.toMap(EmployeeInfoDTO::getId, Function.identity(), (v1, v2) -> v1));

        //查询需求单明细
        List<String> requireCodeList = responseDTOList.stream().map(TaskFinishResponseDTO::getRequireCode).distinct().collect(Collectors.toList());
        List<PackDemandDetailE> demandDetailEList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(requireCodeList)) {
            demandDetailEList = packDemandDetailMapper.queryDemandDetailByRequireCodeList(requireCodeList);
        }
        Map<String, List<PackDemandDetailE>> requireCode2DetailMap = demandDetailEList.stream().collect(Collectors.groupingBy(relation -> relation.getRecordCode()));

        //补充参数
        responseDTOList.forEach(finishDTO -> {
            finishDTO.setPackTypeDesc(PackTypeEnum.getDescByType(finishDTO.getPackType()));
            if (channelInfoMap.containsKey(finishDTO.getChannelCode())) {
                finishDTO.setChannelName(channelInfoMap.get(finishDTO.getChannelCode()).getChannelName());
            }
            if (employeeInfoDTOMap.containsKey(finishDTO.getModifier())) {
                finishDTO.setModifierNo(employeeInfoDTOMap.get(finishDTO.getModifier()).getEmployeeNumber());
            }
            //查询需求单中的计划需求数量
            if (requireCode2DetailMap.containsKey(finishDTO.getRequireCode())) {
                List<PackDemandDetailE> detailEList = requireCode2DetailMap.get(finishDTO.getRequireCode());
                PackDemandDetailE demandDetailE = detailEList.stream().filter(detail -> {
                    if (PackTypeEnum.isCompose(finishDTO.getPackType())) {
                        return detail.getCustomGroupCode().equals(finishDTO.getSkuCode());
                    } else {
                        return detail.getSkuCode().equals(finishDTO.getSkuCode());
                    }
                }).findFirst().orElse(null);
                if (null != demandDetailE) {
                    if (PackTypeEnum.isCompose(finishDTO.getPackType())) {
                        finishDTO.setPlanNum(demandDetailE.getCompositeQty());
                    } else {
                        finishDTO.setPlanNum(demandDetailE.getRequireQty());
                    }
                    //设置商品名称
                    finishDTO.setSkuName(demandDetailE.getSkuName());
                }
            }
        });
        PageInfo<TaskFinishResponseDTO> personPageInfo = new PageInfo<>(responseDTOList);
        personPageInfo.setTotal(page.getTotal());
        return personPageInfo;
    }
}
