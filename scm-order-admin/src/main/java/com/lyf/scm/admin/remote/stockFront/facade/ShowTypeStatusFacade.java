package com.lyf.scm.admin.remote.stockFront.facade;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lyf.scm.admin.remote.stockFront.ShowTypeStatusRemoteService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShowTypeStatusFacade {

	@Autowired
	private ShowTypeStatusRemoteService showTypeStatusRemoteService;

	/**
	 * 获取前置单状态集合
	 * 
	 * @return
	 */
	public Map<Integer, String> queryFrontRecordStatusList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryFrontRecordStatusList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取前置单类型集合
	 * 
	 * @return
	 */
	public Map<Integer, String> queryFrontRecordTypeList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryFrontRecordTypeList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取出入单状态集合
	 * 
	 * @return
	 */
	public Map<Integer, String> queryWarehouseRecordStatusList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryWarehouseRecordStatusList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 入库单页面状态列表
	 * 
	 * @return
	 */
	public Map<Integer, String> queryInWarehouseRecordStatusList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryInWarehouseRecordStatusList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 出库单页面状态列表
	 * 
	 * @return
	 */
	public Map<Integer, String> queryOutWarehouseRecordStatusList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryOutWarehouseRecordStatusList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 查找数据库中已存在的所有非Z补货出库单的状态
	 * 
	 * @return
	 */
	public Map<Integer, String> getReplenishOutWarehouseRecordStatusList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService
				.getReplenishOutWarehouseRecordStatusList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取出入单类型集合
	 * 
	 * @return
	 */
	public Map<Integer, String> queryWarehouseRecordTypeList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryWarehouseRecordTypeList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取入库单页面类型集合
	 * 
	 * @return
	 */
	public Map<Integer, String> queryInWarehouseRecordTypeList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryInWarehouseRecordTypeList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取出库单页面类型集合
	 * 
	 * @return
	 */
	public Map<Integer, String> queryOutWarehouseRecordTypeList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryOutWarehouseRecordTypeList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取加工单类型集合
	 * 
	 * @return
	 */
	public Map<Integer, String> queryWarehouseAssembleRecordTypeList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.queryWarehouseAssembleRecordTypeList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 非Z补货出库单列表
	 * 
	 * @return
	 */
	public Map<Integer, String> getReplenishOutWarehouseRecordList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.getReplenishOutWarehouseRecordList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取所有实仓流水库存类型（出库）
	 * 
	 * @return
	 */
	public Map<Integer, String> getOutStockType() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.getOutStockType();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取所有实仓流水库存类型（入库）
	 * 
	 * @return
	 */
	public Map<Integer, String> getInStockType() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.getInStockType();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取所有实仓流水库存类型
	 * 
	 * @return
	 */
	public Map<Integer, String> getAllStockType() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.getAllStockType();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取所有库存类型枚举
	 * 
	 * @return
	 */
	public Map<Integer, String> getInventoryType() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.getInventoryType();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 获取所有质检状态枚举
	 * 
	 * @return
	 */
	public Map<Integer, String> getQualityStatus() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.getQualityStatus();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

	/**
	 * 查找数据库中已存在的所有非Z补货出库单的状态
	 * 
	 * @return
	 */
	public Map<Integer, String> getWmsSyncStatusList() {
		Response<Map<Integer, String>> response = showTypeStatusRemoteService.getWmsSyncStatusList();
		try {
			return response.getData();
		} catch (RomeException e) {
			log.error(e.getCode(), e.getMessage());
			return new HashMap<>();
		}
	}

}
