package com.lyf.scm.core.service.online.impl;

import com.lyf.scm.core.api.dto.online.RecordPoolDTO;
import com.lyf.scm.core.domain.convert.online.RecordPoolConvertor;
import com.lyf.scm.core.domain.entity.online.RecordPoolDetailE;
import com.lyf.scm.core.domain.entity.online.RecordPoolE;
import com.lyf.scm.core.mapper.online.RecordPoolDetailMapper;
import com.lyf.scm.core.mapper.online.RecordPoolMapper;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.online.OnlineOrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Remarks
 * @date 2020/7/2
 */
@Service
@Slf4j
public class OnlineOrderQueryServiceImpl implements OnlineOrderQueryService {

    @Resource
    private RecordPoolMapper recordPoolMapper;
    @Resource
    private RecordPoolDetailMapper recordPoolDetailMapper;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private RecordPoolConvertor recordPoolConvertor;

    /**
     * 根据单据池DO单号查询单据池信息
     *
     * @param poolRecordCodeList
     * @return
     */
    @Override
    public List<RecordPoolDTO> queryPoolRecordByPoolRecordCode(List<String> poolRecordCodeList) {
        List<RecordPoolE> recordList = recordPoolMapper.queryByDoCodes(poolRecordCodeList);
        if (CollectionUtils.isNotEmpty(recordList)) {
            return new ArrayList<>();
        }
        List<Long> ids = recordList.stream().map(RecordPoolE::getId).distinct().collect(Collectors.toList());
        List<RecordPoolDetailE> detailList = recordPoolDetailMapper.queryByRecordPoolIds(ids);
        Map<Long, List<RecordPoolDetailE>> detailMap = detailList.stream().collect(Collectors.groupingBy(RecordPoolDetailE::getRecordPoolId));

        List<QueryRealWarehouseDTO> queryRealWhDTOS = new ArrayList<>();
        for (RecordPoolE poolE : recordList) {
            QueryRealWarehouseDTO outQueryRealWDTO = new QueryRealWarehouseDTO();
            outQueryRealWDTO.setFactoryCode(poolE.getFactoryCode());
            outQueryRealWDTO.setWarehouseOutCode(poolE.getRealWarehouseCode());
            queryRealWhDTOS.add(outQueryRealWDTO);
        }
        //查询获取实仓信息
        List<RealWarehouse> realWarehouseEList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWhDTOS);
        Map<Long, List<RealWarehouse>> realWarehouseMap = realWarehouseEList.stream().collect(Collectors.groupingBy(RealWarehouse::getId));
        List<RecordPoolDTO> resultList = new ArrayList<>();
        for (RecordPoolE recordPool : recordList) {
            List<RecordPoolDetailE> currDetailList = detailMap.get(recordPool.getId());
            recordPool.setRwRecordPoolDetails(currDetailList);
            List<RecordPoolDTO> recordPoolDoList = recordPoolConvertor.convertEList2DTOList(Arrays.asList(recordPool));
            RecordPoolDTO recordPoolDTO = recordPoolDoList.get(0);
            resultList.add(recordPoolDTO);
            List<RealWarehouse> currRwList = realWarehouseMap.get(recordPool.getRealWarehouseId());
            if (currRwList != null) {
                RealWarehouse rw = currRwList.get(0);
                recordPoolDTO.setRealWarehouse(rw);
            }
        }
        return resultList;
    }

    @Override
    public List<String> queryChildDoCodeByParentDo(String parentCode) {
        return null;
    }
}
