package com.lyf.scm.core.service.stockFront.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lyf.scm.core.api.dto.stockFront.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.core.domain.convert.stockFront.RecordRealVirtualStockSyncRelationConvertor;
import com.lyf.scm.core.domain.entity.stockFront.RecordRealVirtualStockSyncRelationE;
import com.lyf.scm.core.mapper.stockFront.RecordRealVirtualStockSyncRelationMapper;
import com.lyf.scm.core.service.stockFront.RecordRealVirtualStockSyncRelationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecordRealVirtualStockSyncRelationServiceImpl implements RecordRealVirtualStockSyncRelationService {
	@Resource
	private RecordRealVirtualStockSyncRelationMapper recordRealVirtualStockSyncRelationMapper;

	// @Resource
	// private FrPurchaseOrderMapper frPurchaseOrderMapper;

	@Resource
	private RecordRealVirtualStockSyncRelationConvertor recordRealVirtualStockSyncRelationConvertor;

	@Override
	public Map<String, RecordRealVirtualStockSyncRelationDTO> queryRelationMapByRecordCode(String recordCode) {
		return queryRelationMapByRecordCode(recordCode, null);
	}

	@Override
	public Map<String, RecordRealVirtualStockSyncRelationDTO> queryRelationMapByRecordCode(String recordCode,
			Long rwId) {
		List<RecordRealVirtualStockSyncRelationDTO> relationList = recordRealVirtualStockSyncRelationConvertor
				.convertEList2DTOList(recordRealVirtualStockSyncRelationMapper.queryByRecordCode(recordCode));
		Map<String, RecordRealVirtualStockSyncRelationDTO> result = new HashMap<>();
		for (RecordRealVirtualStockSyncRelationDTO relationDTO : relationList) {
			if (rwId == null || rwId.equals(relationDTO.getRealWarehouseId())) {
				result.put(relationDTO.getSkuId() + "_" + relationDTO.getVirtualWarehouseId(), relationDTO);
			}
		}
		return result;
	}

	@Override
	public List<RecordRealVirtualStockSyncRelationDTO> queryRelationListByRecordCode(String recordCode) {
		return recordRealVirtualStockSyncRelationConvertor
				.convertEList2DTOList(recordRealVirtualStockSyncRelationMapper.queryByRecordCode(recordCode));
	}

	@Override
	public List<RecordRealVirtualStockSyncRelationDTO> queryRelationListByRecordCode(String recordCode, Long rwId) {
		List<RecordRealVirtualStockSyncRelationDTO> res = recordRealVirtualStockSyncRelationConvertor
				.convertEList2DTOList(recordRealVirtualStockSyncRelationMapper.queryByRecordCode(recordCode));
		if (rwId == null) {
			return res;
		}
		return res.stream().filter(v -> rwId.equals(v.getRealWarehouseId())).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertRecordRealVirtualStockRelation(List<RecordRealVirtualStockSyncRelationDTO> list) {
		Map<String, List<RecordRealVirtualStockSyncRelationDTO>> configMap = list.stream()
				.collect(Collectors.groupingBy(RecordRealVirtualStockSyncRelationDTO::getRecordCode));
		for (Map.Entry<String, List<RecordRealVirtualStockSyncRelationDTO>> entry : configMap.entrySet()) {
			List<RecordRealVirtualStockSyncRelationDTO> configList = entry.getValue();
			// 删除老数据，重新插入新数据
			recordRealVirtualStockSyncRelationMapper.deleteRelationByRecordCodeAndRid(entry.getKey(),
					configList.get(0).getRealWarehouseId());
			List<RecordRealVirtualStockSyncRelationE> insertList = new ArrayList<>();
			// 入参需要去重,有可能一个物料出现多行，尤其是外采单
			Set<String> newKeyMap = new HashSet<>();
			for (RecordRealVirtualStockSyncRelationDTO dto : configList) {
				RecordRealVirtualStockSyncRelationE recordRealVirtualStockSyncRelationE = this.relationDtoToE(dto);
				String key = dto.getRecordCode() + "_" + dto.getRealWarehouseId() + "_" + dto.getVirtualWarehouseId()
						+ "_" + dto.getSkuId();
				if (newKeyMap.contains(key)) {
					continue;
				}
				newKeyMap.add(key);
				// 新增
				insertList.add(recordRealVirtualStockSyncRelationE);

			}
			if (CollectionUtils.isNotEmpty(insertList)) {
				recordRealVirtualStockSyncRelationMapper.insertRecordRealVirtualStockRelation(insertList);
			}
			// （需修改）
			// 配置单据级别的sku实仓虚仓分配比例，采购单记录修改人ID
			// List<Long> recordIdList =
			// configList.stream().map(RecordRealVirtualStockSyncRelationDTO::getRecordId)
			// .distinct().collect(Collectors.toList());
			// List<Long> ModifierList =
			// configList.stream().map(RecordRealVirtualStockSyncRelationDTO::getModifier)
			// .distinct().collect(Collectors.toList());
			// if (CollectionUtils.isNotEmpty(recordIdList) &&
			// CollectionUtils.isNotEmpty(ModifierList)) {
			// frPurchaseOrderMapper.updateToModifier(recordIdList.get(0),
			// ModifierList.get(0));
			// }
		}
	}

	private RecordRealVirtualStockSyncRelationE relationDtoToE(RecordRealVirtualStockSyncRelationDTO dto) {
		if (dto == null) {
			return null;
		}

		RecordRealVirtualStockSyncRelationE recordRealVirtualStockSyncRelationE = new RecordRealVirtualStockSyncRelationE();

		if (dto.getConfigSyncRate() != null) {
			recordRealVirtualStockSyncRelationE.setSyncRate(dto.getConfigSyncRate());
		}
		recordRealVirtualStockSyncRelationE.setCreator(dto.getCreator());
		recordRealVirtualStockSyncRelationE.setModifier(dto.getModifier());
		recordRealVirtualStockSyncRelationE.setRecordCode(dto.getRecordCode());
		recordRealVirtualStockSyncRelationE.setRealWarehouseId(dto.getRealWarehouseId());
		recordRealVirtualStockSyncRelationE.setRealWarehouseCode(dto.getRealWarehouseCode());
		recordRealVirtualStockSyncRelationE.setFactoryCode(dto.getFactoryCode());
		recordRealVirtualStockSyncRelationE.setVirtualWarehouseId(dto.getVirtualWarehouseId());
		recordRealVirtualStockSyncRelationE.setVirtualWarehouseCode(dto.getVirtualWarehouseCode());
		recordRealVirtualStockSyncRelationE.setSkuId(dto.getSkuId());
		recordRealVirtualStockSyncRelationE.setAllotType(dto.getAllotType());

		return recordRealVirtualStockSyncRelationE;
	}

}
