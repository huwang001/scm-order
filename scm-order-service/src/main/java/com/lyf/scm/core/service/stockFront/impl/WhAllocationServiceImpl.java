package com.lyf.scm.core.service.stockFront.impl;

import static java.math.BigDecimal.ROUND_DOWN;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.constants.WhAllocationConstants;
import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import com.lyf.scm.common.enums.SkuUnitTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.enums.YesOrNoEnum;
import com.lyf.scm.common.util.date.DateUtil;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.DispatchNoticeDTO;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.notify.TmsNotifyDTO;
import com.lyf.scm.core.api.dto.pack.DemandAllotDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.QueryPreCalculateDTO;
import com.lyf.scm.core.api.dto.stockFront.RealWarehouseParamDTO;
import com.lyf.scm.core.api.dto.stockFront.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.core.api.dto.stockFront.RwBatchDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationExportTemplate;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationPageDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationTemplateDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationTemplateDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.WhSkuUnitDTO;
import com.lyf.scm.core.config.RedisUtil;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.domain.convert.stockFront.BusinessReasonConvertor;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.WhAllocationConvertor;
import com.lyf.scm.core.domain.convert.stockFront.WhAllocationDetailConvertor;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.domain.entity.stockFront.AllotRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.BusinessReasonE;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.RecordRealVirtualStockSyncRelationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.lyf.scm.core.mapper.stockFront.AllotRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.BusinessReasonMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.RecordRealVirtualStockSyncRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.mapper.stockFront.WhAllocationDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WhAllocationMapper;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.dto.StorePurchaseAccessDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.sap.dto.SyncSapPoDTO;
import com.lyf.scm.core.remote.sap.facade.SapFacade;
import com.lyf.scm.core.remote.stock.AllocationTool;
import com.lyf.scm.core.remote.stock.dto.AllocationCalQtyParam;
import com.lyf.scm.core.remote.stock.dto.AllocationCalQtyRes;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;
import com.lyf.scm.core.remote.stock.dto.CancelResultDTO;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.PoNoDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseStockDTO;
import com.lyf.scm.core.remote.stock.dto.QueryVirtualWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.QueryVirtualWarehouseStockDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.dto.RealWarehouseStockDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouseWmsConfig;
import com.lyf.scm.core.remote.stock.dto.RecordDetailDTO;
import com.lyf.scm.core.remote.stock.dto.SkuRealVirtualStockSyncRelation;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouse;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouseStock;
import com.lyf.scm.core.remote.stock.dto.VwAllocationQty;
import com.lyf.scm.core.remote.stock.facade.StockFacade;
import com.lyf.scm.core.remote.stock.facade.StockQueryFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.remote.stock.facade.StockWhAllocationFacade;
import com.lyf.scm.core.remote.user.dto.EmployeeInfoDTO;
import com.lyf.scm.core.remote.user.facade.UserFacade;
import com.lyf.scm.core.service.order.OrderService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.pack.PackDemandComponentService;
import com.lyf.scm.core.service.stockFront.RecordRealVirtualStockSyncRelationService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordCommService;
import com.lyf.scm.core.service.stockFront.WhAllocationService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 类WhAllocationServiceImpl的实现描述：仓库调拨
 *
 * @author sunyj 2019/5/13 11:03
 */
@Service
@Slf4j
public class WhAllocationServiceImpl implements WhAllocationService {

	@Resource
	private WhAllocationMapper whAllocationMapper;

	@Resource
	private WhAllocationDetailMapper whAllocationDetailMapper;

	@Resource
	private WarehouseRecordMapper warehouseRecordMapper;

	@Resource
	private WarehouseRecordDetailMapper warehouseRecordDetailMapper;

	@Resource
	private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;

	@Resource
	private RecordRealVirtualStockSyncRelationMapper recordRealVirtualStockSyncRelationMapper;

	@Resource
	private BusinessReasonMapper businessReasonMapper;
	
	@Resource
	private AllotRecordRelationMapper allotRecordRelationMapper;
	
	@Autowired
	private RecordRealVirtualStockSyncRelationService recordRealVirtualStockSyncRelationService;
	
	@Autowired
    private OrderUtilService orderUtilService;
	
	@Autowired
	private WarehouseRecordCommService warehouseRecordCommService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private PackDemandComponentService packDemandComponentService;

	@Resource
	private StockRealWarehouseFacade stockRealWarehouseFacade;
	
	@Resource
	private StockWhAllocationFacade stockWhAllocationFacade;
	
	@Resource
	private StockQueryFacade stockQueryFacade;
	
	@Resource
	private StockFacade stockFacade;
	
	@Resource
	private StockRecordFacade stockRecordFacade;
	
	@Resource
	private ItemFacade itemFacade;

	@Resource
	private BaseFacade baseFacade;

	@Resource
	private SapFacade sapFacade;

	@Resource
	private UserFacade userFacade;

	@Resource
	private AllocationTool allocationTool;

	@Resource
	private ItemInfoTool itemInfoTool;

	@Resource
	private SkuQtyUnitTool skuQtyUnitTool;

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private WhAllocationConvertor whAllocationConvertor;

	@Resource
	private WhAllocationDetailConvertor whAllocationDetailConvertor;

	@Resource
	private BusinessReasonConvertor businessReasonConvertor;
	
	@Resource
	private StockInRecordDTOConvert stockInRecordDTOConvert;
	
	@Resource
	private StockRecordDTOConvert stockOutRecordDTOConvert;
	
	@Value("${spring.application.name}")
    private String appNmae;

	private final static Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{0,3})?$");

	/**
	 * 分布式锁定
	 */
	private final static String WH_ALLOT_DISPARITY_KEY = "createDisparityAllot";

	/**
	 * 自动释放锁的时间,超时时间10分钟
	 */
	private final static Integer WH_ALLOT_DISPARITY_TIME = 600;

	private final static String CLIENT_ID = "clientId";

	final BeanCopier detailCopier = BeanCopier.create(WhAllocationDetailE.class, WhAllocationExportTemplate.class,
			false);

	/**
	 * 保存仓库调拨单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveWhAllocation(WhAllocationDTO whAllocationDTO) {
		// 幂等校验
		WhAllocationE whAllocationE = whAllocationMapper.queryByRecordCode(whAllocationDTO.getRecordCode());
		AlikAssert.isTrue(whAllocationE == null, ResCode.ORDER_ERROR_6001, ResCode.ORDER_ERROR_6001_DESC);
		// 数据校验
		this.validData(whAllocationDTO);
		WhAllocationE whAllocation = whAllocationConvertor.convertDTO2E(whAllocationDTO);
		// 设置调拨类型为页面新增
		whAllocation.setAddType(WhAllocationConstants.ADD_TYPE_PAGE);
		// 页面的新增的原始数量和调拨数量一致
		for (WhAllocationDetailE detailE : whAllocation.getFrontRecordDetails()) {
			detailE.setOrginQty(detailE.getAllotQty());
		}
		whAllocation.setIsDisparity(WhAllocationConstants.IS_DISPARITY_FALSE);
		whAllocation.setOrginId(-1L);
		whAllocation.setRecordCode(orderUtilService.queryOrderCode(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getCode()));
		// 生成仓库调拨单
		this.addAllocation(whAllocation, false);
	}

	/**
	 * 数据校验
	 * 
	 * @param whAllocationDTO
	 */
	private void validData(WhAllocationDTO whAllocationDTO) {
		// 校验业务类型(必须是五种类型之一)
		List<Integer> typeList = WhAllocationConstants.ALLOCATION_TYPE_LIST;
		AlikAssert.isTrue(typeList.contains(whAllocationDTO.getBusinessType()), ResCode.ORDER_ERROR_6002,
				ResCode.ORDER_ERROR_6002_DESC);
		// 出入仓不能相等
		AlikAssert.isTrue(!whAllocationDTO.getInWarehouseId().equals(whAllocationDTO.getOutWarehouseId()),
				ResCode.ORDER_ERROR_6003, ResCode.ORDER_ERROR_6003_DESC);
		// 校验仓库是否存在
		RealWarehouse inWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
				whAllocationDTO.getInRealWarehouseCode(), whAllocationDTO.getInFactoryCode());
		AlikAssert.isNotNull(inWarehouse, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
		RealWarehouse outWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
				whAllocationDTO.getOutRealWarehouseCode(), whAllocationDTO.getOutFactoryCode());
		AlikAssert.isNotNull(outWarehouse, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
		// 内部调拨只能选择相同工厂
		if (WhAllocationConstants.INNER_ALLOCATION.equals(whAllocationDTO.getBusinessType())) {
			if (!inWarehouse.getFactoryCode().equals(outWarehouse.getFactoryCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_6006, ResCode.ORDER_ERROR_6006_DESC);
			}
		}
		// RDC调拨的工厂不能相同
		if (WhAllocationConstants.RDC_ALLOCATION.equals(whAllocationDTO.getBusinessType())) {
			if (inWarehouse.getFactoryCode().equals(outWarehouse.getFactoryCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_6007, ResCode.ORDER_ERROR_6007_DESC);
			}
		}
	}

	/**
	 * 生成仓库调拨单
	 * 
	 * @param whAllocationE
	 * @param isDisparity
	 */
	public void addAllocation(WhAllocationE whAllocationE, boolean isDisparity) {
		whAllocationE.setRecordType(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType());
		if (null == whAllocationE.getRecordStatus()) {
			whAllocationE.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
		}
		if (null == whAllocationE.getIsDiffIn()) {
			whAllocationE.setIsDiffIn(WhAllocationConstants.IS_DISPARITY_FALSE);
		}
		if (null == whAllocationE.getSyncStatus()) {
			whAllocationE.setSyncStatus(WhAllocationConstants.NOT_NEED_SYNC_STATUS);
		}
		if (null == whAllocationE.getWhType()) {
			whAllocationE.setWhType(WhAllocationConstants.WH_TYPE_INIT);
		}
		whAllocationMapper.saveWhAllocation(whAllocationE);
		for (int j = 0; j < whAllocationE.getFrontRecordDetails().size(); j++) {
			WhAllocationDetailE whAllocationDetailE = whAllocationE.getFrontRecordDetails().get(j);
			whAllocationDetailE.setFrontRecordId(whAllocationE.getId());
			whAllocationDetailE.setRecordCode(whAllocationE.getRecordCode());
			whAllocationDetailE.setInQty(BigDecimal.ZERO);
			whAllocationDetailE.setOutQty(BigDecimal.ZERO);
			if (isDisparity) {
				// 差异的单子的行号保持跟原单一致，方便更新原单
				whAllocationDetailE.setOriginLineNo(whAllocationDetailE.getLineNo());
			}
			Integer lineNo = (j + 1) * 10;
			whAllocationDetailE.setLineNo(String.format("%05d", lineNo));
		}
		whAllocationDetailMapper.saveWhAllocationDetail(whAllocationE.getFrontRecordDetails());
	}

	/**
	 * 修改仓库调拨申请
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateWhAllocation(WhAllocationDTO whAllocationDTO) {
		// 判断数据是否存在
		WhAllocationE whAllocationE = whAllocationMapper.queryByRecordCode(whAllocationDTO.getRecordCode());
		AlikAssert.isTrue(whAllocationE != null, ResCode.ORDER_ERROR_6001, ResCode.ORDER_ERROR_6001_DESC);
		// 数据校验
		this.validData(whAllocationDTO);
		// 删除订单明细
		whAllocationDetailMapper.deleteDetailByFrontId(whAllocationDTO.getId());
		// 更新仓库调拨单、插入明细
		WhAllocationE whAllocation = whAllocationConvertor.convertDTO2E(whAllocationDTO);
		this.updateAllocation(whAllocation);
		// 更新出库数量
		Integer isDisparity = WhAllocationConstants.IS_DISPARITY_FALSE;
		List<WhAllocationDetailE> whAllocationDetailEList = whAllocation.getFrontRecordDetails();
		for (WhAllocationDetailE whAllocationDetailE : whAllocationDetailEList) {
			whAllocationDetailE.setOutQty(whAllocationDetailE.getAllotQty());
			// 判断是否有差异
			if (whAllocationDetailE.getAllotQty().compareTo(whAllocationDetailE.getOrginQty()) != 0) {
				isDisparity = WhAllocationConstants.IS_DISPARITY_TRUE;
			}
		}
		// 更新差异状态
		whAllocationMapper.updateDisparityStatus(whAllocationE.getId(), isDisparity);
	}

	/**
	 * 更新更新仓库调拨单、插入明细
	 * 
	 * @param whAllocationE
	 */
	private void updateAllocation(WhAllocationE whAllocationE) {
		Integer updateNum = whAllocationMapper.updateWhAllocation(whAllocationE);
		AlikAssert.isTrue(updateNum > 0, ResCode.ORDER_ERROR_6008, ResCode.ORDER_ERROR_6008_DESC);
		for (int j = 0; j < whAllocationE.getFrontRecordDetails().size(); j++) {
			WhAllocationDetailE whAllocationDetailE = whAllocationE.getFrontRecordDetails().get(j);
			whAllocationDetailE.setFrontRecordId(whAllocationE.getId());
			whAllocationDetailE.setRecordCode(whAllocationE.getRecordCode());
			whAllocationDetailE.setInQty(BigDecimal.ZERO);
			whAllocationDetailE.setOutQty(BigDecimal.ZERO);
			Integer lineNo = (j + 1) * 10;
			whAllocationDetailE.setLineNo(String.format("%05d", lineNo));
		}
		whAllocationDetailMapper.saveWhAllocationDetail(whAllocationE.getFrontRecordDetails());
	}

	/**
	 * 根据调拨单ID查询（预）下市商品
	 */
	@Override
	public List<String> queryMarketDownSku(Long id) {
		List<String> undercarriageCodes = new ArrayList<>();
		// 查询仓库调拨单（包含明细）
		WhAllocationE whAllocationE = this.queryWithDetailById(id);
		AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6001, ResCode.ORDER_ERROR_6001_DESC);
		RealWarehouse inWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
				whAllocationE.getInRealWarehouseCode(), whAllocationE.getInFactoryCode());
		AlikAssert.isNotNull(inWarehouse, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
		whAllocationE.setInWarehouse(inWarehouse);
		this.calculateForCreateDisparity(whAllocationE, undercarriageCodes, false);
		return undercarriageCodes;
	}

	/**
	 * 查询存在差异的明细，并剔除预下市商品
	 * 
	 * @param whAllocationE
	 * @param undercarriageCodes
	 * @param isDisparity
	 * @return
	 */
	private List<WhAllocationDetailE> calculateForCreateDisparity(WhAllocationE whAllocationE,
			List<String> undercarriageCodes, boolean isDisparity) {
		List<WhAllocationDetailE> whAllocationDetailEList = new ArrayList<WhAllocationDetailE>();
		Set<String> codes = new HashSet<>();
		List<String> lineNos = new ArrayList<>();
		for (WhAllocationDetailE whAllocationDetailE : whAllocationE.getFrontRecordDetails()) {
			if (isDisparity) {
				if (whAllocationDetailE.getAllotQty().compareTo(whAllocationDetailE.getOrginQty()) < 0) {
					codes.add(whAllocationDetailE.getSkuCode());
					lineNos.add(whAllocationDetailE.getLineNo());
				}
			} else {
				codes.add(whAllocationDetailE.getSkuCode());
				lineNos.add(whAllocationDetailE.getLineNo());
			}
		}
		if (codes.size() > 0) {
			String factoryCode = whAllocationE.getInWarehouse().getFactoryCode();
			// 调用商品中心接口查询预下市商品
			List<StorePurchaseAccessDTO> res = itemFacade.getStoreAccessFromSAPBySkuCodesAndStoreCode(factoryCode,
					new ArrayList<>(codes));
			Map<String, StorePurchaseAccessDTO> resMap = res.stream()
					.collect(Collectors.toMap(StorePurchaseAccessDTO::getSkuCode, Function.identity(), (v1, v2) -> v1));
			for (WhAllocationDetailE whAllocationDetailE : whAllocationE.getFrontRecordDetails()) {
				String skuCode = whAllocationDetailE.getSkuCode();
				if (codes.contains(skuCode) && lineNos.contains(whAllocationDetailE.getLineNo())) {
					StorePurchaseAccessDTO accessDTO = resMap.get(whAllocationDetailE.getSkuCode());
					boolean flag = null != accessDTO
							&& ("01".equals(accessDTO.getIsAccess()) || "02".equals(accessDTO.getIsAccess()));
					if (flag) {
						undercarriageCodes.add(skuCode);
					} else {
						// 如果返回的code中不包含，则表示需要调拨,调拨数量设置为差异数量
						// 差异调拨的需要计算调拨数量，非差异流程只需要过滤掉下市商品
						if (isDisparity) {
							whAllocationDetailE.setAllotQty(whAllocationDetailE.getOrginQty().subtract(whAllocationDetailE.getAllotQty()));
							whAllocationDetailE.setOrginQty(whAllocationDetailE.getAllotQty());
						}
						whAllocationDetailEList.add(whAllocationDetailE);
					}
				}
			}
		}
		return whAllocationDetailEList;
	}

	/**
	 * 根据ID查询仓库调拨单（包含明细）
	 * 
	 * @param id
	 * @return
	 */
	private WhAllocationE queryWithDetailById(Long id) {
		WhAllocationE whAllocationE = whAllocationMapper.queryById(id);
		if (whAllocationE == null) {
			return null;
		}
		List<WhAllocationDetailE> whAllocationDetailEList = whAllocationDetailMapper
				.queryDetailByFrontIds(Arrays.asList(whAllocationE.getId()));
		if (CollectionUtils.isEmpty(whAllocationDetailEList)) {
			return null;
		}
		whAllocationE.setFrontRecordDetails(whAllocationDetailEList);
		return whAllocationE;
	}

	/**
	 * 根据单据编号查询仓库调拨单（包含明细）
	 * 
	 * @param id
	 * @return
	 */
	private WhAllocationE queryWithDetailByCode(String recordCode) {
		WhAllocationE whAllocationE = whAllocationMapper.queryByRecordCode(recordCode);
		if (whAllocationE == null) {
			return null;
		}
		List<WhAllocationDetailE> whAllocationDetailEList = whAllocationDetailMapper
				.queryDetailByFrontIds(Arrays.asList(whAllocationE.getId()));
		if (CollectionUtils.isEmpty(whAllocationDetailEList)) {
			return null;
		}
		whAllocationE.setFrontRecordDetails(whAllocationDetailEList);
		return whAllocationE;
	}

	/**
	 * 确认调拨申请
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String confimWhAllocation(Long id, Long userId) {
		// 查询仓库调拨单（包含明细）
		WhAllocationE whAllocationE = this.queryWithDetailById(id);
		AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6001, ResCode.ORDER_ERROR_6001_DESC);
		// 查询入库仓库
		RealWarehouse inWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
				whAllocationE.getInRealWarehouseCode(), whAllocationE.getInFactoryCode());
		AlikAssert.isNotNull(inWarehouse, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
		whAllocationE.setInWarehouse(inWarehouse);
		/***** 发货单单位取整以及虚仓预计算处理 开始 ***/
		List<AllocationCalQtyRes> preResult = this.preCalculate(whAllocationE, true);
		Map<String, AllocationCalQtyRes> resultMap = preResult.stream()
				.collect(Collectors.toMap(AllocationCalQtyRes::getLineNo, Function.identity(), (v1, v2) -> v1));
		List<WhAllocationDetailE> updateList = this.resetOriginQtyByPreResult(whAllocationE, resultMap);
		// 更新原始需求数 以及调拨数
		if (updateList.size() > 0) {
			whAllocationDetailMapper.updateDetailOriginAndAllotQty(updateList);
		}
		/***** 发货单单位取整以及虚仓预计算处理 结束 ***/

		this.queryContainerAttr(whAllocationE);
		Integer whType = this.dealWhType(whAllocationE);
		// 构造确认申请对象
		WhAllocationE whAllocation = new WhAllocationE();
		whAllocation.setId(id);
		whAllocation.setModifier(userId);
		whAllocation.setWhType(whType);
		// 需要产生PO
		whAllocation.setSyncStatus(WhAllocationConstants.WAIT_SYNC_STATUS);
		// 确认调拨申请
		Integer i = whAllocationMapper.updateAuditSuccess(whAllocation);
		AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_6012, ResCode.ORDER_ERROR_6012_DESC);

		List<String> undercarriageCodes = new ArrayList<>();
		List<WhAllocationDetailE> needHandleDetails = this.calculateForCreateDisparity(whAllocationE,
				undercarriageCodes, false);
		AlikAssert.isTrue(needHandleDetails.size() > 0, ResCode.ORDER_ERROR_1003,
				ResCode.ORDER_ERROR_1003_DESC + "：所有物料均为预下市或下市物料");
		// 过滤（预）下市商品并更新预下市商品的调拨数量
		if (CollectionUtils.isNotEmpty(undercarriageCodes)) {
			List<WhAllocationDetailE> updateDetails = new ArrayList<>();
			for (WhAllocationDetailE detailE : whAllocationE.getFrontRecordDetails()) {
				if (undercarriageCodes.contains(detailE.getSkuCode())) {
					detailE.setAllotQty(BigDecimal.ZERO);
					updateDetails.add(detailE);
				}
			}
			// 更新前置单
			if (CollectionUtils.isNotEmpty(updateDetails)) {
				whAllocationDetailMapper.updateDetailAllotQty(updateDetails);
			}
		}
		// 按集装散零属性分类分别处理
		Map<String, List<WhAllocationDetailE>> containerMap = needHandleDetails.stream()
				.collect(Collectors.groupingBy(WhAllocationDetailE::getContainer));
		for (List<WhAllocationDetailE> whAllocationDetailE : containerMap.values()) {
			whAllocationE.setFrontRecordDetails(whAllocationDetailE);
			// 出入都是中台或者出仓为中台
			if (whType.equals(WhAllocationConstants.WH_TYPE_ALL_MIDDLE)
					|| whType.equals(WhAllocationConstants.WH_TYPE_OUT_MIDDLE)) {
				// 根据仓库调拨单创建出库单
				// 说明一下 :这里的resultMap 在createOutOrderByWhAllocation 方法中被改变了引用,即行号变了,所以返回了新的
				resultMap = this.createOutOrderByWhAllocation(whAllocationE, resultMap, undercarriageCodes.size() > 0);
			}
			// 入仓为中台出仓不为中台,创建入库单,增加入仓在途
			if (whType.equals(WhAllocationConstants.WH_TYPE_IN_MIDDLE)) {
				// 更新出库数量
				Integer isDisparity = WhAllocationConstants.IS_DISPARITY_FALSE;
				// 对行号重新编排
				this.resetLineNo(whAllocationE, resultMap);
				resultMap = new ArrayList<>(resultMap.values()).stream()
						.collect(Collectors.toMap(AllocationCalQtyRes::getLineNo, Function.identity(), (v1, v2) -> v1));
				List<WhAllocationDetailE> whAllocationDetailEList = whAllocationE.getFrontRecordDetails();
				for (WhAllocationDetailE detail : whAllocationDetailEList) {
					detail.setOutQty(detail.getAllotQty());
					// 判断是否有差异
					if (detail.getAllotQty().compareTo(detail.getOrginQty()) != 0) {
						isDisparity = WhAllocationConstants.IS_DISPARITY_TRUE;
					}
				}
				// 有预下市物料的,默认就是差异单
				if (undercarriageCodes.size() > 0) {
					isDisparity = WhAllocationConstants.IS_DISPARITY_TRUE;
				}
				// 更新差异状态
				whAllocationMapper.updateDisparityStatus(whAllocationE.getId(), isDisparity);
				// 更新实出
				whAllocationDetailMapper.updateDetailOutQty(whAllocationDetailEList);
				
				// 根据ID修改仓库调拨单状态recordStatus=4（已派车）
				int row = whAllocationMapper.updateDispatchSuccess(whAllocationE.getId());
				AlikAssert.isTrue(row == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
				// 出非中台入中台直接修改为已出库
				if (WhAllocationConstants.WH_TYPE_IN_MIDDLE.equals(whAllocationE.getWhType())) {
					row = whAllocationMapper.updateDeliverySuccess(whAllocationE.getId());
					AlikAssert.isTrue(row == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
				}
				
				// 生成入库单
				WarehouseRecordE inRecordE = new WarehouseRecordE();
				inRecordE.setExpectReceiveDateStart(whAllocationE.getExpeAogTime());
				// 根据仓库调拨单生成大仓入库单
				this.whInRecordByWhAllocation(inRecordE, whAllocationE);
			}
		}
		return packageConfirmReturnInfo(updateList);
	}

	/**
	 * 锁定库存预计算
	 * 
	 * @param whAllocationE
	 * @param isCalVmAllocation
	 * @return
	 */
	private List<AllocationCalQtyRes> preCalculate(WhAllocationE whAllocationE, boolean isCalVmAllocation) {
		RealWarehouse outWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
				whAllocationE.getOutRealWarehouseCode(), whAllocationE.getOutFactoryCode());
		AlikAssert.isNotNull(outWarehouse, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
		List<AllocationCalQtyParam> paramList = new ArrayList<>();
		for (WhAllocationDetailE whAllocationDetailE : whAllocationE.getFrontRecordDetails()) {
			AllocationCalQtyParam param = new AllocationCalQtyParam();
			param.setLineNo(whAllocationDetailE.getLineNo());
			param.setSkuId(whAllocationDetailE.getSkuId());
			param.setSkuCode(whAllocationDetailE.getSkuCode());
			param.setUnitCode(whAllocationDetailE.getUnitCode());
			param.setPlanQty(whAllocationDetailE.getOrginQty());
			paramList.add(param);
		}
		if (isCalVmAllocation) {
			// recordE.getOrginRecord() 不为空，表示是差异调拨单，需要用原单配置
			return allocationTool.calculateVmQtyAndRound(whAllocationE.getInWarehouse().getRealWarehouseType(),
					outWarehouse.getRealWarehouseType(), outWarehouse.getId(), paramList,
					whAllocationE.getOrginRecord() == null ? whAllocationE.getRecordCode()
							: whAllocationE.getOrginRecord());
		} else {
			// 只做单位取整，虚仓预计算不做
			return allocationTool.calculateRound(whAllocationE.getInWarehouse().getRealWarehouseType(),
					outWarehouse.getRealWarehouseType(), paramList);
		}
	}

	/**
	 * 出入库单虚仓预计算
	 *
	 * @param details
	 * @return
	 */
	private List<AllocationCalQtyRes> preCalculate(List<WarehouseRecordDetailE> details, String recordCode, Long rwId) {
		List<AllocationCalQtyParam> paramList = new ArrayList<>();
		for (WarehouseRecordDetailE detail : details) {
			AllocationCalQtyParam param = new AllocationCalQtyParam();
			param.setLineNo(detail.getLineNo());
			param.setSkuId(detail.getSkuId());
			param.setSkuCode(detail.getSkuCode());
			// 这里是要按基本单位进行预计算，基本单位就可以不用传单位
			// param.setUnitCode(detail.getUnitCode());
			param.setPlanQty(detail.getActualQty());
			paramList.add(param);
		}
		return allocationTool.calculateVmQty(paramList, recordCode, rwId);
	}

	/**
	 * 根据与计算结果重置需求数，没有考虑一个sku多行的情况，如果有这种场景，需要重新设计
	 *
	 * @param recordE
	 * @param resultMap
	 */
	private List<WhAllocationDetailE> resetOriginQtyByPreResult(WhAllocationE whAllocationE,
			Map<String, AllocationCalQtyRes> resultMap) {
		List<WhAllocationDetailE> updateList = new ArrayList<>();
		for (WhAllocationDetailE whAllocationDetailE : whAllocationE.getFrontRecordDetails()) {
			AllocationCalQtyRes pre = resultMap.get(whAllocationDetailE.getLineNo());
			if (pre.getIsChangedPlan()) {
				whAllocationDetailE.setPlanOrigin(whAllocationDetailE.getOrginQty());
				whAllocationDetailE.setPlanUnitCode(whAllocationDetailE.getUnitCode());
				// 换成发货单位发货数量，避免前置单位除不尽的情况
				whAllocationDetailE.setOrginQty(pre.getActualQty());
				whAllocationDetailE.setAllotQty(pre.getActualQty());
				whAllocationDetailE.setUnitCode(pre.getActualUnitCode());
				whAllocationDetailE.setUnit(pre.getActualUnitName());
				updateList.add(whAllocationDetailE);
			}
		}
		return updateList;
	}

	/**
	 * 查询是否集装箱信息
	 */
	private void queryContainerAttr(WhAllocationE whAllocationE) {
		RealWarehouse inWarehouse = whAllocationE.getInWarehouse();
		if (WhAllocationConstants.SPLIT_ORDER_FACTORY_CODE.equals(inWarehouse.getFactoryCode())) {
			// X007需要拆装分组
			for (WhAllocationDetailE whAllocationDetailE : whAllocationE.getFrontRecordDetails()) {
				// 特意将skuId清空 然后查询商品中心接口查询集装箱信息
				whAllocationDetailE.setSkuId(null);
			}
			itemInfoTool.convertSkuCode(whAllocationE.getFrontRecordDetails());
			for (WhAllocationDetailE whAllocationDetailE : whAllocationE.getFrontRecordDetails()) {
				// 暂时将该字段写死，后续注释掉
				whAllocationDetailE.setContainer(WhAllocationConstants.SPLIT_TYPE_CONTAINER);
			}
		} else {
			itemInfoTool.convertSkuCode(whAllocationE.getFrontRecordDetails());
			for (WhAllocationDetailE whAllocationDetailE : whAllocationE.getFrontRecordDetails()) {
				// 特意将skuId清空 然后查询商品中心接口查询集装箱信息
				whAllocationDetailE.setContainer(WhAllocationConstants.SPLIT_TYPE_CONTAINER);
			}
		}
	}

	/**
	 * 设置应用中台类型
	 * 
	 * @param whAllocationE
	 * @return
	 */
	private Integer dealWhType(WhAllocationE whAllocationE) {
		Integer whType = null;
		RealWarehouseWmsConfig inWhConfig = null;
		RealWarehouseWmsConfig outWhConfig = null;
		Long inWarehouseId = whAllocationE.getInWarehouseId();
		String inRealWarehouseCode = whAllocationE.getInRealWarehouseCode();
		String inFactoryCode = whAllocationE.getInFactoryCode();
		QueryRealWarehouseDTO inQueryRealWarehouse = new QueryRealWarehouseDTO();
		inQueryRealWarehouse.setWarehouseOutCode(inRealWarehouseCode);
		inQueryRealWarehouse.setFactoryCode(inFactoryCode);

		Long outWarehouseId = whAllocationE.getOutWarehouseId();
		String outRealWarehouseCode = whAllocationE.getOutRealWarehouseCode();
		String outFactoryCode = whAllocationE.getOutFactoryCode();
		QueryRealWarehouseDTO outQueryRealWarehouse = new QueryRealWarehouseDTO();
		outQueryRealWarehouse.setWarehouseOutCode(outRealWarehouseCode);
		outQueryRealWarehouse.setFactoryCode(outFactoryCode);
		List<QueryRealWarehouseDTO> queryRealWarehouseList = new ArrayList<QueryRealWarehouseDTO>();
		queryRealWarehouseList.add(inQueryRealWarehouse);
		queryRealWarehouseList.add(outQueryRealWarehouse);
		// 查询配置
		List<RealWarehouseWmsConfig> list = stockRealWarehouseFacade
				.queryWmsConfigByWarehouseCodeAndFactoryCode(queryRealWarehouseList);
		Map<Long, RealWarehouseWmsConfig> configMap = list.stream().collect(
				Collectors.toMap(RealWarehouseWmsConfig::getRealWarehouseId, Function.identity(), (v1, v2) -> v1));
		// 设置出入库仓库配置
		inWhConfig = configMap.get(inWarehouseId);
		outWhConfig = configMap.get(outWarehouseId);
		// 设置whType
		if (outWhConfig != null && inWhConfig != null) {
			whType = WhAllocationConstants.WH_TYPE_ALL_MIDDLE;
		} else if (outWhConfig != null && inWhConfig == null) {
			whType = WhAllocationConstants.WH_TYPE_OUT_MIDDLE;
		} else if (outWhConfig == null && inWhConfig != null) {
			whType = WhAllocationConstants.WH_TYPE_IN_MIDDLE;
		} else {
			throw new RomeException(ResCode.ORDER_ERROR_6011, ResCode.ORDER_ERROR_6011_DESC);
		}
		return whType;
	}

	/**
	 * 对行号重新编排
	 * 
	 * @param whAllocationE
	 * @param resultMap
	 */
	private void resetLineNo(WhAllocationE whAllocationE, Map<String, AllocationCalQtyRes> resultMap) {
		// 对行号重新编排
		List<WhAllocationDetailE> detailETempList = whAllocationDetailMapper
				.queryDetailByFrontIdsOrderBySkuQty(whAllocationE.getId());
		Map<Long, WhAllocationDetailE> oldDetailMap = whAllocationE.getFrontRecordDetails().stream()
				.collect(Collectors.toMap(WhAllocationDetailE::getId, Function.identity(), (v1, v2) -> v1));
		int j = 0;
		// 对行号重新编排
		for (WhAllocationDetailE detailE : detailETempList) {
			Integer lineNo = (j + 1) * 10;
			detailE.setLineNo(String.format("%05d", lineNo));
			j++;
			if (oldDetailMap.containsKey(detailE.getId())) {
				if (resultMap.containsKey(oldDetailMap.get(detailE.getId()).getLineNo())) {
					resultMap.get(oldDetailMap.get(detailE.getId()).getLineNo())
							.setLineNo(String.format("%05d", lineNo));
				}
				oldDetailMap.get(detailE.getId()).setLineNo(String.format("%05d", lineNo));
			}
		}
		whAllocationDetailMapper.updateDetailLionNo(detailETempList);
	}

	/**
	 * 根据仓库调拨单创建出库单
	 * 
	 * @param recordE
	 * @param resultMap
	 * @param hasMarketDownSku
	 */
	private Map<String, AllocationCalQtyRes> createOutOrderByWhAllocation(WhAllocationE whAllocationE,
			Map<String, AllocationCalQtyRes> resultMap, boolean hasMarketDownSku) {
		// 生成出入库单
		WarehouseRecordE outRecordE = new WarehouseRecordE();
		// 设置派车状态待下发
		outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
		// 根据库存和箱单位重新设置调拨数量
		Integer isDisparity = this.resetDetailListByRwStock(whAllocationE, resultMap, false, true);

		this.resetLineNo(whAllocationE, resultMap);
		// 调拨单行号重排后,计算结果resultMap也需要重新排
		resultMap = new ArrayList<>(resultMap.values()).stream()
				.collect(Collectors.toMap(AllocationCalQtyRes::getLineNo, Function.identity(), (v1, v2) -> v1));

		if (hasMarketDownSku) {
			isDisparity = WhAllocationConstants.IS_DISPARITY_TRUE;
		}
		// 更新差异状态
		whAllocationMapper.updateDisparityStatus(whAllocationE.getId(), isDisparity);
		// 根据仓库调拨单生成大仓出库单
		this.whOutRecordByWhAllocation(outRecordE, whAllocationE, new ArrayList<>(resultMap.values()));
		return resultMap;
	}

	/**
	 * 构建出库单同步库存中心
	 * 
	 * @param whAllocationE
	 * @param outRecordE
	 * @param preResult
	 * @return
	 */
	private OutWarehouseRecordDTO buildOutRecord(WhAllocationE whAllocationE, WarehouseRecordE outRecordE, List<AllocationCalQtyRes> preResult) {
		OutWarehouseRecordDTO outWarehouseRecordDTO = stockOutRecordDTOConvert.convertE2OutDTO(outRecordE);
		if (WhAllocationConstants.QUALITY_ALLOCATE.equals(whAllocationE.getIsQualityAllotcate())) {
			outWarehouseRecordDTO.setCheckRealStock(false);
		} else {
			outWarehouseRecordDTO.setCheckRealStock(true);
		}
		outWarehouseRecordDTO.setPreResult(preResult);
		
		return outWarehouseRecordDTO;
	}

	/**
	 * 根据仓库调拨单生成大仓出库单
	 * 
	 * @param outRecordE
	 * @param whAllocationE
	 * @param preResult
	 */
	private void whOutRecordByWhAllocation(WarehouseRecordE outRecordE, WhAllocationE whAllocationE, List<AllocationCalQtyRes> preResult) {
		// 生成单据号并设置基础数据
		outRecordE.setRecordCode(orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.WH_ALLOCATION_OUT_WAREHOUSE_RECORD.getCode()));
		outRecordE.setRecordType(WarehouseRecordTypeEnum.WH_ALLOCATION_OUT_WAREHOUSE_RECORD.getType());
		outRecordE.setRealWarehouseId(whAllocationE.getOutWarehouseId());
		outRecordE.setRealWarehouseCode(whAllocationE.getOutRealWarehouseCode());
		outRecordE.setFactoryCode(whAllocationE.getOutFactoryCode());
		outRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
		outRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
		outRecordE.setMerchantId(whAllocationE.getMerchantId());
		outRecordE.setOutCreateTime(whAllocationE.getOutCreateTime());
		outRecordE.setSyncTmsbStatus(0);
		outRecordE.setSapOrderCode(whAllocationE.getSapPoNo());
		outRecordE.setVirtualWarehouseCode(outRecordE.getVwCode());
		// 插入单据
		List<WhAllocationDetailE> WhAllocationDetailEList = whAllocationE.getFrontRecordDetails();
		List<WarehouseRecordDetailE> warehouseRecordDetailEList = new ArrayList<WarehouseRecordDetailE>();
		for (WhAllocationDetailE detailE : WhAllocationDetailEList) {
			// 数量为0的不写入明细
			if (BigDecimal.ZERO.compareTo(detailE.getBasicSkuQty()) == 0) {
				continue;
			}
			WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
			warehouseRecordDetailE.setRecordCode(outRecordE.getRecordCode());
			warehouseRecordDetailE.setSkuId(detailE.getSkuId());
			warehouseRecordDetailE.setSkuCode(detailE.getSkuCode());
			// 此处设置计划数量和实际数量一致，wms回调再更新实际数量，无需wms回调的业务，直接设置成一致即可
			warehouseRecordDetailE.setPlanQty(detailE.getBasicSkuQty());
			warehouseRecordDetailE.setUnit(detailE.getBasicUnit());
			warehouseRecordDetailE.setUnitCode(detailE.getBasicUnitCode());
			warehouseRecordDetailE.setActualQty(BigDecimal.ZERO);
			warehouseRecordDetailE.setSapPoNo(whAllocationE.getSapPoNo());
			warehouseRecordDetailE.setLineNo(detailE.getLineNo());
			warehouseRecordDetailE.setDeliveryLineNo(String.valueOf(detailE.getId()));
			warehouseRecordDetailEList.add(warehouseRecordDetailE);
		}
		AlikAssert.isTrue(warehouseRecordDetailEList.size() > 0, ResCode.ORDER_ERROR_6014, "当前调拨的单所有物料库存不足");
		// 设置skuCode和skuID
		itemInfoTool.convertSkuCode(warehouseRecordDetailEList);
		if (outRecordE.getSyncDispatchStatus() == null) {
			outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.INIT_DISPATCH);
		}
		if (outRecordE.getSyncTransferStatus() == null) {
			outRecordE.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
		}
		if (outRecordE.getSyncTradeStatus() == null) {
			outRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
		}
		// 保存后置单
		warehouseRecordMapper.insertWarehouseRecord(outRecordE);
		Long id = outRecordE.getId();
		warehouseRecordDetailEList.forEach(e -> {
			e.setWarehouseRecordId(id);
		});
		outRecordE.setWarehouseRecordDetailList(warehouseRecordDetailEList);
		// 保存后置单明细
		warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordDetailEList);
		// 保存仓库调拨单和后置单关联关系表
		FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
		relation.setWarehouseRecordId(outRecordE.getId());
		relation.setFrontRecordId(whAllocationE.getId());
		relation.setFrontRecordType(whAllocationE.getRecordType());
		relation.setRecordCode(outRecordE.getRecordCode());
		relation.setFrontRecordCode(whAllocationE.getRecordCode());
		frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
		
		//构建同步库存中心的出库单对象
		OutWarehouseRecordDTO outWarehouseRecordDTO = this.buildOutRecord(whAllocationE, outRecordE, preResult);
		//出库单同步库存中心
		stockRecordFacade.createOutRecord(outWarehouseRecordDTO);
	}

	/**
	 * 根据库存和箱单位重新设置调拨数量
	 *
	 * @param whAllocationE
	 * @param resultMap
	 *            虚仓预计算的分配结果
	 * @param isDisparityCreate
	 *            是否差异创建流程
	 * @param isDisparityCreate
	 *            是否考虑虚仓库存是否充足
	 */
	private Integer resetDetailListByRwStock(WhAllocationE whAllocationE, Map<String, AllocationCalQtyRes> resultMap,
			boolean isDisparityCreate, boolean isNeedCalVwStock) {
		Integer isDisparity = WhAllocationConstants.IS_DISPARITY_FALSE;
		List<WhAllocationDetailE> updateDetails = new ArrayList<>();
		RealWarehouse inWarehouse = whAllocationE.getInWarehouse();
		List<WhAllocationDetailE> whAllocationDetailEList = whAllocationE.getFrontRecordDetails();
		// 查询仓库库存
		List<Long> ids = whAllocationDetailEList.stream().map(WhAllocationDetailE::getSkuId).distinct()
				.collect(Collectors.toList());

		QueryRealWarehouseStockDTO queryRealWarehouseStockDTO = new QueryRealWarehouseStockDTO();
		queryRealWarehouseStockDTO.setRealWarehouseOutCode(whAllocationE.getOutRealWarehouseCode());
		queryRealWarehouseStockDTO.setFactoryCode(whAllocationE.getOutFactoryCode());
		queryRealWarehouseStockDTO.setIsQualityAllocate(whAllocationE.getIsQualityAllotcate());
		queryRealWarehouseStockDTO.setSkuIds(ids);
		List<RealWarehouseStockDTO> stockList = stockWhAllocationFacade.listStockBySkuWhIdForWhAllot(queryRealWarehouseStockDTO);

		Map<Long, RealWarehouseStockDTO> stockMap = stockList.stream()
				.collect(Collectors.toMap(RealWarehouseStockDTO::getSkuId, Function.identity(), (v1, v2) -> v1));
		Map<Long, Map<Long, VirtualWarehouseStock>> vwStockMap = new HashMap<>();
		if (isNeedCalVwStock) {
			vwStockMap = this.queryVmStock(whAllocationE.getOutRealWarehouseCode(), whAllocationE.getOutFactoryCode(),
					ids);
		}
		// 处理单位换算
		whAllocationDetailEList.forEach(e -> e.setSkuQty(e.getAllotQty()));
		skuQtyUnitTool.convertRealToBasic(whAllocationDetailEList);
		Set<Long> skuIdSet = new HashSet<>();
		for (WhAllocationDetailE detailE : whAllocationDetailEList) {
			AlikAssert.isNotNull(detailE.getBasicSkuQty(), ResCode.ORDER_ERROR_1001,
					"获取物料" + detailE.getSkuCode() + "单位" + detailE.getUnit() + "换算比例失败");
			BigDecimal basicQty = detailE.getBasicSkuQty();
			BigDecimal allotQty = detailE.getAllotQty();
			boolean isNeedUpdate = false;
			// 入库仓是非团购的仓库的需要按照中台库存计算
			AllocationCalQtyRes skuAllocationCalQtyRes = resultMap.get(detailE.getLineNo());

			boolean sufficient = true;
			if (!RealWarehouseTypeEnum.RW_TYPE_9.getType().equals(inWarehouse.getRealWarehouseType())) {
				RealWarehouseStockDTO stock = stockMap.get(detailE.getSkuId());
				if (stock != null) {
					// 质量调拨取真实库存
					BigDecimal leftStock = stock.getRealQty().subtract(stock.getLockQty());
					if (WhAllocationConstants.QUALITY_ALLOCATE.equals(whAllocationE.getIsQualityAllotcate())) {
						leftStock = stock.getRealQty();
					}
					if (leftStock.compareTo(BigDecimal.ZERO) == 0) {
						basicQty = BigDecimal.ZERO;
						allotQty = BigDecimal.ZERO;
						isNeedUpdate = true;
						sufficient = false;
					} else if (leftStock.compareTo(detailE.getBasicSkuQty()) < 0) {
						sufficient = false;
						// 实仓库存不足时，需要再次考虑发货单位取整问题
						if (skuAllocationCalQtyRes != null) {
							// 重新计算发货单位取整以及虚仓预分配数
							skuAllocationCalQtyRes.setPlanBasicQty(leftStock);
							allocationTool.reCalculateVmQtyForOneSku(skuAllocationCalQtyRes);
							basicQty = skuAllocationCalQtyRes.getActualBasicQty();
							allotQty = skuAllocationCalQtyRes.getActualQty();
							isNeedUpdate = true;
						} else {
							// 这里正常情况下不会走到，其实是可去掉的
							basicQty = leftStock;
							// 基本单位除以前置单位换单比例
							allotQty = basicQty.divide(detailE.getScale(), CommonConstants.DECIMAL_POINT_NUM,
									ROUND_DOWN);
							isNeedUpdate = true;
						}
					}
					stock.setRealQty(stock.getRealQty().subtract(basicQty));
				} else {
					sufficient = false;
					basicQty = BigDecimal.ZERO;
					allotQty = BigDecimal.ZERO;
					isNeedUpdate = true;
				}

				// 计算虚仓
				if (vwStockMap.containsKey(detailE.getSkuId())) {
					// 判断是否该实仓是否有虚仓
					if (skuAllocationCalQtyRes != null) {
						Map<Long, VirtualWarehouseStock> vmStock = vwStockMap.get(detailE.getSkuId());
						if (CollectionUtils.isNotEmpty(skuAllocationCalQtyRes.getVwAllocationQtyList())) {
							isNeedUpdate = true;
							// 不为空说明有虚仓配置
							basicQty = this.vwAllocationCal(skuAllocationCalQtyRes, vmStock,
									WhAllocationConstants.QUALITY_ALLOCATE
											.equals(whAllocationE.getIsQualityAllotcate()));
							// 虚仓库存重置
							for (VwAllocationQty vwAllocationQty : skuAllocationCalQtyRes.getVwAllocationQtyList()) {
								if (vwAllocationQty.getRealSkuQty().compareTo(BigDecimal.ZERO) > 0) {
									BigDecimal temp = vmStock.get(vwAllocationQty.getVirtualWarehouseId()).getRealQty();
									vmStock.get(vwAllocationQty.getVirtualWarehouseId())
											.setRealQty(temp.subtract(vwAllocationQty.getRealSkuQty()));
								}
							}
							if (skuAllocationCalQtyRes.getExistInSufficient()) {
								// 存在某个虚仓库存不足
								sufficient = false;
							}
						}
					}
				}
			}
			if (!sufficient && skuIdSet.contains(detailE.getSkuId())) {
				// 重复物料且库存不足时,抛异常
				throw new RomeException(ResCode.ORDER_ERROR_1003, "有重复物料,且库存不足,不分配");
			} else {
				skuIdSet.add(detailE.getSkuId());
			}
			detailE.setBasicSkuQty(basicQty);
			detailE.setAllotQty(allotQty);
			if (isNeedUpdate) {
				updateDetails.add(detailE);
			}
			// 判断是否有差异
			if (detailE.getAllotQty().compareTo(detailE.getOrginQty()) != 0) {
				isDisparity = WhAllocationConstants.IS_DISPARITY_TRUE;
			}
		}
		// 更新仓库调拨单明细
		if (!isDisparityCreate && CollectionUtils.isNotEmpty(updateDetails)) {
			whAllocationDetailMapper.updateDetailAllotQty(updateDetails);
		}
		return isDisparity;
	}

	/**
	 * 批量查询虚仓库存
	 *
	 * @param ids
	 * @return map {key = skuid ，value = {key = vwid ，value = 库存信息}}
	 */
	private Map<Long, Map<Long, VirtualWarehouseStock>> queryVmStock(String realWarehouseCode, String factoryCode,
			List<Long> ids) {
		QueryVirtualWarehouseDTO queryVirtualWarehouseDTO = new QueryVirtualWarehouseDTO();
		queryVirtualWarehouseDTO.setRealWarehouseOutCode(realWarehouseCode);
		queryVirtualWarehouseDTO.setFactoryCode(factoryCode);
		List<VirtualWarehouse> vlist = stockRealWarehouseFacade.queryVwByRealWarehouseCode(queryVirtualWarehouseDTO);
		Map<Long, Map<Long, VirtualWarehouseStock>> vwStockMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(vlist)) {
			List<VirtualWarehouseStock> temp;
			for (VirtualWarehouse vw : vlist) {
				QueryVirtualWarehouseStockDTO queryVirtualWarehouseStockDTO = new QueryVirtualWarehouseStockDTO();
				queryVirtualWarehouseStockDTO.setVirtualWarehouseCode(vw.getVirtualWarehouseCode());
				queryVirtualWarehouseStockDTO.setSkuIds(ids);
				temp = stockQueryFacade.queryVwStockByVwCode(queryVirtualWarehouseStockDTO);
				for (VirtualWarehouseStock stockDo : temp) {
					if (vwStockMap.containsKey(stockDo.getSkuId())) {
						Map<Long, VirtualWarehouseStock> vwStock = vwStockMap.get(stockDo.getSkuId());
						vwStock.put(vw.getId(), stockDo);
					} else {
						Map<Long, VirtualWarehouseStock> vwStock = new HashMap<>();
						vwStock.put(vw.getId(), stockDo);
						vwStockMap.put(stockDo.getSkuId(), vwStock);
					}
				}
			}
		}
		return vwStockMap;
	}

	private BigDecimal vwAllocationCal(AllocationCalQtyRes skuAllocationCalQtyRes,
			Map<Long, VirtualWarehouseStock> vmStock, boolean isQualityAllocate) {
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal vBasicQty = BigDecimal.ZERO;
		BigDecimal vAllotQty = BigDecimal.ZERO;
		Map<Long, BigDecimal> vwAvailabelStock = new HashMap<>();
		Map<Long, VwAllocationQty> vwAllocationQtyMap = skuAllocationCalQtyRes.getVwAllocationQtyList().stream()
				.collect(Collectors.toMap(VwAllocationQty::getVirtualWarehouseId, Function.identity(), (v1, v2) -> v1));
		skuAllocationCalQtyRes.setExistInSufficient(false);
		for (VwAllocationQty vwAllocationQty : skuAllocationCalQtyRes.getVwAllocationQtyList()) {
			// vwAllocationQty 为预计算的虚仓分配数
			// virStock 为虚仓数据库查出来的库存
			VirtualWarehouseStock virStock = vmStock.get(vwAllocationQty.getVirtualWarehouseId());
			BigDecimal leftvStock = BigDecimal.ZERO;
			if (virStock != null) {
				// 质量调拨取真实库存
				leftvStock = virStock.getRealQty().subtract(virStock.getLockQty());
				if (isQualityAllocate) {
					leftvStock = virStock.getRealQty();
				}
			}
			if (leftvStock.compareTo(vwAllocationQty.getSkuQty()) >= 0) {
				// 虚仓库存充足,则虚仓实际库存设置为预计算库存
				vwAllocationQty.setRealSkuQty(vwAllocationQty.getSkuQty());
			} else {
				// 虚仓库存不足，则该虚仓实际调拨数量为剩余库存
				vwAllocationQty.setRealSkuQty(leftvStock);
				skuAllocationCalQtyRes.setExistInSufficient(true);
			}
			vwAvailabelStock.put(vwAllocationQty.getVirtualWarehouseId(),
					leftvStock.subtract(vwAllocationQty.getRealSkuQty()));

			vBasicQty = vBasicQty.add(vwAllocationQty.getRealSkuQty());
			vAllotQty = vBasicQty.divide(skuAllocationCalQtyRes.getActualScale(), CommonConstants.DECIMAL_POINT_NUM,
					ROUND_DOWN);
			result = vBasicQty;
		}
		if (skuAllocationCalQtyRes.getConsiderRound()
				&& new BigDecimal(vAllotQty.intValue()).compareTo(vAllotQty) < 0) {
			// 虚仓库存不足，这个时候有可能导致总的实仓调拨数不是整数【不满足箱单位或发货单位取整】
			for (RecordRealVirtualStockSyncRelationE relation : skuAllocationCalQtyRes.getList()) {
				if (relation.getSyncRate() == null || BigDecimal.ZERO.compareTo(relation.getSyncRate()) == 0) {
					continue;
				}
				BigDecimal temp = vwAvailabelStock.get(relation.getVirtualWarehouseId());
				VwAllocationQty vwAllocationQty = vwAllocationQtyMap.get(relation.getVirtualWarehouseId());
				if (temp.compareTo(BigDecimal.ZERO) > 0) {
					if (vBasicQty.add(temp).compareTo(skuAllocationCalQtyRes.getActualBasicQty()) >= 0) {
						// 加这个虚仓可用库存后 满足需求
						vwAllocationQty.setRealSkuQty(vwAllocationQty.getRealSkuQty()
								.add(skuAllocationCalQtyRes.getActualBasicQty().subtract(vBasicQty)));

						vBasicQty = vBasicQty.add(skuAllocationCalQtyRes.getActualBasicQty().subtract(vBasicQty));
						vAllotQty = vBasicQty.divide(skuAllocationCalQtyRes.getActualScale(),
								CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
						result = vBasicQty;
						break;
					} else {
						// 加这个虚仓可用库存后，依然不满足需求
						vwAllocationQty.setRealSkuQty(vwAllocationQty.getRealSkuQty().add(temp));
						vBasicQty = vBasicQty.add(temp);
						vAllotQty = vBasicQty.divide(skuAllocationCalQtyRes.getActualScale(),
								CommonConstants.DECIMAL_POINT_NUM, ROUND_DOWN);
						result = vBasicQty;
					}
				}
			}
		}
		if (skuAllocationCalQtyRes.getConsiderRound()
				&& new BigDecimal(vAllotQty.intValue()).compareTo(vAllotQty) < 0) {
			// 可用库存分配完了还不够或者还不是整数，则需要减少需求，取整
			vAllotQty = vAllotQty.setScale(0, ROUND_DOWN);
			if (vAllotQty.compareTo(BigDecimal.ZERO) == 0) {
				// 如果取整后为0，则表示无库存了，无需调拨了
				skuAllocationCalQtyRes.setActualBasicQty(BigDecimal.ZERO);
				skuAllocationCalQtyRes.setActualQty(BigDecimal.ZERO);
				return BigDecimal.ZERO;
			}
			skuAllocationCalQtyRes.setPlanBasicQty(vAllotQty.multiply(skuAllocationCalQtyRes.getActualScale()));
			allocationTool.reCalculateVmQtyForOneSku(skuAllocationCalQtyRes);
			return vwAllocationCal(skuAllocationCalQtyRes, vmStock, isQualityAllocate);
		}
		return result;
	}

	/**
	 * 构建入库单同步库存中心
	 * 
	 * @param inRecordE
	 * @return
	 */
	private InWarehouseRecordDTO buildInRecord(WarehouseRecordE inRecordE) {
		InWarehouseRecordDTO inWarehouseRecordDTO = new InWarehouseRecordDTO();
		inWarehouseRecordDTO.setFactoryCode(inRecordE.getFactoryCode());
		inWarehouseRecordDTO.setRecordCode(inRecordE.getRecordCode());  
		inWarehouseRecordDTO.setRecordType(inRecordE.getRecordType());
		inWarehouseRecordDTO.setWarehouseCode(inRecordE.getRealWarehouseCode());
		inWarehouseRecordDTO.setSapPoNo(inRecordE.getWarehouseRecordDetailList().get(0).getSapPoNo());
		
		List<RecordDetailDTO> detailList = new ArrayList<RecordDetailDTO>();
		inRecordE.getWarehouseRecordDetailList().forEach(e -> {
			RecordDetailDTO recordDetailDTO = new RecordDetailDTO();
			recordDetailDTO.setBasicSkuQty(e.getPlanQty());
			recordDetailDTO.setBasicUnit(e.getUnit());
			recordDetailDTO.setBasicUnitCode(e.getUnitCode());
			recordDetailDTO.setDeliveryLineNo(e.getDeliveryLineNo());
			recordDetailDTO.setLineNo(e.getLineNo());
			recordDetailDTO.setSkuCode(e.getSkuCode());
			detailList.add(recordDetailDTO);
		});
		inWarehouseRecordDTO.setDetailList(detailList);
		
		return inWarehouseRecordDTO;
	}
	
	/**
	 * 根据仓库调拨单生成大仓入库单
	 * 
	 * @param inRecordE
	 * @param whAllocationE
	 */
	private void whInRecordByWhAllocation(WarehouseRecordE inRecordE, WhAllocationE whAllocationE) {
		// 生成单据号并设置基础数据
		inRecordE.setRecordCode(orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.WH_ALLOCATION_IN_WAREHOUSE_RECORD.getCode()));
		inRecordE.setRecordType(WarehouseRecordTypeEnum.WH_ALLOCATION_IN_WAREHOUSE_RECORD.getType());
		inRecordE.setRealWarehouseId(whAllocationE.getInWarehouseId());
		inRecordE.setRealWarehouseCode(whAllocationE.getInRealWarehouseCode());
		inRecordE.setFactoryCode(whAllocationE.getInFactoryCode());
		inRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
		inRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
		inRecordE.setMerchantId(whAllocationE.getMerchantId());
		inRecordE.setOutCreateTime(whAllocationE.getOutCreateTime());
		inRecordE.setSyncTmsbStatus(0);
		inRecordE.setSapOrderCode(whAllocationE.getSapPoNo());
		// 插入单据
		List<WhAllocationDetailE> whAllocationDetailEList = whAllocationE.getFrontRecordDetails();
		// 处理单位换算
		whAllocationDetailEList.forEach(e -> e.setSkuQty(e.getOutQty()));
		skuQtyUnitTool.convertRealToBasic(whAllocationDetailEList);
		List<WarehouseRecordDetailE> warehouseRecordDetailEList = new ArrayList<WarehouseRecordDetailE>();
		for (WhAllocationDetailE detailE : whAllocationDetailEList) {
			if (detailE.getBasicSkuQty().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
			warehouseRecordDetailE.setRecordCode(inRecordE.getRecordCode());
			warehouseRecordDetailE.setSkuId(detailE.getSkuId());
			warehouseRecordDetailE.setSkuCode(detailE.getSkuCode());
			// 此处设置计划数量和实际数量一致，wms回调再更新实际数量，无需wms回调的业务，直接设置成一致即可
			warehouseRecordDetailE.setPlanQty(detailE.getBasicSkuQty());
			warehouseRecordDetailE.setUnit(detailE.getBasicUnit());
			warehouseRecordDetailE.setUnitCode(detailE.getBasicUnitCode());
			warehouseRecordDetailE.setActualQty(BigDecimal.ZERO);
			warehouseRecordDetailE.setLineNo(detailE.getLineNo());
			warehouseRecordDetailE.setSapPoNo(whAllocationE.getSapPoNo());
			warehouseRecordDetailE.setDeliveryData(whAllocationE.getExpeAogTime());
			warehouseRecordDetailE.setDeliveryLineNo(String.valueOf(detailE.getId()));
			warehouseRecordDetailEList.add(warehouseRecordDetailE);
		}
		AlikAssert.isTrue(warehouseRecordDetailEList.size() > 0, ResCode.ORDER_ERROR_6014, "当前调拨的单所有物料库存不足");
		// 设置skuCode和skuID
		itemInfoTool.convertSkuCode(warehouseRecordDetailEList);

		if (inRecordE.getSyncDispatchStatus() == null) {
			inRecordE.setSyncDispatchStatus(WarehouseRecordConstant.INIT_DISPATCH);
		}
		if (inRecordE.getSyncTransferStatus() == null) {
			inRecordE.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
		}
		if (inRecordE.getSyncTradeStatus() == null) {
			inRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
		}
		// 保存后置单
		warehouseRecordMapper.insertWarehouseRecord(inRecordE);
		Long id = inRecordE.getId();
		warehouseRecordDetailEList.forEach(e -> {
			e.setWarehouseRecordId(id);
		});
		inRecordE.setWarehouseRecordDetailList(warehouseRecordDetailEList);
		// 保存后置单明细
		warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordDetailEList);
		// 保存仓库调拨单和后置单关联关系表
		FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
		relation.setWarehouseRecordId(inRecordE.getId());
		relation.setFrontRecordId(whAllocationE.getId());
		relation.setFrontRecordType(whAllocationE.getRecordType());
		relation.setRecordCode(inRecordE.getRecordCode());
		relation.setFrontRecordCode(whAllocationE.getRecordCode());
		frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
		
		//构建同步库存中心的入库单对象
		InWarehouseRecordDTO inWarehouseRecordDTO = stockInRecordDTOConvert.convertE2InDTO(inRecordE);
		
		//入库单同步库存中心
		stockRecordFacade.createInRecord(inWarehouseRecordDTO);
		
		boolean isSuccess = false;
		try {
			//勇军要求特殊处理-调用库存中心派车
			DispatchNoticeDTO dispatchNoticeDTO = new DispatchNoticeDTO();
			dispatchNoticeDTO.setRecordCode(inRecordE.getRecordCode());
			dispatchNoticeDTO.setIsDispatch(false);
			dispatchNoticeDTO.setSourceSystem(appNmae);
			dispatchNoticeDTO.setThirdRecordCode("");
			List<CancelResultDTO> cancelResultList = stockRecordFacade.batchDispatchingNotify(Arrays.asList(dispatchNoticeDTO));
			if(!cancelResultList.get(0).getStatus()) {
				log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.TMS_NOTIFY, "batchDispatchingNotify", "仓库调拨入库单：" + dispatchNoticeDTO.getRecordCode(), dispatchNoticeDTO));
				 throw new RomeException(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC + "，调用库存中心派车：" + dispatchNoticeDTO.getRecordCode());
			}
			isSuccess = true;
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        throw e;
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC + ":" + e.getMessage());
	    } finally {
	        if (!isSuccess) {
	        	CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
	        	cancelRecordDTO.setRecordCode(inRecordE.getRecordCode());
	        	cancelRecordDTO.setRecordType(inRecordE.getRecordType());
	        	cancelRecordDTO.setIsForceCancel(YesOrNoEnum.YES.getType());
                warehouseRecordCommService.cancelWarehouseRecordToStock(cancelRecordDTO);
	        }
	    }
	}

	/**
	 * 根据出库单生成大仓入库单
	 * 
	 * @param frontRecord
	 * @param outRecordE
	 */
	public void whInRecordByOutRecord(WhAllocationE whAllocationE, WarehouseRecordE inRecordE,
			WarehouseRecordE outRecordE) {
		// 生成单据号并设置基础数据
		inRecordE.setRecordCode(orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.WH_ALLOCATION_IN_WAREHOUSE_RECORD.getCode()));
		inRecordE.setRecordType(WarehouseRecordTypeEnum.WH_ALLOCATION_IN_WAREHOUSE_RECORD.getType());
		inRecordE.setRealWarehouseId(whAllocationE.getInWarehouseId());
		inRecordE.setRealWarehouseCode(whAllocationE.getInRealWarehouseCode());
		inRecordE.setFactoryCode(whAllocationE.getInFactoryCode());
		inRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
		inRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
		inRecordE.setMerchantId(whAllocationE.getMerchantId());
		inRecordE.setOutCreateTime(whAllocationE.getOutCreateTime());
		inRecordE.setSapOrderCode(whAllocationE.getSapPoNo());
		// 插入单据
		List<WarehouseRecordDetailE> warehouseRecordDetails = outRecordE.getWarehouseRecordDetailList();
		List<WarehouseRecordDetailE> warehouseRecordDetailEList = new ArrayList<WarehouseRecordDetailE>();
		for (WarehouseRecordDetailE detailE : warehouseRecordDetails) {
			if (detailE.getActualQty().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			detailE.setRecordCode(inRecordE.getRecordCode());
			detailE.setPlanQty(detailE.getActualQty());
			detailE.setActualQty(BigDecimal.ZERO);
			detailE.setLineNo(detailE.getLineNo());
			detailE.setSapPoNo(whAllocationE.getSapPoNo());
			detailE.setDeliveryData(whAllocationE.getExpeAogTime());
			warehouseRecordDetailEList.add(detailE);
		}
		// 设置skuCode和skuID
		itemInfoTool.convertSkuCode(warehouseRecordDetailEList);

		if (inRecordE.getSyncDispatchStatus() == null) {
			inRecordE.setSyncDispatchStatus(WarehouseRecordConstant.INIT_DISPATCH);
		}
		if (inRecordE.getSyncTransferStatus() == null) {
			inRecordE.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
		}
		if (inRecordE.getSyncTradeStatus() == null) {
			inRecordE.setSyncTradeStatus(WarehouseRecordConstant.INIT_SYNC_TRADE);
		}
		// 保存后置单
		warehouseRecordMapper.insertWarehouseRecord(inRecordE);
		Long id = inRecordE.getId();
		warehouseRecordDetailEList.forEach(e -> {
			e.setWarehouseRecordId(id);
		});
		inRecordE.setWarehouseRecordDetailList(warehouseRecordDetailEList);
		// 保存后置单明细
		warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordDetailEList);
		// 保存仓库调拨单和后置单关联关系表
		FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
		relation.setWarehouseRecordId(inRecordE.getId());
		relation.setFrontRecordId(whAllocationE.getId());
		relation.setFrontRecordType(whAllocationE.getRecordType());
		relation.setRecordCode(inRecordE.getRecordCode());
		relation.setFrontRecordCode(whAllocationE.getRecordCode());
		frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
		
		//构建同步库存中心的入库单对象
		InWarehouseRecordDTO inWarehouseRecordDTO = stockInRecordDTOConvert.convertE2InDTO(inRecordE);
		//入库单同步库存中心
		stockRecordFacade.createInRecord(inWarehouseRecordDTO);
	}

	private String packageConfirmReturnInfo(List<WhAllocationDetailE> updateList) {
		StringBuilder sb = new StringBuilder();
		for (WhAllocationDetailE detail : updateList) {
			sb.append(detail.getSkuCode()).append("原调拨数 ").append(detail.getPlanOrigin())
					.append(detail.getPlanUnitCode())

					.append(" 自动调整为 ").append(detail.getOrginQty()).append(detail.getUnitCode())

					.append("</br>");
		}
		return sb.toString();
	}

	/**
	 * 取消调拨申请
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancleWhAllocation(Long id, Long userId, Integer isForceCancle) {
		WhAllocationE whAllocationE = whAllocationMapper.queryById(id);
		if(whAllocationE == null) {
			throw new RomeException(ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		}
		// 取消调拨申请
		Integer i = whAllocationMapper.updateAuditFail(id, userId);
		AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_6015, ResCode.ORDER_ERROR_6015_DESC);
		// 查询仓库调拨单
		// 查询所有大仓出入库单
		List<WarehouseRecordE> warehouseRecordEList = this.queryWarehouseRecordByFrontRecordCode(whAllocationE.getRecordCode());
		if(CollectionUtils.isNotEmpty(warehouseRecordEList)) {
			// 只会有一个出入库单
			for (WarehouseRecordE warehouseRecordE : warehouseRecordEList) {
				// 取消后置单
				int j = warehouseRecordMapper.updateCancelOutRecord(warehouseRecordE.getRecordCode(), userId);
				AlikAssert.isTrue(j > 0, ResCode.ORDER_ERROR_6015, ResCode.ORDER_ERROR_6015_DESC);
			}
			
			List<CancelRecordDTO> cancelRecordList = new ArrayList<CancelRecordDTO>();
			for (WarehouseRecordE warehouseRecordE : warehouseRecordEList) {
				CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
				cancelRecordDTO.setRecordCode(warehouseRecordE.getRecordCode());
				cancelRecordDTO.setRecordType(warehouseRecordE.getRecordType());
				cancelRecordDTO.setIsForceCancel(isForceCancle);
				cancelRecordList.add(cancelRecordDTO);
			}
			List<CancelResultDTO> cancelResultList = stockRecordFacade.cancelRecord(cancelRecordList);
			List<Boolean> statusList =  cancelResultList.stream().filter(cancelResultDTO -> cancelResultDTO.getStatus() == false).map(CancelResultDTO :: getStatus).distinct().collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(statusList)) {
				throw new RomeException(ResCode.ORDER_ERROR_6032, ResCode.ORDER_ERROR_6032_DESC);
			}
		}
	}
	
	/**
	 *	根据仓库调拨单据编号查询出入库单列表
	 *
	 * @param frontRecordCode
	 * @return
	 */
	private List<WarehouseRecordE> queryWarehouseRecordByFrontRecordCode(String frontRecordCode) {
		List<WarehouseRecordE> warehouseRecordEList = new ArrayList<WarehouseRecordE>();
		List<String> recordCodeList = frontWarehouseRecordRelationMapper.getRecordCodeByFrontRecordCode(frontRecordCode);
		if(CollectionUtils.isEmpty(recordCodeList)) {
			return warehouseRecordEList;
		}
		warehouseRecordEList = warehouseRecordMapper.queryWarehouseRecordByRecordCode(recordCodeList);
		return warehouseRecordEList;
	}
	
	/**
	 *	根据仓库调拨单据编号查询出入库单列表（包含明细）
	 *
	 * @param frontRecordCode
	 * @return
	 */
	private List<WarehouseRecordE> queryRecordWithDetailByFrontRecordCode(String frontRecordCode) {
		List<WarehouseRecordE> warehouseRecordEList = new ArrayList<WarehouseRecordE>();
		List<String> recordCodeList = frontWarehouseRecordRelationMapper.getRecordCodeByFrontRecordCode(frontRecordCode);
		if(CollectionUtils.isEmpty(recordCodeList)) {
			return warehouseRecordEList;
		}
		warehouseRecordEList = warehouseRecordMapper.queryWarehouseRecordByRecordCode(recordCodeList);
		if(CollectionUtils.isNotEmpty(warehouseRecordEList)) {
			for (WarehouseRecordE warehouseRecordE : warehouseRecordEList) {
				// 设置明细
				List<WarehouseRecordDetailE> details = warehouseRecordDetailMapper.queryListByRecordCode(warehouseRecordE.getRecordCode());
				warehouseRecordE.setWarehouseRecordDetailList(details);
			}
		}
		return warehouseRecordEList;
	}

	/**
	 * 根据ID查询出入库单（包含明细）
	 * 
	 * @param id
	 * @return
	 */
	public WarehouseRecordE getRecordWithDetailById(Long id) {
		// 查询单据
		WarehouseRecordE warehouseRecordE = warehouseRecordMapper.getWarehouseRecordById(id);
		if (warehouseRecordE == null) {
			return null;
		}
		// 设置明细
		List<WarehouseRecordDetailE> details = warehouseRecordDetailMapper
				.queryListByRecordId(warehouseRecordE.getId());
		if (CollectionUtils.isEmpty(details)) {
			return null;
		}
		warehouseRecordE.setWarehouseRecordDetailList(details);
		return warehouseRecordE;
	}

	/**
	 * 根据单据编号查询出入库单（包含明细）
	 * 
	 * @param recordCode
	 * @return
	 */
	public WarehouseRecordE getRecordWithDetailByRecordCode(String recordCode) {
		// 查询单据
		WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(recordCode);
		if (warehouseRecordE == null) {
			return null;
		}
		// 设置明细
		List<WarehouseRecordDetailE> details = warehouseRecordDetailMapper.queryListByRecordCode(warehouseRecordE.getRecordCode());
		if (CollectionUtils.isEmpty(details)) {
			return null;
		}
		warehouseRecordE.setWarehouseRecordDetailList(details);
		return warehouseRecordE;
	}

	@Override
	public PageInfo<WhAllocationPageDTO> queryList(WhAllocationPageDTO whAllocationPageDTO) {
		// 如果存在派车单号或sap交货单号,确定前置单单号范围
		List<WarehouseRecordE> warehouseRecordES = new ArrayList<WarehouseRecordE>();
		List<FrontWarehouseRecordRelationE> frontWarehouseRecordRelationES = null;
		if (StringUtils.isNotBlank(whAllocationPageDTO.getTmsRecordCode())) {
		warehouseRecordES = warehouseRecordMapper.queryWarehouseRecordByTmsRecordCode(whAllocationPageDTO.getTmsRecordCode());
			if (CollectionUtils.isNotEmpty(warehouseRecordES)) {
				// 查询前置单关系表取出前置单号
				frontWarehouseRecordRelationES = frontWarehouseRecordRelationMapper
						.getFrontRelationByRecordCodes(warehouseRecordES.stream()
								.map(WarehouseRecordE::getRecordCode).distinct().collect(Collectors.toList()));
			} else {
				return new PageInfo<>();
			}
		}
		Set<String> tempSet = new HashSet<>();
		List<String> recordCodes = null;
		if (CollectionUtils.isNotEmpty(frontWarehouseRecordRelationES)) {
			recordCodes = frontWarehouseRecordRelationES.stream().map(FrontWarehouseRecordRelationE::getFrontRecordCode)
					.distinct().collect(Collectors.toList());
			tempSet.addAll(recordCodes);
		}

		// 若查询条件商品编号不为空,查询明细表，获取前置单编号
		if (StringUtils.isNotBlank(whAllocationPageDTO.getSkuCode())) {
			List<String> frRecordCodes = whAllocationDetailMapper
					.queryRecordCodeBySkuCode(whAllocationPageDTO.getSkuCode());
			Set<String> tempSet2 = new HashSet<>();
			if (null != frRecordCodes && 0 != frRecordCodes.size()) {
				// 定义存储重复元素的集合
				// 提取重复元素
				if (0 != tempSet.size()) {
					for (String frRecordCode : frRecordCodes) {
						if (!tempSet.add(frRecordCode)) {
							tempSet2.add(frRecordCode);
						}
					}
					// 防止前置单号集合为空,查询时忽略前置单集合
					tempSet2.add("");
					tempSet = tempSet2;
				} else {
					tempSet.addAll(frRecordCodes);
				}
			} else {
				tempSet2.add("");
				tempSet = tempSet2;
			}
		}
		recordCodes = new ArrayList<>(tempSet);
		if(StringUtils.isNotBlank(whAllocationPageDTO.getEmpNum())) {
			// 若查询条件empNum不为空,转换成用户id
			EmployeeInfoDTO employeeNum = userFacade.getUserIdByEmployeeNum(whAllocationPageDTO.getEmpNum());
			if (null != employeeNum) {
				whAllocationPageDTO.setCreator(employeeNum.getId());
			} else {
				whAllocationPageDTO.setCreator(2L);
			}
		}
		WhAllocationE whAllocationE = whAllocationConvertor.convertPageDTO2E(whAllocationPageDTO);
		Page page = PageHelper.startPage(whAllocationPageDTO.getPageIndex(), whAllocationPageDTO.getPageSize());
		List<WhAllocationE> result = whAllocationMapper.queryWhAllocationByCondition(whAllocationE, recordCodes);
		if (CollectionUtils.isEmpty(result)) {
			return new PageInfo<>();
		}
		List<WhAllocationPageDTO> dtoList = whAllocationConvertor.convertEList2PageDTOList(result);

		// 前置单号 --- 不重复发货单号集合
		List<String> collect = dtoList.stream().map(WhAllocationPageDTO::getRecordCode).distinct()
				.collect(Collectors.toList());
		if (null != collect && collect.size() > 0) {
			List<FrontWarehouseRecordRelationE> recordCodeRelation = frontWarehouseRecordRelationMapper
					.getRecordRelationByFrontRecordCodes(collect);
			// 前置单号----不重复发货单信息
			Map<String, List<String>> recordRelationMap = new HashMap<>();
			recordCodeRelation.forEach(item -> {
				if (recordRelationMap.containsKey(item.getFrontRecordCode())) {
					if (!recordRelationMap.get(item.getFrontRecordCode()).contains(item.getRecordCode())) {
						recordRelationMap.get(item.getFrontRecordCode()).add(item.getRecordCode());
					}
				} else {
					List<String> recordCodeList = new LinkedList<>();
					recordCodeList.add(item.getRecordCode());
					recordRelationMap.put(item.getFrontRecordCode(), recordCodeList);
				}
			});
			List<String> rcList = recordCodeRelation.stream().map(FrontWarehouseRecordRelationE::getRecordCode)
					.distinct().collect(Collectors.toList());
			List<WarehouseRecordE> warehouseRecordEs = new ArrayList<WarehouseRecordE>();
			if (CollectionUtils.isNotEmpty(rcList)) {
				warehouseRecordEs = warehouseRecordMapper.queryWarehouseRecordByRecordCode(rcList);
			}
			// 发货单编号——发货单信息
			Map<String, WarehouseRecordE> warehouseRecordEMap = warehouseRecordEs.stream()
					.collect(Collectors.toMap(WarehouseRecordE::getRecordCode, item -> item));
			
			List<QueryRealWarehouseDTO> list = new ArrayList<QueryRealWarehouseDTO>();
			dtoList.forEach(e -> {
				QueryRealWarehouseDTO inQueryRealWarehouse = new QueryRealWarehouseDTO();
				inQueryRealWarehouse.setWarehouseOutCode(e.getInRealWarehouseCode());
				inQueryRealWarehouse.setFactoryCode(e.getInFactoryCode());
				QueryRealWarehouseDTO outQueryRealWarehouse = new QueryRealWarehouseDTO();
				outQueryRealWarehouse.setWarehouseOutCode(e.getOutRealWarehouseCode());
				outQueryRealWarehouse.setFactoryCode(e.getOutFactoryCode());
				list.add(inQueryRealWarehouse);
				list.add(outQueryRealWarehouse);
			});
			List<RealWarehouse> realWarehouseList = new ArrayList<RealWarehouse>();
			try {
				realWarehouseList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(list);
			} catch (Exception e1) {
				log.error(e1.getMessage(), e1);
			}
			Map<Long, RealWarehouse> realWarehouseMap = realWarehouseList.stream().collect(Collectors.toMap(RealWarehouse :: getId, Function.identity(), (v1, v2) -> v1));
			
			for (WhAllocationPageDTO pageDto : dtoList) {
				RealWarehouse inWarehouse = realWarehouseMap.get(pageDto.getInWarehouseId());
				RealWarehouse outWarehouse = realWarehouseMap.get(pageDto.getOutWarehouseId());
				// 设置仓库名称
				if (inWarehouse != null) {
					pageDto.setInRealWarehouse(inWarehouse);
				}
				if (outWarehouse != null) {
					pageDto.setOutRealWarehouse(outWarehouse);
				}
				if (recordRelationMap.containsKey(pageDto.getRecordCode())) {
					List<String> recordCodeList = recordRelationMap.get(pageDto.getRecordCode());
					StringBuilder tmsRecordCode = new StringBuilder("");
					recordCodeList.forEach(item -> {
						if (warehouseRecordEMap.containsKey(item)) {
							if (warehouseRecordEMap.get(item).getTmsRecordCode() != null) {
								tmsRecordCode.append(warehouseRecordEMap.get(item).getTmsRecordCode()).append(",");
							}
						}
					});
					if (!"".equals(tmsRecordCode.toString())) {
						pageDto.setTmsRecordCode(tmsRecordCode.substring(0, tmsRecordCode.indexOf(",")).toString());
					}
				}

			}
		}

		List<Long> modifiers = dtoList.stream().filter(v -> v.getModifier() != null)
				.map(WhAllocationPageDTO::getModifier).distinct().collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(modifiers)) {
			// 根据modifiers获取员工信息
			List<EmployeeInfoDTO> employeeInfoDTOList = userFacade.getEmployeeInfoByUserIds(modifiers);
			Map<Long, EmployeeInfoDTO> employeeInfoDTOMap = employeeInfoDTOList.stream()
					.collect(Collectors.toMap(EmployeeInfoDTO::getId, Function.identity(), (v1, v2) -> v1));
			dtoList.forEach(item -> {
				if (employeeInfoDTOMap.containsKey(item.getModifier())) {
					String modifyEmpNum = employeeInfoDTOMap.get(item.getModifier()).getEmployeeNumber();
					if (StringUtils.isNotBlank(modifyEmpNum)) {
						item.setModifyEmpNum(modifyEmpNum);
					} else {
						item.setModifyEmpNum(item.getModifier() == null ? null : String.valueOf(item.getModifier()));
					}
				} else {
					item.setModifyEmpNum(item.getModifier() == null ? null : String.valueOf(item.getModifier()));
				}
			});
		}

		PageInfo<WhAllocationPageDTO> personPageInfo = new PageInfo<>(dtoList);
		personPageInfo.setTotal(page.getTotal());
		return personPageInfo;

	}

	/**
	 * 查询单据sku实仓虚仓分配关系[调拨入库]
	 */
	@Override
	public WhAllocationPageDTO queryAllocConfigInfo(Long id) {
		return this.queryAllocConfigInfoByType(id, 2, true);
	}

	/**
	 * 查询分配比例
	 * 
	 * @param id
	 * @param type
	 *            1出库分配 2入库分配
	 * @param needQuerySystemSku
	 *            是否需要查询系统sku级别的配置
	 * @return
	 */
	private WhAllocationPageDTO queryAllocConfigInfoByType(Long id, Integer type, boolean needQuerySystemSku) {

		WhAllocationE whAllocationE = this.queryWithDetailById(id);
		if (whAllocationE == null) {
			return null;
		}
		RealWarehouse realWarehouse = null;
		if (type == 2) {
			realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(whAllocationE.getInRealWarehouseCode(),
					whAllocationE.getInFactoryCode());
		} else {
			realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(whAllocationE.getOutRealWarehouseCode(),
					whAllocationE.getOutFactoryCode());
		}
		if(realWarehouse == null) {
			throw new RomeException(ResCode.ORDER_ERROR_6043, ResCode.ORDER_ERROR_6043_DESC);
		}
		List<VirtualWarehouse> virtualWarehouseList = new ArrayList<VirtualWarehouse>();
		if (realWarehouse != null) {
			QueryVirtualWarehouseDTO queryVirtualWarehouseDTO = new QueryVirtualWarehouseDTO();
			queryVirtualWarehouseDTO.setRealWarehouseOutCode(realWarehouse.getRealWarehouseOutCode());
			queryVirtualWarehouseDTO.setFactoryCode(realWarehouse.getFactoryCode());
			virtualWarehouseList = stockRealWarehouseFacade.queryVwByRealWarehouseCode(queryVirtualWarehouseDTO);
		}
		Map<String, RecordRealVirtualStockSyncRelationE> configRelationMap = new HashMap<String, RecordRealVirtualStockSyncRelationE>();
		if (realWarehouse != null) {
			// 根据单据编码查询该单据配置的sku级别的实仓虚仓配比关系
			configRelationMap = this.queryRelationMapByRecordCode(whAllocationE.getRecordCode(), realWarehouse.getId());
		}
		List<WhAllocationDetailDTO> whAllocationDetailDTOList = whAllocationDetailConvertor
				.convertEList2DTOList(whAllocationE.getFrontRecordDetails());
		// 相同sku物料多行合并处理，设置比例的时候是sku维度并不是行号维度
		whAllocationDetailDTOList = this.mergeBySku(whAllocationDetailDTOList);
		List<SkuInfoExtDTO> skuInfoList = new ArrayList<SkuInfoExtDTO>();
		try {
			// 第三方接口异常 ，只打印错误日志，不影响主流程
			skuInfoList = itemFacade.skuBySkuIds(whAllocationDetailDTOList.stream().map(WhAllocationDetailDTO::getSkuId)
					.distinct().collect(Collectors.toList()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		Map<Long, SkuInfoExtDTO> skuInfoMap = skuInfoList.stream()
				.collect(Collectors.toMap(SkuInfoExtDTO::getId, Function.identity(), (v1, v2) -> v1));

		// 设置sku级别的实仓虚仓分配比例
		for (WhAllocationDetailDTO dto : whAllocationDetailDTOList) {
			// 查询该sku系统级别的配置关系
			Map<Long, SkuRealVirtualStockSyncRelation> skuConfigMap = new HashMap<>();
			if (needQuerySystemSku) {
				List<SkuRealVirtualStockSyncRelation> skuRelationDOs = stockRealWarehouseFacade.querySyncRateBySkuIdWId(
						dto.getSkuId(),
						type == 2 ? whAllocationE.getInRealWarehouseCode() : whAllocationE.getOutRealWarehouseCode(),
						type == 2 ? whAllocationE.getInFactoryCode() : whAllocationE.getOutFactoryCode());
				skuConfigMap = skuRelationDOs.stream().collect(Collectors.toMap(
						SkuRealVirtualStockSyncRelation::getVirtualWarehouseId, Function.identity(), (v1, v2) -> v1));
			}
			// 这里必须用拷贝的_virtualWarehouseList对象来使用，因为各个sku下面的实仓配比关系有可能是不一样
			List<VirtualWarehouse> virtualWarehouses = this.copyVirtualWarehouse(virtualWarehouseList);
			for (VirtualWarehouse warehouse : virtualWarehouses) {
				String key = dto.getSkuId() + "_" + warehouse.getId();
				if (configRelationMap.containsKey(key)) {
					// 该sku默认了单据级别的实仓虚仓配比关系，只要配了该sku的分配关系，那么该sku必须指定所属单据所对应的的实仓对应的所有虚仓
					warehouse.setConfigSyncRate(configRelationMap.get(key).getSyncRate());
					warehouse.setAllotType(configRelationMap.get(key).getAllotType());
				}
				// 系统sku级别比例覆盖默认仓到仓的比例
				if (skuConfigMap.containsKey(warehouse.getId())) {
					warehouse.setSyncRate(skuConfigMap.get(warehouse.getId()).getSyncRate());
				}
				//
			}
			if (skuInfoMap.containsKey(dto.getSkuId())) {
				dto.setSkuName(skuInfoMap.get(dto.getSkuId()).getName());
			} else {
				dto.setSkuName("");
			}
			// 默认的实仓虚仓分配关系，每个sku都使用该默认的比例
			dto.setVmSyncRate(virtualWarehouses);
		}
		WhAllocationPageDTO result = whAllocationConvertor.convertE2PageDTO(whAllocationE);
		if (type == 1) {
			result.setOutWarehouseCode(realWarehouse.getRealWarehouseCode());
			result.setOutWarehouseName(realWarehouse.getRealWarehouseName());
			result.setOutWarehouseId(realWarehouse.getId());
		} else {
			result.setInWarehouseCode(realWarehouse.getRealWarehouseCode());
			result.setInWarehouseName(realWarehouse.getRealWarehouseName());
			result.setInWarehouseId(realWarehouse.getId());
		}
		result.setFrontRecordDetails(whAllocationDetailDTOList);
		return result;

	}

	private Map<String, RecordRealVirtualStockSyncRelationE> queryRelationMapByRecordCode(String recordCode,
			Long rwId) {
		List<RecordRealVirtualStockSyncRelationE> relationList = recordRealVirtualStockSyncRelationMapper
				.queryByRecordCode(recordCode);
		Map<String, RecordRealVirtualStockSyncRelationE> result = new HashMap<>();
		for (RecordRealVirtualStockSyncRelationE relationE : relationList) {
			if (rwId == null || rwId.equals(relationE.getRealWarehouseId())) {
				result.put(relationE.getSkuId() + "_" + relationE.getVirtualWarehouseId(), relationE);
			}
		}
		return result;
	}

	/**
	 * 属性拷贝
	 *
	 * @param origin
	 * @return
	 */
	private List<VirtualWarehouse> copyVirtualWarehouse(List<VirtualWarehouse> origin) {
		List<VirtualWarehouse> desc = new ArrayList<>();
		try {
			for (VirtualWarehouse virtualWarehouse : origin) {
				VirtualWarehouse item = new VirtualWarehouse();
				BeanUtils.copyProperties(virtualWarehouse, item);
				desc.add(item);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return desc;
	}

	/**
	 * 相同sku物料多行合并处理，设置比例或绝对数的时候是sku维度并不是行号维度
	 * 
	 * @param list
	 */
	private List<WhAllocationDetailDTO> mergeBySku(List<WhAllocationDetailDTO> list) {
		List<WhAllocationDetailDTO> res = new ArrayList<>();
		Map<Long, List<WhAllocationDetailDTO>> tempMap = list.stream()
				.collect(Collectors.groupingBy(WhAllocationDetailDTO::getSkuId));
		for (Map.Entry<Long, List<WhAllocationDetailDTO>> entry : tempMap.entrySet()) {
			WhAllocationDetailDTO dto = entry.getValue().get(0);
			for (int i = 1; i < entry.getValue().size(); i++) {
				dto.setAllotQty(dto.getAllotQty().add(entry.getValue().get(i).getAllotQty()));
			}
			res.add(dto);
		}
		return res;
	}

	/**
	 * 查询单据sku实仓虚仓分配关系[调拨出库]
	 */
	@Override
	public WhAllocationPageDTO queryOutAllocConfigInfo(Long id) {
		return this.queryAllocConfigInfoByType(id, 1, true);
	}

	/**
	 * 批量查询单据sku实仓虚仓分配关系[调拨出库]
	 */
	@Override
	public List<WhAllocationPageDTO> queryAllocConfigInfoByRecords(List<Long> ids) {
		List<WhAllocationPageDTO> result = new ArrayList<>();
		for (Long id : ids) {
			result.add(this.queryAllocConfigInfoByType(id, 2, false));
		}
		return result;
	}

	/**
	 * 批量查询单据sku实仓虚仓分配关系[调拨出库]
	 */
	@Override
	public List<WhAllocationPageDTO> queryOutAllocConfigInfoByRecords(List<Long> ids) {
		List<WhAllocationPageDTO> result = new ArrayList<>();
		for (Long id : ids) {
			result.add(this.queryAllocConfigInfoByType(id, 1, false));
		}
		return result;
	}

	/**
	 * 根据仓库ID查询实仓库存列表
	 */
	@Override
	public PageInfo<RealWarehouseStockDTO> queryStockByWhIdForWhAllot(RealWarehouseStockDTO realWarehouseStockDTO) {

		String skuCodesStr = null;
		if (realWarehouseStockDTO.getSkuCodes() != null) {
			skuCodesStr = realWarehouseStockDTO.getSkuCodes().replaceAll("\\n", " ");
		}
		if (skuCodesStr != null && !"".equals(skuCodesStr.trim())) {
			List<String> skuCodes = Arrays.asList(skuCodesStr.split(" "));
			realWarehouseStockDTO
					.setSkuCodeList(skuCodes.stream().map(String::trim).distinct().collect(Collectors.toList()));
		}
		PageInfo<RealWarehouseStockDTO> pageInfo = stockWhAllocationFacade.queryStockByWhIdForWhAllot(realWarehouseStockDTO);
		// 封装商品信息
		this.dealStockInfo(pageInfo.getList());
		pageInfo.setTotal(pageInfo.getTotal());
		return pageInfo;
	}

	/**
	 * 初始化新增页面
	 */
	@Override
	public WhAllocationPageDTO initAddPage() {
		WhAllocationPageDTO dto = new WhAllocationPageDTO();
		// 生成单据编号
		String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getCode());
		// 获取原因列表
		List<BusinessReasonE> reasonList = businessReasonMapper
				.queryBusinessReasonByRecordType(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType());
		dto.setReasonList(businessReasonConvertor.convertEList2DTOList(reasonList));
		dto.setRecordCode(code);
		return dto;
	}

	/**
	 * 初始化编辑页面
	 */
	@Override
	public WhAllocationPageDTO initEditPage(Long id) {
		// 先查询出前置单据
		WhAllocationE whAllocationE = this.queryWithDetailById(id);
		AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		WhAllocationPageDTO pageDto = whAllocationConvertor.convertE2PageDTO(whAllocationE);
		RealWarehouse inWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
				whAllocationE.getInRealWarehouseCode(), whAllocationE.getInFactoryCode());
		RealWarehouse outWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
				whAllocationE.getOutRealWarehouseCode(), whAllocationE.getOutFactoryCode());
		// 设置仓库名称
		if (inWarehouse != null) {
			pageDto.setInRealWarehouse(inWarehouse);
		}
		if (outWarehouse != null) {
			pageDto.setOutRealWarehouse(outWarehouse);
		}
		// 查询商品信息
		List<Long> skuIdList = pageDto.getFrontRecordDetails().stream().map(WhAllocationDetailDTO::getSkuId).distinct()
				.collect(Collectors.toList());
		List<String> skuCodeList = pageDto.getFrontRecordDetails().stream().map(WhAllocationDetailDTO::getSkuCode)
				.distinct().collect(Collectors.toList());
		List<SkuInfoExtDTO> skuInfoList = itemFacade.skuBySkuIds(skuIdList);
		Map<Long, SkuInfoExtDTO> skuMap = skuInfoList.stream()
				.collect(Collectors.toMap(SkuInfoExtDTO::getId, Function.identity(), (v1, v2) -> v1));

		QueryRealWarehouseStockDTO queryRealWarehouseStockDTO = new QueryRealWarehouseStockDTO();
		queryRealWarehouseStockDTO.setRealWarehouseOutCode(pageDto.getOutRealWarehouseCode());
		queryRealWarehouseStockDTO.setFactoryCode(pageDto.getOutFactoryCode());
		queryRealWarehouseStockDTO.setIsQualityAllocate(whAllocationE.getIsQualityAllotcate());
		queryRealWarehouseStockDTO.setSkuIds(skuIdList);
		// 查询库存信息
		List<RealWarehouseStockDTO> stockList = stockWhAllocationFacade.listStockBySkuWhIdForWhAllot(queryRealWarehouseStockDTO);

		Map<Long, RealWarehouseStockDTO> stockMap = stockList.stream()
				.collect(Collectors.toMap(RealWarehouseStockDTO::getSkuId, Function.identity(), (v1, v2) -> v1));
		// 查询单位换算信息
		List<SkuUnitExtDTO> skuUnitList = itemFacade.querySkuUnits(skuCodeList);
		Map<Long, List<SkuUnitExtDTO>> skuUnitMap = skuUnitList.stream()
				.collect(Collectors.groupingBy(SkuUnitExtDTO::getSkuId));
		// 设置信息
		for (WhAllocationDetailDTO rwDto : pageDto.getFrontRecordDetails()) {
			// 设置商品信息
			SkuInfoExtDTO skuInfo = skuMap.get(rwDto.getSkuId());
			if (skuInfo != null) {
				rwDto.setSkuCode(skuInfo.getSkuCode());
				rwDto.setCategoryName(skuInfo.getCategoryName());
				rwDto.setSkuName(skuInfo.getName());
				rwDto.setBaseUnit(skuInfo.getSpuUnitName());
			}
			// 设置单位换算信息
			List<SkuUnitExtDTO> unitList = skuUnitMap.get(rwDto.getSkuId());
			if (CollectionUtils.isNotEmpty(unitList)) {
				List<WhSkuUnitDTO> whUnitList = whAllocationConvertor.convertUnitList2WhUnitList(unitList);
				List<WhSkuUnitDTO> singleList = this.dealUnitList(whUnitList);
				rwDto.setSkuUnitList(singleList);
			}
			// 设置库存信息
			RealWarehouseStockDTO stock = stockMap.get(rwDto.getSkuId());
			if (stock != null) {
				rwDto.setRealQty(stock.getRealQty());
				rwDto.setLockQty(stock.getLockQty());
			}
		}
		// 获取原因列表
		List<BusinessReasonE> reasonList = businessReasonMapper
				.queryBusinessReasonByRecordType(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType());
		pageDto.setReasonList(businessReasonConvertor.convertEList2DTOList(reasonList));
		return pageDto;

	}

	/**
	 * 获取待同步的订单
	 */
	@Override
	public List<WhAllocationDTO> getWaitSyncOrder(int page, int maxResult) {
		Date endTime = new Date();
		Date startTime = DateUtils.addDays(endTime, -30);
		return whAllocationConvertor.convertEList2DTOList(whAllocationMapper.queryWaitSyncOrder(page, maxResult, startTime, endTime));
	}

	/**
	 * 下发RDC调拨单至SAP
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void processWhAllocationOrderToSap(WhAllocationDTO whAllocationDTO) {
		if (WhAllocationConstants.CONFIM_STATUS.equals(whAllocationDTO.getRecordStatus())
				&& WhAllocationConstants.WAIT_SYNC_STATUS.equals(whAllocationDTO.getSyncStatus())) {
			// 获取入向仓库
			RealWarehouse inWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
					whAllocationDTO.getInRealWarehouseCode(), whAllocationDTO.getInFactoryCode());
			AlikAssert.isNotNull(inWarehouse, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
			StoreDTO inStore = baseFacade.searchByCode(inWarehouse.getFactoryCode());
			AlikAssert.isNotNull(inStore, ResCode.ORDER_ERROR_6028, ResCode.ORDER_ERROR_6028_DESC);
			// 获取出向仓库
			RealWarehouse outWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(
					whAllocationDTO.getOutRealWarehouseCode(), whAllocationDTO.getOutFactoryCode());
			AlikAssert.isNotNull(outWarehouse, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
			StoreDTO outStore = baseFacade.searchByCode(outWarehouse.getFactoryCode());
			AlikAssert.isNotNull(outStore, ResCode.ORDER_ERROR_6029, ResCode.ORDER_ERROR_6029_DESC);
			// 获取前置单明细
			WhAllocationE whAllocationE = whAllocationMapper.queryById(whAllocationDTO.getId());
			List<WhAllocationDetailE> whAllocationDetailEList = whAllocationDetailMapper
					.queryDetailByFrontIds(Arrays.asList(whAllocationDTO.getId()));
			whAllocationE.setFrontRecordDetails(whAllocationDetailEList);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			List<SyncSapPoDTO> list = new ArrayList<>();
			whAllocationDetailEList.forEach(wrRecord -> wrRecord.setSkuQty(wrRecord.getAllotQty()));
			skuQtyUnitTool.convertRealToBasic(whAllocationDetailEList);
			// 按照skuCode[行号]进行升序排序，传给sap必须保证行号的顺序
			Collections.sort(whAllocationDetailEList, new Comparator<WhAllocationDetailE>() {
				@Override
				public int compare(WhAllocationDetailE o1, WhAllocationDetailE o2) {
					return o1.getLineNo().compareTo(o2.getLineNo());
				}
			});
			for (int j = 0; j < whAllocationDetailEList.size(); j++) {
				WhAllocationDetailE detail = whAllocationDetailEList.get(j);
				SyncSapPoDTO dto = new SyncSapPoDTO();
				dto.setBillNo(detail.getRecordCode());
				// 不足6位补0
				dto.setBillNum(detail.getLineNo());
				// 0的不推送
				if (detail.getBasicSkuQty().compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				// 退货调拨
				if (WhAllocationConstants.RETURN_ALLOCATE.equals(whAllocationDTO.getIsReturnAllotcate())) {
					dto.setCompanyCode(outStore.getCompanyCode());
					dto.setSupplierCode(inWarehouse.getFactoryCode());
					dto.setType(this.calculatePoType(outStore, inStore, true));
					if (StringUtils.isNotBlank(detail.getReasonCode())) {
						dto.setReason(detail.getReasonCode());
					}
					dto.setRecvPlant(outWarehouse.getFactoryCode());
					dto.setRecvStorage(outWarehouse.getRealWarehouseOutCode());
					dto.setSendPlant(inWarehouse.getFactoryCode());
					dto.setSendStorage(inWarehouse.getRealWarehouseOutCode());
				} else {
					dto.setCompanyCode(inStore.getCompanyCode());
					dto.setSupplierCode(outWarehouse.getFactoryCode());
					dto.setType(this.calculatePoType(outStore, inStore, false));
					dto.setSendPlant(outWarehouse.getFactoryCode());
					dto.setSendStorage(outWarehouse.getRealWarehouseOutCode());
					dto.setRecvPlant(inWarehouse.getFactoryCode());
					dto.setRecvStorage(inWarehouse.getRealWarehouseOutCode());
				}
				dto.setReqDate(sdf.format(whAllocationE.getCreateTime()));
				dto.setMaterialCode(detail.getSkuCode());
				dto.setQuantity(detail.getSkuQty());
				dto.setUnit(detail.getUnitCode());
				list.add(dto);
			}
			if (list.size() > 0) {
				// 推送sap
				Response res = sapFacade.syncPoRecordToSap(whAllocationDTO.getRecordCode(), list);
				// 更新sap单号及下发状态
				String sapCode = res.getData().toString();
				int i = whAllocationMapper.updateSyncSucc(whAllocationDTO.getId(), sapCode);
				AlikAssert.isTrue(i == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
				List<WarehouseRecordE> warehouseRecordEList = this.queryRecordWithDetailByFrontRecordCode(whAllocationDTO.getRecordCode());
				for (WarehouseRecordE warehouseRecordE : warehouseRecordEList) {
					List<PoNoDTO> poNoList = new ArrayList<PoNoDTO>();
					// 修改记录sap单号到明细
					warehouseRecordE.getWarehouseRecordDetailList().forEach(e -> {
						e.setSapPoNo(sapCode);
						PoNoDTO poNoDTO = new PoNoDTO();
						poNoDTO.setSapPoNo(sapCode);
						poNoDTO.setRecordCode(warehouseRecordE.getRecordCode());
						poNoDTO.setDeliveryLineNo(e.getDeliveryLineNo());
						poNoDTO.setLineNo(e.getLineNo());
						poNoDTO.setSkuCode(e.getSkuCode());
						poNoList.add(poNoDTO);
					});
					warehouseRecordMapper.updateSapPoNo(warehouseRecordE.getRecordCode(), sapCode);
					warehouseRecordDetailMapper.updateDetailSapNo(warehouseRecordE.getWarehouseRecordDetailList());
					
					// 同步派车系统状态 0无需同步 1待同步 2已完成
					Integer syncTmsbStatus = 0;
					//同步库存中心状态 0无需同步 1待同步 2已同步
					Integer syncStockStatus = 1;
					if (CollectionUtils.isNotEmpty(poNoList)) {
						try {
							// 下发sapPoNo给库存中心
							stockWhAllocationFacade.updateLineNoAndSapPoNo(poNoList);
							//businessType=1出库单2入库单
							if(Integer.valueOf(1).equals(warehouseRecordE.getBusinessType())) {
								syncTmsbStatus = 1;
							}
							syncStockStatus = 2;
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
					// 更新出库单同步派车系统、同步库存状态
					warehouseRecordMapper.updateSyncTmsbAndStockStatus(warehouseRecordE.getRecordCode(), syncTmsbStatus,
							syncStockStatus);
				}
			} else {
				log.info("=====无需推送sap，明细为空,单号：" + whAllocationDTO.getRecordCode());
			}
		}
	}

	/**
	 * 计算sap的PO类型
	 * 
	 * @param outStore
	 * @param inStore
	 * @param isReturnWhallocate
	 * @return
	 */
	private String calculatePoType(StoreDTO outStore, StoreDTO inStore, boolean isReturnWhallocate) {
		String outCompanyCode = outStore.getCompanyCode();
		if (outCompanyCode.equals(inStore.getCompanyCode())) {
			if (isReturnWhallocate) {
				return SyncSapPoDTO.SAME_COMPANY_RETURN;
			} else {
				return SyncSapPoDTO.SAME_COMPANY_PURCHASE;
			}
		} else {
			if (isReturnWhallocate) {
				return SyncSapPoDTO.DIFF_COMPANY_RETURN;
			} else {
				return SyncSapPoDTO.DIFF_COMPANY_PURCHASE;
			}
		}
	}

	/**
	 * 导入excel文件中的数据
	 */
	@Override
	public void importFileData(List<WhAllocationTemplateDTO> dataList, Long userId) {
		List<String> errorLogs = new ArrayList<>();
		// 生成错误列表
		WhAllocationDTO dto = new WhAllocationDTO();
		// 只处理一条数据
		WhAllocationTemplateDTO templateDTO = dataList.get(0);
		for (int i = 0; i < dataList.size(); i++) {
			WhAllocationTemplateDTO temp = dataList.get(i);
			// 校验业务类型(必须是两种类型之一)
			List<String> typeList = WhAllocationConstants.ALLOCATION_TYPE_LIST_STR;
			AlikAssert.isTrue(typeList.contains(temp.getBusinessTypeStr()), ResCode.ORDER_ERROR_6002,
					"第" + (i + 2) + "行" + ResCode.ORDER_ERROR_6002_DESC);
			if (!temp.getBusinessTypeStr().equals(templateDTO.getBusinessTypeStr())) {
				throw new RomeException(ResCode.ORDER_ERROR_6019, ResCode.ORDER_ERROR_6019_DESC);
			}
		}
		// 校验仓库是否存在
		RealWarehouse inWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(templateDTO.getInRealWareCode(),
				templateDTO.getInFactoryCode());
		AlikAssert.isNotNull(inWarehouse, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
		RealWarehouse outWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(templateDTO.getOutRealWareCode(),
				templateDTO.getOutFactoryCode());
		AlikAssert.isNotNull(outWarehouse, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
		// 内部调拨只能选择相同工厂
		if (WhAllocationConstants.INNER_ALLOCATION_STR.equals(templateDTO.getBusinessTypeStr())) {
			if (!inWarehouse.getFactoryCode().equals(outWarehouse.getFactoryCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_6006, ResCode.ORDER_ERROR_6006_DESC);
			}
			dto.setBusinessType(WhAllocationConstants.INNER_ALLOCATION);
		}
		// RDC调拨的工厂不能相同
		if (WhAllocationConstants.RDC_ALLOCATION_STR.equals(templateDTO.getBusinessTypeStr())) {
			if (inWarehouse.getFactoryCode().equals(outWarehouse.getFactoryCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_6007, ResCode.ORDER_ERROR_6007_DESC);
			}
			dto.setBusinessType(WhAllocationConstants.RDC_ALLOCATION);
		}
		// 出入仓不能相等
		AlikAssert.isTrue(!inWarehouse.getId().equals(outWarehouse.getId()), ResCode.ORDER_ERROR_6003,
				ResCode.ORDER_ERROR_6003_DESC);
		dto.setInWarehouseId(inWarehouse.getId());
		dto.setInRealWarehouseCode(inWarehouse.getRealWarehouseOutCode());
		dto.setInFactoryCode(inWarehouse.getFactoryCode());
		dto.setOutWarehouseId(outWarehouse.getId());
		dto.setOutRealWarehouseCode(outWarehouse.getRealWarehouseOutCode());
		dto.setOutFactoryCode(outWarehouse.getFactoryCode());
		// 校验退货类型
		if (templateDTO.getIsReturnAllotcate() != null
				&& WhAllocationConstants.RETURN_ALLOCATE_TYPE_LIST_STR.contains(templateDTO.getIsReturnAllotcate())) {
			if (WhAllocationConstants.RETURN_ALLOCATE_STR.equals(templateDTO.getIsReturnAllotcate())) {
				dto.setIsReturnAllotcate(WhAllocationConstants.RETURN_ALLOCATE);
			} else if (WhAllocationConstants.NOT_RETURN_ALLOCATE_STR.equals(templateDTO.getIsReturnAllotcate())) {
				dto.setIsReturnAllotcate(WhAllocationConstants.NOT_RETURN_ALLOCATE);
			}
		} else {
			throw new RomeException(ResCode.ORDER_ERROR_6020, ResCode.ORDER_ERROR_6020_DESC);
		}
		// 校验质量调拨
		if (templateDTO.getIsQualityAllotcate() != null
				&& WhAllocationConstants.RETURN_ALLOCATE_TYPE_LIST_STR.contains(templateDTO.getIsQualityAllotcate())) {
			if (WhAllocationConstants.QUALITY_ALLOCATE_STR.equals(templateDTO.getIsQualityAllotcate())) {
				dto.setIsQualityAllotcate(WhAllocationConstants.QUALITY_ALLOCATE);
			} else if (WhAllocationConstants.NOT_QUALITY_ALLOCATE_STR.equals(templateDTO.getIsQualityAllotcate())) {
				dto.setIsQualityAllotcate(WhAllocationConstants.NOT_QUALITY_ALLOCATE);
			}
		} else {
			throw new RomeException(ResCode.ORDER_ERROR_6021, ResCode.ORDER_ERROR_6021_DESC);
		}
		// 填充出库联系人和电话
		if (StringUtils.isNotBlank(templateDTO.getInWarehouseName())) {
			dto.setInWarehouseName(templateDTO.getInWarehouseName());
		}
		if (StringUtils.isNotBlank(templateDTO.getInWarehouseMobile())) {
			dto.setInWarehouseMobile(templateDTO.getInWarehouseMobile());
		}
		// 填充入库联系人和电话
		if (StringUtils.isNotBlank(templateDTO.getOutWarehouseName())) {
			dto.setOutWarehouseName(templateDTO.getOutWarehouseName());
		}
		if (StringUtils.isNotBlank(templateDTO.getOutWarehouseMobile())) {
			dto.setOutWarehouseMobile(templateDTO.getOutWarehouseMobile());
		}
		// 填充调拨日期
		if (templateDTO.getAllotTime() != null) {
			dto.setAllotTime(templateDTO.getAllotTime());
		} else {
			dto.setAllotTime(new Date());
		}
		// 填充预计到货日期
		if (templateDTO.getExpeAogTimeStr() != null) {
			dto.setExpeAogTime(templateDTO.getExpeAogTimeStr());
		} else {
			dto.setExpeAogTime(new Date());
		}
		// 校验商品信息
		List<WhAllocationTemplateDetailDTO> tempDetailList = templateDTO.getFrontRecordDetails();
		if (CollectionUtils.isEmpty(tempDetailList)) {
			throw new RomeException(ResCode.ORDER_ERROR_6022, "调拨明细为空");
		}
		// 查询商品信息
		List<String> skuCodeList = tempDetailList.stream().map(WhAllocationTemplateDetailDTO::getSkuCode).distinct()
				.collect(Collectors.toList());
		skuCodeList.removeIf(s -> StringUtils.isBlank(s));
		List<SkuInfoExtDTO> skuList = itemFacade.skuBySkuCodes(skuCodeList);
		// 设置商品Id
		for (SkuInfoExtDTO skuInfo : skuList) {
			tempDetailList.stream().filter(sku -> skuInfo.getSkuCode().equals(sku.getSkuCode()))
					.forEach(sku -> sku.setSkuId(skuInfo.getId()));
		}
		Map<String, SkuInfoExtDTO> skuMap = skuList.stream()
				.collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, Function.identity(), (v1, v2) -> v1));
		// 查询单位信息
		List<ParamExtDTO> paramExtList = whAllocationConvertor.converTempaleList2UnitParamList(tempDetailList);
		paramExtList.removeIf(s -> s.getSkuId() == null || StringUtils.isBlank(s.getUnitCode()));
		List<SkuUnitExtDTO> skuUnitList = itemFacade.unitsBySkuIdAndUnitCode(paramExtList, null);
		Map<Long, SkuUnitExtDTO> skuUnitMap = skuUnitList.stream()
				.collect(Collectors.toMap(SkuUnitExtDTO::getSkuId, Function.identity(), (v1, v2) -> v1));
		List<WhAllocationDetailDTO> detailList = new ArrayList<>();
		// 获取退货原因
		List<BusinessReasonE> reasonList = businessReasonMapper
				.queryBusinessReasonByRecordType(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getType());
		Map<String, BusinessReasonE> reasonMap = reasonList.stream()
				.collect(Collectors.toMap(BusinessReasonE::getReasonName, Function.identity(), (v1, v2) -> v1));
		for (int i = 0; i < tempDetailList.size(); i++) {
			WhAllocationDetailDTO detailDTO = new WhAllocationDetailDTO();
			WhAllocationTemplateDetailDTO templateDetailDTO = tempDetailList.get(i);
			// 设置skuCode
			if (StringUtils.isNotBlank(templateDetailDTO.getSkuCode())
					&& skuMap.containsKey(templateDetailDTO.getSkuCode())) {
				detailDTO.setSkuCode(templateDetailDTO.getSkuCode());
				detailDTO.setSkuId(skuMap.get(templateDetailDTO.getSkuCode()).getId());
			} else {
				errorLogs.add("第" + (i + 2) + "行商品" + templateDetailDTO.getSkuCode() + "不存在");
			}
			// 设置单位
			SkuUnitExtDTO unitExtDTO = null;
			if (StringUtils.isNotBlank(templateDetailDTO.getUnitCode())
					&& skuUnitMap.containsKey(templateDetailDTO.getSkuId())) {
				SkuUnitExtDTO extDTO = skuUnitMap.get(templateDetailDTO.getSkuId());
				detailDTO.setUnitCode(extDTO.getUnitCode());
				unitExtDTO = skuUnitMap.get(templateDetailDTO.getSkuId());
				detailDTO.setUnit(unitExtDTO.getUnitName());
				// 设置数量
				if (StringUtils.isBlank(templateDetailDTO.getAllotQty())) {
					errorLogs.add("第" + (i + 2) + "行商品编号为" + templateDetailDTO.getSkuCode() + "数量" + "不存在");
				} else {
					if (this.posttiveFloat(templateDetailDTO.getAllotQty())) {
						// 调拨数量必须大于0
						AlikAssert.isTrue(
								new BigDecimal(templateDetailDTO.getAllotQty()).compareTo(BigDecimal.ZERO) > 0,
								ResCode.ORDER_ERROR_6022,
								"第" + (i + 2) + "行商品编号为" + templateDetailDTO.getSkuCode() + "数量必须大于0");
						detailDTO.setOrginQty(new BigDecimal(templateDetailDTO.getAllotQty()));
						detailDTO.setAllotQty(new BigDecimal(templateDetailDTO.getAllotQty()));

					} else {
						errorLogs.add("第" + (i + 2) + "行商品编号为" + templateDetailDTO.getSkuCode() + "数量"
								+ ResCode.ORDER_ERROR_6022_DESC);
					}
				}
			} else {
				errorLogs.add("第" + (i + 2) + "行商品编号为" + templateDetailDTO.getSkuCode() + "的单位"
						+ templateDetailDTO.getUnitCode() + "不存在");
			}
			// 设置退货原因
			if (dto.getIsReturnAllotcate().equals(WhAllocationConstants.RETURN_ALLOCATE)) {
				if (StringUtils.isNotBlank(templateDetailDTO.getReturnReason())) {
					if (reasonMap.containsKey(templateDetailDTO.getReturnReason())) {
						detailDTO.setReasonCode(reasonMap.get(templateDetailDTO.getReturnReason()).getReasonCode());
					} else {
						errorLogs.add("第" + (i + 2) + "行商品编号为" + templateDetailDTO.getSkuCode() + "退货原因不存在");
					}
				} else {
					errorLogs.add("第" + (i + 2) + "行商品编号为" + templateDetailDTO.getSkuCode() + "退货原因不能为空");
				}
			}
			detailList.add(detailDTO);
		}
		dto.setFrontRecordDetails(detailList);
		if (errorLogs.size() > 0) {
			throw new RomeException(ResCode.ORDER_ERROR_1001, errorLogs);
		}
		// 保存单据
		dto.setCreator(userId);
		dto.setRecordCode(orderUtilService.queryOrderCode(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getCode()));
		WhAllocationE whAllocationE = whAllocationConvertor.convertDTO2E(dto);
		// 设置调拨类型为excel导入
		whAllocationE.setAddType(WhAllocationConstants.ADD_TYPE_IMPORT);
		// 根据库存和箱单位重新设置调拨数量
		whAllocationE.setInWarehouse(inWarehouse);

		/***** 发货单单位取整计算处理 开始 ***/
		// 查询入库仓库
		List<AllocationCalQtyRes> preResult = this.preCalculate(whAllocationE, false);
		Map<String, AllocationCalQtyRes> resultMap = preResult.stream()
				.collect(Collectors.toMap(AllocationCalQtyRes::getLineNo, Function.identity(), (v1, v2) -> v1));
		/***** 发货单单位取整计算处理 结束 ***/

		Integer isDisparity = this.resetDetailListByRwStock(whAllocationE, resultMap, false, false);
		whAllocationE.setIsDisparity(isDisparity);
		whAllocationE.setOrginId(-1L);
		this.addAllocation(whAllocationE, false);

	}

	/**
	 * 验证浮点数
	 *
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
	 */
	private boolean posttiveFloat(String value) {
		BigDecimal allotNum = null;
		try {
			allotNum = new BigDecimal(value).setScale(CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
		} catch (Exception e) {
			log.error("转化excel数据错误, 元数据: " + value, e);
			return false;
		}
		Matcher matcher = pattern.matcher(String.valueOf(allotNum));
		return matcher.matches();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String createDisparityAllot(Long id, Long userId) {
		boolean isLock = redisUtil.lock(WH_ALLOT_DISPARITY_KEY, CLIENT_ID, WH_ALLOT_DISPARITY_TIME);
		if (isLock) {
			JSONObject tipLogs = new JSONObject();
			// 预下市商品列表
			List<String> undercarriageCodes = new ArrayList<>();
			// 不满足库存商品列表
			List<String> inSufficeStockCodes = new ArrayList<>();
			// 待处理商品列表
			List<WhAllocationDetailE> allotSkuInfo = new ArrayList<>();
			try {
				// 查询前置单
				WhAllocationE recordE = this.queryWithDetailById(id);
				// 1、必要的校验
				this.validateForCreateDisparity(recordE);

				// 2、查询存在差异的明细,即需要创建差异调拨的明细【并剔除预下市商品】
				List<WhAllocationDetailE> needHandleSkus = this.calculateForCreateDisparity(recordE, undercarriageCodes,
						true);
				AlikAssert.isTrue(needHandleSkus.size() > 0, ResCode.ORDER_ERROR_1003,
						ResCode.ORDER_ERROR_1003 + "：所有物料均为预下市或下市物料");
				// 3、根据库存和箱单位重新设置调拨数量
				WhAllocationE disparityRecordE = this.copyBasicInfoFromOrigin(recordE);
				disparityRecordE.setFrontRecordDetails(needHandleSkus);

				/***** 发货单单位取整以及虚仓预计算处理 开始 ***/
				List<AllocationCalQtyRes> preResult = this.preCalculate(disparityRecordE, true);
				Map<String, AllocationCalQtyRes> resultMap = preResult.stream()
						.collect(Collectors.toMap(AllocationCalQtyRes::getLineNo, Function.identity(), (v1, v2) -> v1));

				this.resetOriginQtyByPreResult(disparityRecordE, resultMap);
				/***** 发货单单位取整以及虚仓预计算处理 结束 ***/

				this.resetDetailListByRwStock(disparityRecordE, resultMap, true, true);
				for (WhAllocationDetailE detailE : disparityRecordE.getFrontRecordDetails()) {
					if (detailE.getAllotQty().compareTo(BigDecimal.ZERO) == 0) {
						inSufficeStockCodes.add(detailE.getSkuCode());
					} else {
						allotSkuInfo.add(detailE);
					}
				}
				// 4、判断是否可以创建差异调拨单
				AlikAssert.isTrue(allotSkuInfo.size() > 0, ResCode.ORDER_ERROR_1001,
						ResCode.ORDER_ERROR_1001_DESC + ":所有物料均无库存满足（或转换为发货单位后为0）");
				disparityRecordE.setFrontRecordDetails(allotSkuInfo);
				// 5、创建差异调拨前置单及明细
				this.createDisparityWhAllocation(disparityRecordE, userId);
				// resultMap 重新排号,因为创建差异调拨前置单时 调拨单的行号重新编排了
				List<AllocationCalQtyRes> tempList = new ArrayList<>();
				for (WhAllocationDetailE detailE : disparityRecordE.getFrontRecordDetails()) {
					if (resultMap.containsKey(detailE.getOriginLineNo())) {
						resultMap.get(detailE.getOriginLineNo()).setLineNo(detailE.getLineNo());
						tempList.add(resultMap.get(detailE.getOriginLineNo()));
					}
				}
				resultMap = tempList.stream()
						.collect(Collectors.toMap(AllocationCalQtyRes::getLineNo, Function.identity(), (v1, v2) -> v1));

				// 6、创建调拨出库单
				this.queryContainerAttr(disparityRecordE);

				Map<String, List<WhAllocationDetailE>> containerMap = disparityRecordE.getFrontRecordDetails().stream()
						.collect(Collectors.groupingBy(WhAllocationDetailE::getContainer));

				for (List<WhAllocationDetailE> detailEList : containerMap.values()) {
					disparityRecordE.setFrontRecordDetails(detailEList);
					WarehouseRecordE whRecordE = new WarehouseRecordE();
					if (WhAllocationConstants.WH_TYPE_ALL_MIDDLE.equals(recordE.getWhType())
							|| WhAllocationConstants.WH_TYPE_OUT_MIDDLE.equals(recordE.getWhType())) {
						// 设置派车状态待下发
						whRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
						// 根据仓库调拨单生成大仓出库单
						this.whOutRecordByWhAllocation(whRecordE, disparityRecordE, new ArrayList<>(resultMap.values()));
					} else {
						for (WhAllocationDetailE detail : disparityRecordE.getFrontRecordDetails()) {
							detail.setOutQty(detail.getAllotQty());
						}
						// 根据ID修改仓库调拨单状态recordStatus=4（已派车）
						int i = whAllocationMapper.updateDispatchSuccess(disparityRecordE.getId());
						AlikAssert.isTrue(i == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
						// 出非中台入中台直接修改为已出库
						if (WhAllocationConstants.WH_TYPE_IN_MIDDLE.equals(disparityRecordE.getWhType())) {
							i = whAllocationMapper.updateDeliverySuccess(disparityRecordE.getId());
							AlikAssert.isTrue(i == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
						}
						// 根据仓库调拨单生成大仓入库单
						this.whInRecordByWhAllocation(whRecordE, disparityRecordE);
					}

				}
				// 7、更新原单调拨数量,并更新是否有差异的字段
				// 重新设置最新的差异前置单的明细用于更新原单
				disparityRecordE.setFrontRecordDetails(allotSkuInfo);
				this.updateOriginRecord(disparityRecordE);
				// 9、处理返回值
				tipLogs.put("undercarriageCodes", undercarriageCodes);
				tipLogs.put("inSufficeStockCodes", inSufficeStockCodes);
				return JSONObject.toJSONString(tipLogs);
			} catch (RomeException e) {
				log.error(e.getMessage(), e);
				throw e;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC + ":" + e.getMessage());
			} finally {
				redisUtil.unLock(WH_ALLOT_DISPARITY_KEY, CLIENT_ID);
			}
		} else {
			throw new RomeException(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC + ":有单子正在处理中，请稍后再试");
		}
	}

	private void validateForCreateDisparity(WhAllocationE recordE) {
		// 判断原单是否存在
		AlikAssert.isNotNull(recordE, ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		// 判断单据状态是否为已确认、已出库、已派车中的一种
		AlikAssert.isTrue(
				FrontRecordStatusEnum.OUT_ALLOCATION.getStatus().equals(recordE.getRecordStatus())
						|| FrontRecordStatusEnum.TMS.getStatus().equals(recordE.getRecordStatus())
						|| FrontRecordStatusEnum.ENABLED.getStatus().equals(recordE.getRecordStatus()),
				ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001 + ":单据状态不支持创建差异调拨单");
		// 判断入库仓库是否存在
		RealWarehouse inWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(recordE.getInRealWarehouseCode(),
				recordE.getInFactoryCode());
		AlikAssert.isNotNull(inWarehouse, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
		recordE.setInWarehouse(inWarehouse);
	}

	/**
	 * copy原单信息并关联原单
	 *
	 * @param recordE
	 * @return
	 */
	private WhAllocationE copyBasicInfoFromOrigin(WhAllocationE recordE) {
		WhAllocationE disparityRecordE = new WhAllocationE();
		disparityRecordE.setInWarehouseId(recordE.getInWarehouseId());
		disparityRecordE.setInRealWarehouseCode(recordE.getInRealWarehouseCode());
		disparityRecordE.setInFactoryCode(recordE.getInFactoryCode());
		disparityRecordE.setOutWarehouseId(recordE.getOutWarehouseId());
		disparityRecordE.setOutRealWarehouseCode(recordE.getOutRealWarehouseCode());
		disparityRecordE.setOutFactoryCode(recordE.getOutFactoryCode());
		disparityRecordE.setInWarehouse(recordE.getInWarehouse());
		disparityRecordE.setWhType(recordE.getWhType());
		disparityRecordE.setBusinessType(recordE.getBusinessType());
		disparityRecordE.setInWarehouseName(recordE.getInWarehouseName());
		disparityRecordE.setOutWarehouseName(recordE.getOutWarehouseName());
		disparityRecordE.setAllotTime(recordE.getAllotTime());
		disparityRecordE.setExpeAogTime(recordE.getExpeAogTime());
		disparityRecordE.setRemark(recordE.getRemark());
		disparityRecordE.setIsReturnAllotcate(recordE.getIsReturnAllotcate());
		disparityRecordE.setInWarehouseMobile(recordE.getInWarehouseMobile());
		disparityRecordE.setOutWarehouseMobile(recordE.getOutWarehouseMobile());
		disparityRecordE.setIsQualityAllotcate(recordE.getIsQualityAllotcate());
		// 关联原单
		disparityRecordE.setOrginId(recordE.getId());
		disparityRecordE.setOrginRecord(recordE.getRecordCode());
		return disparityRecordE;
	}

	/**
	 * 创建差异调拨单及明细
	 *
	 * @param disparityRecordE
	 */
	private void createDisparityWhAllocation(WhAllocationE disparityRecordE, Long userId) {
		disparityRecordE.setAddType(WhAllocationConstants.ADD_TYPE_DISPARITY);
		disparityRecordE.setRecordCode(orderUtilService.queryOrderCode(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getCode()));
		// 需要创建pc
		disparityRecordE.setSyncStatus(WhAllocationConstants.WAIT_SYNC_STATUS);
		// 不存在差异
		disparityRecordE.setIsDisparity(WhAllocationConstants.IS_DISPARITY_FALSE);
		// 状态为已确认
		disparityRecordE.setRecordStatus(FrontRecordStatusEnum.ENABLED.getStatus());
		disparityRecordE.setCreator(userId);
		disparityRecordE.setAuditor(userId);
		// 差异调拨单的原始数量和调拨数量一致
		for (WhAllocationDetailE detailE : disparityRecordE.getFrontRecordDetails()) {
			detailE.setOrginQty(detailE.getAllotQty());
		}
		this.addAllocation(disparityRecordE, true);
	}

	/**
	 * 根据差异调拨单更新原单的调拨数量和是否有差异字段
	 *
	 * @param disparityRecordE
	 */
	private void updateOriginRecord(WhAllocationE disparityRecordE) {
		WhAllocationE originRecordE = this.queryWithDetailById(disparityRecordE.getOrginId());
		Map<String, WhAllocationDetailE> allotSkuMap = disparityRecordE.getFrontRecordDetails().stream()
				.collect(Collectors.toMap(WhAllocationDetailE::getOriginLineNo, Function.identity(), (v1, v2) -> v1));

		List<WhAllocationDetailE> updateDetails = new ArrayList<>();
		Integer hasDisparity = WhAllocationConstants.IS_DISPARITY_FALSE;
		for (WhAllocationDetailE detailE : originRecordE.getFrontRecordDetails()) {
			WhAllocationDetailE allot = allotSkuMap.get(detailE.getLineNo());
			if (null != allot) {
				detailE.setAllotQty(detailE.getAllotQty().add(allot.getAllotQty()));
				updateDetails.add(detailE);
			}
			// 判断是否有差异
			if (detailE.getAllotQty().compareTo(detailE.getOrginQty()) != 0) {
				hasDisparity = WhAllocationConstants.IS_DISPARITY_TRUE;
			}
		}
		if (CollectionUtils.isNotEmpty(updateDetails)) {
			whAllocationDetailMapper.updateDetailAllotQty(updateDetails);
		}
		if (WhAllocationConstants.IS_DISPARITY_FALSE.equals(hasDisparity)) {
			whAllocationMapper.updateDisparityStatus(originRecordE.getId(), hasDisparity);
		}
	}

	/**
	 * 导出调拨单
	 */
	@Override
	public List<WhAllocationExportTemplate> exportWhallot(WhAllocationPageDTO whAllocationPageDTO) {

		List<WhAllocationExportTemplate> exportList = new ArrayList<>();
		// 开始时间不能为空且必须在15天内
		AlikAssert.isNotNull(whAllocationPageDTO.getStartDate(), ResCode.ORDER_ERROR_6024,
				ResCode.ORDER_ERROR_6024_DESC);
		long diff = DateUtil.diff(new Date(), whAllocationPageDTO.getStartDate(), 24 * 60 * 60 * 1000);
		AlikAssert.isTrue(diff <= 15, ResCode.ORDER_ERROR_6025, ResCode.ORDER_ERROR_6025_DESC);
		// 查询列表
		whAllocationPageDTO.setPageIndex(1);
		whAllocationPageDTO.setPageSize(Integer.MAX_VALUE);
		PageInfo<WhAllocationPageDTO> pageList = this.queryList(whAllocationPageDTO);
		if (CollectionUtils.isNotEmpty(pageList.getList())) {
			List<WhAllocationPageDTO> list = pageList.getList();
			Map<Long, WhAllocationPageDTO> orderMap = list.stream()
					.collect(Collectors.toMap(WhAllocationPageDTO::getId, Function.identity(), (v1, v2) -> v1));
			List<Long> ids = list.stream().map(WhAllocationPageDTO::getId).distinct().collect(Collectors.toList());
			// 查询明细
			List<WhAllocationDetailE> detailList = whAllocationDetailMapper.queryDetailByFrontIds(ids);
			// 转化为基础单位
			Set<Long> skuIds = new HashSet<>();
			for (WhAllocationDetailE detail : detailList) {
				skuIds.add(detail.getSkuId());
				detail.setSkuQty(detail.getAllotQty());
				// 放入入库工厂
				if (orderMap.containsKey(detail.getFrontRecordId())) {
					WhAllocationPageDTO dto = orderMap.get(detail.getFrontRecordId());
					RealWarehouse inRealWarehouse = dto.getInRealWarehouse();
					detail.setInFactoryCode(inRealWarehouse.getFactoryCode());
				}
			}
			Map<String, List<String>> factorySkuMap = detailList.stream()
					.collect(Collectors.groupingBy(WhAllocationDetailE::getInFactoryCode,
							Collectors.mapping(WhAllocationDetailE::getSkuCode, Collectors.toList())));
			Map<String, List<String>> undercarriageMap = new HashMap<>();
			// 计算预下市商品
			for (Map.Entry<String, List<String>> entry : factorySkuMap.entrySet()) {
				String key = entry.getKey();
				List<String> value = entry.getValue().stream().distinct().collect(Collectors.toList());
				List<StorePurchaseAccessDTO> res = itemFacade.getStoreAccessFromSAPBySkuCodesAndStoreCode(key, value);
				if (CollectionUtils.isNotEmpty(res)) {
					List<String> undercarriageCodes = new ArrayList<>();
					for (StorePurchaseAccessDTO accessDTO : res) {
						boolean flag = null != accessDTO
								&& ("01".equals(accessDTO.getIsAccess()) || "02".equals(accessDTO.getIsAccess()));
						if (flag) {
							undercarriageCodes.add(accessDTO.getSkuCode());
						}
					}
					if (CollectionUtils.isNotEmpty(undercarriageCodes)) {
						undercarriageMap.put(key, undercarriageCodes);
					}
				}
			}

			skuQtyUnitTool.convertRealToBasic(detailList);
			// 查询商品信息
			List<SkuInfoExtDTO> skuList = itemFacade.skuBySkuIds(new ArrayList<>(skuIds));
			Map<Long, SkuInfoExtDTO> skuMap = skuList.stream()
					.collect(Collectors.toMap(SkuInfoExtDTO::getId, Function.identity(), (v1, v2) -> v1));
			// 设置导出数据
			for (WhAllocationDetailE detail : detailList) {
				WhAllocationPageDTO order = orderMap.get(detail.getFrontRecordId());
				WhAllocationExportTemplate extemplate = whAllocationConvertor.convertPageDTO2Template(order);
				extemplate.setRecordTypeStr(FrontRecordStatusEnum.getDescByType(order.getRecordStatus()));
				// 设置出库仓库
				if (order.getOutRealWarehouse() != null) {
					extemplate.setOutFactoryCode(order.getOutRealWarehouse().getFactoryCode());
					extemplate.setOutWarehouseCode(order.getOutRealWarehouse().getRealWarehouseOutCode());
				}
				// 设置入库仓库
				if (order.getInRealWarehouse() != null) {
					extemplate.setInFactoryCode(order.getInRealWarehouse().getFactoryCode());
					extemplate.setInWarehouseCode(order.getInRealWarehouse().getRealWarehouseOutCode());
				}
				// 设置调拨类型
				if (WhAllocationConstants.RDC_ALLOCATION.equals(order.getBusinessType())) {
					extemplate.setBusinessTypeStr(WhAllocationConstants.RDC_ALLOCATION_STR);
				} else if (WhAllocationConstants.INNER_ALLOCATION.equals(order.getBusinessType())) {
					extemplate.setBusinessTypeStr(WhAllocationConstants.INNER_ALLOCATION_STR);
				}
				// 设置明细
				detailCopier.copy(detail, extemplate, null);
				SkuInfoExtDTO skuInfo = skuMap.get(detail.getSkuId());
				if (skuInfo != null) {
					extemplate.setSkuName(skuInfo.getName());
				}
				// 设置预下市
				extemplate.setUnderCarriageStr("正常");
				if (undercarriageMap.containsKey(detail.getInFactoryCode())) {
					List<String> undercarriageCodes = undercarriageMap.get(detail.getInFactoryCode());
					if (CollectionUtils.isNotEmpty(undercarriageCodes)) {
						if (undercarriageCodes.contains(detail.getSkuCode())) {
							extemplate.setUnderCarriageStr("下市或预下市");
						}
					}
				}
				extemplate.setCreateTime(order.getCreateTime());
				extemplate.setUnit(detail.getUnit());
				extemplate.setDiffQty(detail.getOrginQty().subtract(detail.getAllotQty())
						.setScale(CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN));
				extemplate.setOutBasicQty(detail.getOutQty().multiply(detail.getScale())
						.setScale(CommonConstants.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN));
				extemplate.setBasicUnit(detail.getBasicUnit());
				exportList.add(extemplate);
			}
		}
		return exportList;
	}

	/**
	 * 出库实仓库存对象,封装商品信息
	 *
	 * @param stockList
	 * @return
	 */
	private List<RealWarehouseStockDTO> dealStockInfo(List<RealWarehouseStockDTO> stockList) {
		// 设置商品信息
		List<Long> skuIdList = stockList.stream().map(RealWarehouseStockDTO::getSkuId).distinct()
				.collect(Collectors.toList());
		List<String> skuCodeList = stockList.stream().map(RealWarehouseStockDTO::getSkuCode).distinct()
				.collect(Collectors.toList());
		List<SkuInfoExtDTO> skuInfoList = itemFacade.skuBySkuIds(skuIdList);
		// 查询单位及转换信息
		List<SkuUnitExtDTO> skuUnitList = itemFacade.querySkuUnits(skuCodeList);
		Map<Long, List<SkuUnitExtDTO>> skuUnitMap = skuUnitList.stream()
				.collect(Collectors.groupingBy(SkuUnitExtDTO::getSkuId));
		if (CollectionUtils.isNotEmpty(skuInfoList)) {
			Map<Long, SkuInfoExtDTO> skuMap = skuInfoList.stream()
					.collect(Collectors.toMap(SkuInfoExtDTO::getId, Function.identity(), (v1, v2) -> v1));
			for (RealWarehouseStockDTO rwDto : stockList) {
				// 设置基础信息
				SkuInfoExtDTO skuInfo = skuMap.get(rwDto.getSkuId());
				if (skuInfo != null) {
					rwDto.setSkuCode(skuInfo.getSkuCode());
					rwDto.setCategoryName(skuInfo.getCategoryName());
					rwDto.setSkuName(skuInfo.getName());
					rwDto.setBaseUnit(skuInfo.getSpuUnitName());
				}
				// 设置单位换算信息
				List<SkuUnitExtDTO> unitList = skuUnitMap.get(rwDto.getSkuId());
				if (CollectionUtils.isNotEmpty(unitList)) {
					List<WhSkuUnitDTO> whUnitList = whAllocationConvertor.convertUnitList2WhUnitList(unitList);
					List<WhSkuUnitDTO> singleList = this.dealUnitList(whUnitList);
					rwDto.setSkuUnitList(singleList);
				}
			}
		}
		return stockList;
	}

	/**
	 * 处理前端展示的单位
	 * 
	 * @param whUnitList
	 * @return
	 */
	private List<WhSkuUnitDTO> dealUnitList(List<WhSkuUnitDTO> whUnitList) {
		List<WhSkuUnitDTO> singleList = new ArrayList<>();
		Map<Long, List<WhSkuUnitDTO>> skuListMap = whUnitList.stream()
				.collect(Collectors.groupingBy(WhSkuUnitDTO::getSkuId));
		for (Long skuId : skuListMap.keySet()) {
			List<WhSkuUnitDTO> list = skuListMap.get(skuId);
			Map<String, WhSkuUnitDTO> skuMap = new HashMap<>();
			for (WhSkuUnitDTO unit : list) {
				if (!skuMap.containsKey(unit.getUnitCode())) {
					skuMap.put(unit.getUnitCode(), unit);
				}
			}
			singleList.addAll(skuMap.values());
		}
		return singleList;
	}

	/**
	 * 根据出入库单据编号查询仓库调拨单
	 */
	@Override
	public WhAllocationDTO queryWhAllocationByRecordCode(String recordCode) {
		String frontRecord = frontWarehouseRecordRelationMapper.getFrontRecordCodeByRecordCode(recordCode);
		if (StringUtils.isBlank(frontRecord)) {
			return null;
		}
		return whAllocationConvertor.convertE2DTO((whAllocationMapper.queryByRecordCode(frontRecord)));
	}
	
	/**
	 * 根据出入库单据编号查询仓库调拨单
	 */
	private WhAllocationE queryByRecordCode(String recordCode) {
		String frontRecord = frontWarehouseRecordRelationMapper.getFrontRecordCodeByRecordCode(recordCode);
		if (StringUtils.isBlank(frontRecord)) {
			return null;
		}
		WhAllocationE whAllocationE = whAllocationMapper.queryByRecordCode(frontRecord);
		if (whAllocationE == null) {
			return null;
		}
		return whAllocationE;
	}

	/**
	 * 根据出入库单据编号查询仓库调拨单（包含明细）
	 */
	private WhAllocationE queryWithDetailByRecordCode(String recordCode) {
		String frontRecord = frontWarehouseRecordRelationMapper.getFrontRecordCodeByRecordCode(recordCode);
		if (StringUtils.isBlank(frontRecord)) {
			return null;
		}
		WhAllocationE whAllocationE = whAllocationMapper.queryByRecordCode(frontRecord);
		if (whAllocationE == null) {
			return null;
		}
		List<WhAllocationDetailE> whAllocationDetailEList = whAllocationDetailMapper
				.queryDetailByRecordCode(frontRecord);
		if (CollectionUtils.isEmpty(whAllocationDetailEList)) {
			return null;
		}
		whAllocationE.setFrontRecordDetails(whAllocationDetailEList);
		return whAllocationE;
	}

	/**
	 * 配置单据级别的sku实仓虚仓分配比例
	 */
	@Override
	public void allotConfig(List<RecordRealVirtualStockSyncRelationDTO> config) {
		recordRealVirtualStockSyncRelationService.insertRecordRealVirtualStockRelation(config);
	}

	/**
	 * 根据条件查询实仓信息-运营平台查询接口,不分页
	 */
	@Override
	public List<RealWarehouse> queryForAdmin(RealWarehouseParamDTO paramDTO) {
		List<RealWarehouse> realWarehouseList = stockWhAllocationFacade.queryForAdmin(paramDTO);
		return realWarehouseList;
	}

	/**
	 * 仓库调拨单出库通知
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void outRecordNotify(StockNotifyDTO stockNotifyDTO) {
		// 根据出入库单据编号查询仓库调拨单
		WhAllocationE whAllocationE = this.queryWithDetailByRecordCode(stockNotifyDTO.getRecordCode());
		AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		// 查询单据及明细
		WarehouseRecordE outRecordE = this.getRecordWithDetailByRecordCode(stockNotifyDTO.getRecordCode());
		AlikAssert.isNotNull(outRecordE, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026);
		// 计算并设置实出,出库不用记录差异,直接按照实际数量出
		List<WhAllocationDetailE> frontDetailList = this.calculateOutDetail(whAllocationE, outRecordE, 2);
		if (CollectionUtils.isNotEmpty(frontDetailList)) {
			whAllocationDetailMapper.updateDetailOutQty(frontDetailList);
		}
		// 修改前置单为已出库
		Integer i = whAllocationMapper.updateDeliverySuccess(whAllocationE.getId());
		AlikAssert.isTrue(i == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
		
		//基于销售预约单创建
		if(WhAllocationConstants.ADD_TYPE_RESERVATION.equals(whAllocationE.getAddType())) {
			orderService.allotOutNotify(whAllocationE);
		} else if(WhAllocationConstants.ADD_TYPE_PACK_DEMAND.equals(whAllocationE.getAddType())){
			//基于包装需求单创建
			packDemandComponentService.allotOutNotify(stockNotifyDTO.getDetailDTOList());
		}
		
		// 出入都为中台需要创建入库单
		if (whAllocationE.getWhType().equals(WhAllocationConstants.WH_TYPE_ALL_MIDDLE)) {
			int j = 0;
			for (WhAllocationDetailE whAllocationRecordDetailE : whAllocationE.getFrontRecordDetails()) {
				if (whAllocationRecordDetailE.getOutQty().compareTo(BigDecimal.ZERO) != 0) {
					j++;
				}
			}
			if (j > 0) {
				this.createInOrderByOutRecord(whAllocationE, outRecordE);
			}
		}
	}

	/**
	 * 根据出库单创建入库单
	 */
	private void createInOrderByOutRecord(WhAllocationE frontRecord, WarehouseRecordE outRecordE) {
		// 生成入库单
		WarehouseRecordE inRecordE = new WarehouseRecordE();
		inRecordE.setTmsRecordCode(outRecordE.getTmsRecordCode());
		inRecordE.setExpectReceiveDateStart(frontRecord.getExpeAogTime());
		inRecordE.setSyncTmsbStatus(0);
		this.whInRecordByOutRecord(frontRecord, inRecordE, outRecordE);
	}

	/**
	 * 计算并设置实出,并更新数据库
	 *
	 * @param frontRecordE
	 * @param recordE
	 * @param type
	 *            1按照planqty 2按照actualqty计算
	 */
	private List<WhAllocationDetailE> calculateOutDetail(WhAllocationE frontRecordE, WarehouseRecordE recordE,
			Integer type) {
		List<WhAllocationDetailE> frontDetailList = frontRecordE.getFrontRecordDetails();
		frontDetailList.forEach(wrRecord -> wrRecord.setSkuQty(wrRecord.getAllotQty()));
		// 得到换算比例
		skuQtyUnitTool.convertRealToBasic(frontDetailList);
		List<WarehouseRecordDetailE> whDetailList = recordE.getWarehouseRecordDetailList();
		Map<String, WarehouseRecordDetailE> map = whDetailList.stream()
				.collect(Collectors.toMap(WarehouseRecordDetailE::getLineNo, Function.identity(), (v1, v2) -> v1));
		for (WhAllocationDetailE frontDetail : frontDetailList) {
			WarehouseRecordDetailE whDetail = map.get(frontDetail.getLineNo());
			if (whDetail != null) {
				// 需要按照单位换算成实际的数量
				if (type == 1) {
					BigDecimal planQty = whDetail.getPlanQty();
					frontDetail.setOutQty(planQty);
				} else {
					BigDecimal actualQty = whDetail.getActualQty();
					frontDetail.setOutQty(actualQty);
				}
			}
		}
		// 更新前置单明细实出和实入
		this.transformOutActualQty(frontDetailList);
		return frontDetailList;
	}

	/**
	 * 转化实出的数量
	 * 
	 * @param detailEList
	 */
	public void transformOutActualQty(List<WhAllocationDetailE> detailEList) {
		detailEList.forEach(wrRecord -> wrRecord.setBasicSkuQty(wrRecord.getOutQty()));
		skuQtyUnitTool.convertBasicToReal(detailEList);
		for (WhAllocationDetailE detailE : detailEList) {
			detailE.setOutQty(detailE.getSkuQty());
		}
	}

	/**
	 * 转化实入的数量
	 * 
	 * @param detailEList
	 */
	public void transformInActualQty(List<WhAllocationDetailE> detailEList) {
		detailEList.forEach(wrRecord -> wrRecord.setBasicSkuQty(wrRecord.getInQty()));
		skuQtyUnitTool.convertBasicToReal(detailEList);
		for (WhAllocationDetailE detailE : detailEList) {  
			detailE.setInQty(detailE.getSkuQty());
		}
	}

	/**
	 * 仓库调拨单入库通知
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void inRecordNotify(StockNotifyDTO stockNotifyDTO) {
		// 根据出入库单据编号查询仓库调拨单
		WhAllocationE whAllocationE = this.queryWithDetailByRecordCode(stockNotifyDTO.getRecordCode());
		AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		// 更新前置单状态为已入库
		whAllocationMapper.updateToInWh(whAllocationE.getId());
		
		List<WarehouseRecordDetailE> details = new ArrayList<WarehouseRecordDetailE>();
		stockNotifyDTO.getDetailDTOList().forEach(e -> {
			WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
			warehouseRecordDetailE.setSkuCode(e.getSkuCode());
			warehouseRecordDetailE.setActualQty(e.getActualQty());
			warehouseRecordDetailE.setLineNo(e.getLineNo());
			details.add(warehouseRecordDetailE);
		});
		// 计算并设置实入,出库不用记录差异,直接按照实际数量出
		this.calculateInDetail(whAllocationE, details);
		
		//基于销售预约单创建
		if(WhAllocationConstants.ADD_TYPE_RESERVATION.equals(whAllocationE.getAddType())) {
			orderService.allotInNotify(whAllocationE);
		}
	}

	/**
	 * 将批次信息转换为按skuId维度的出入库明细
	 * 
	 * @param rwBatchS
	 * @param warehouseRecordE
	 * @return
	 */
	private List<WarehouseRecordDetailE> batchInfoToWarehouseRecordDetails(List<RwBatchDTO> rwBatchS, WarehouseRecordE warehouseRecordE) {
		Map<String, WarehouseRecordDetailE> warehouseRecordEDetailMap = warehouseRecordE.getWarehouseRecordDetailList()
				.stream().collect(Collectors.toMap(WarehouseRecordDetailE::getLineNo, Function.identity(), (v1, v2) -> v1));
		List<WarehouseRecordDetailE> result = new ArrayList<>();
		Map<String, List<RwBatchDTO>> skuBatchInfoMap = rwBatchS.stream()
				.collect(Collectors.groupingBy(RwBatchDTO::getLineNo));
		for (Map.Entry<String, List<RwBatchDTO>> mapEntry : skuBatchInfoMap.entrySet()) {
			WarehouseRecordDetailE detail = new WarehouseRecordDetailE();
			BigDecimal actualQty = BigDecimal.ZERO;
			for (RwBatchDTO batch : mapEntry.getValue()) {
				detail.setSkuId(batch.getSkuId());
				detail.setSkuCode(batch.getSkuCode());
				actualQty = actualQty.add(batch.getActualQty());
				detail.setLineNo(warehouseRecordEDetailMap.get(batch.getLineNo()).getLineNo());
			}
			detail.setActualQty(actualQty);
			result.add(detail);
		}
		return result;
	}

	/**
	 * 计算并设置实入,并更新数据库
	 *
	 * @param frontRecordE
	 * @param details
	 */
	private void calculateInDetail(WhAllocationE whAllocationE, List<WarehouseRecordDetailE> details) {
		List<WhAllocationDetailE> detailEList = whAllocationE.getFrontRecordDetails();
		detailEList.forEach(wrRecord -> wrRecord.setSkuQty(wrRecord.getAllotQty()));
		// 得到换算比例
		skuQtyUnitTool.convertRealToBasic(detailEList);
		Map<String, WarehouseRecordDetailE> map = details.stream()
				.collect(Collectors.toMap(WarehouseRecordDetailE::getLineNo, Function.identity(), (v1, v2) -> v1));
		List<WhAllocationDetailE> updateDetail = new ArrayList<>();
		for (WhAllocationDetailE frontDetail : detailEList) {
			WarehouseRecordDetailE whDetail = map.get(frontDetail.getLineNo());
			if (whDetail != null) {
				// 需要按照单位换算成实际的数量
				BigDecimal actualQty = whDetail.getActualQty();
				frontDetail.setInQty(actualQty);
				updateDetail.add(frontDetail);
			}
		}
		// 更新前置单明细实出和实入
		this.transformInActualQty(detailEList);
		if (CollectionUtils.isNotEmpty(detailEList)) {
			whAllocationDetailMapper.updateDetailInQty(updateDetail);
		}
	}

	/**
	 * 仓库调拨派车通知
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void tmsNotify(TmsNotifyDTO tmsNotifyDTO) {
		//根据出入库单据编号查询仓库调拨单
		WhAllocationE whAllocationE = this.queryByRecordCode(tmsNotifyDTO.getRecordCode());
		AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		// 仓库调拨单单据状态recordStatus=1（已确认）
		if (Integer.valueOf(1).equals(whAllocationE.getRecordStatus())) {
			// 根据ID修改仓库调拨单状态recordStatus=4（已派车）
			int i = whAllocationMapper.updateDispatchSuccess(whAllocationE.getId());
			AlikAssert.isTrue(i == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
			// 出非中台入中台直接修改为已出库
			if (WhAllocationConstants.WH_TYPE_IN_MIDDLE.equals(whAllocationE.getWhType())) {
				i = whAllocationMapper.updateDeliverySuccess(whAllocationE.getId());
				AlikAssert.isTrue(i == 1, ResCode.ORDER_ERROR_6018, ResCode.ORDER_ERROR_6018_DESC);
			}
		}
	}

	/**
	 * 查询待推送给库存中心的poNo列表
	 */
	@Override
	public List<String> queryPoNoToStock() {
		Date endTime = new Date();
        Date startTime = DateUtils.addDays(endTime, -30);
		return warehouseRecordMapper.queryPoNoToStock(startTime, endTime);
	}

	/**
	 * 处理待推送给库存中心的poNo
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handlePoNoToStock(String recordCode) {
		// 1、根据单据编号查询出入库单（包含明细）
		WarehouseRecordE warehouseRecordE = this.getRecordWithDetailByRecordCode(recordCode);
		if (warehouseRecordE == null) {
			throw new RomeException(ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
		}
		// 2、获取同步库存中心状态
		Integer stockStatus = warehouseRecordE.getSyncStockStatus();
		if (Integer.valueOf(0).equals(stockStatus)) {
			throw new RomeException(ResCode.ORDER_ERROR_6027, ResCode.ORDER_ERROR_6027_DESC);
		}
		if (Integer.valueOf(2).equals(stockStatus)) {
			return;
		}
		List<WarehouseRecordDetailE> detailList = warehouseRecordE.getWarehouseRecordDetailList();
		List<PoNoDTO> poNoList = new ArrayList<PoNoDTO>();
		// 3、构建待推送给库存中心的poNo
		detailList.forEach(e -> {
			PoNoDTO poNoDTO = new PoNoDTO();
			poNoDTO.setSapPoNo(e.getSapPoNo());
			poNoDTO.setRecordCode(recordCode);
			poNoDTO.setDeliveryLineNo(e.getDeliveryLineNo());
			poNoDTO.setLineNo(e.getLineNo());
			poNoDTO.setSkuCode(e.getSkuCode());
			poNoList.add(poNoDTO);
		});
		if (CollectionUtils.isNotEmpty(poNoList)) {
			//businessType=1出库单2入库单
			Integer syncTmsbStatus = 0;
			if(Integer.valueOf(1).equals(warehouseRecordE.getBusinessType())) {
				// 同步派车系统状态 0无需同步 1待同步 2已同步
				syncTmsbStatus = 1;
			}
			// 同步库存中心状态 0无需同步 1待同步 2已同步
			Integer syncStockStatus = 2;
			// 更新同步库存状态为已下发
			int result = warehouseRecordMapper.updateSyncTmsbAndStockStatus(recordCode, syncTmsbStatus,
					syncStockStatus);
			if (result > 0) {
				// 4、下发poNo给库存中心
				stockWhAllocationFacade.updateLineNoAndSapPoNo(poNoList);
			}
		}
	}

	/**
	 * 查询虚仓预计算
	 */
	@Override
	public List<AllocationCalQtyRes> queryPreCalculate(QueryPreCalculateDTO queryPreCalculateDTO) {
		// 根据出入库单据编号查询仓库调拨单
		WhAllocationE whAllocationE = this.queryWithDetailByRecordCode(queryPreCalculateDTO.getRecordCode());
		AlikAssert.isNotNull(whAllocationE, ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		// 查询单据及明细
		WarehouseRecordE inRecordE = this.getRecordWithDetailByRecordCode(queryPreCalculateDTO.getRecordCode());
		AlikAssert.isNotNull(inRecordE, ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026);
		// 获取批次信息
		List<RwBatchDTO> rwBatchList = queryPreCalculateDTO.getRwBatchs();
		// 根据批次明细转换为skuId维度的入库明细
		List<WarehouseRecordDetailE> details = this.batchInfoToWarehouseRecordDetails(rwBatchList, inRecordE);
		// 虚仓分配预计算,originCode不为空说明是差异调拨单，用原单的配置
		List<AllocationCalQtyRes> preResult = this.preCalculate(details,
				whAllocationE.getOrginRecord() == null ? whAllocationE.getRecordCode() : whAllocationE.getOrginRecord(),
				whAllocationE.getInWarehouseId());
		return 	preResult;	
	}

	/**
	 * 根据预约单创建调拨单（团购专用）
	 */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void createWhallotByOrder(OrderE orderE, WhAllocationE whAllocationE) {
        Integer whType = this.dealWhType(whAllocationE);
        //设置调拨类型为团购预约单创建
        whAllocationE.setAddType(WhAllocationConstants.ADD_TYPE_RESERVATION);
        skuQtyUnitTool.convertRealToBasicSku(whAllocationE.getFrontRecordDetails(), SkuUnitTypeEnum.TRANSPORT_UNIT.getId(), BigDecimal.ROUND_DOWN);
        for(WhAllocationDetailE detailE : whAllocationE.getFrontRecordDetails()){
        	detailE.setAllotQty(detailE.getBasicSkuQty());
        	detailE.setOrginQty(detailE.getBasicSkuQty());
            detailE.setBasicSkuQty(detailE.getBasicSkuQty());
            detailE.setBasicUnit(detailE.getUnit());
            detailE.setBasicUnitCode(detailE.getUnitCode());
        }
        whAllocationE.setWhType(whType);
        whAllocationE.setRecordStatus(WhAllocationConstants.CONFIM_STATUS);
        whAllocationE.setSyncStatus(WhAllocationConstants.WAIT_SYNC_STATUS);
        whAllocationE.setIsDisparity(WhAllocationConstants.IS_DISPARITY_FALSE);  
        whAllocationE.setOrginId(-1L);
        itemInfoTool.convertSkuCode(whAllocationE.getFrontRecordDetails());
        this.addAllocation(whAllocationE, false);
        List<String> undercarriageCodes = new ArrayList<String>();
         this.calculateForCreateDisparity(whAllocationE, undercarriageCodes, false);
        //过滤（预）下市商品并更新预下市商品的调拨数量
        if(CollectionUtils.isNotEmpty(undercarriageCodes)){
           //预下市商品直接提示出去
            throw new RomeException(ResCode.ORDER_ERROR_6038, ResCode.ORDER_ERROR_6038_DESC + StringUtils.join(undercarriageCodes,","));
        }
        //团购不考虑拆装
        //出入都是中台或者出仓为中台
        if (whType.equals(WhAllocationConstants.WH_TYPE_ALL_MIDDLE) || whType.equals(WhAllocationConstants.WH_TYPE_OUT_MIDDLE)) {
        	//根据预约单号释放预约单锁定库存
        	stockFacade.unlockOrderStock(orderE.getOrderCode());
            //生成出库单
        	WarehouseRecordE outRecordE = new WarehouseRecordE();
        	outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
        	//指定虚仓调拨
        	String virWarehouseCode = orderE.getVmWarehouseCode();
        	outRecordE.setVwCode(virWarehouseCode);
        	this.whOutRecordByWhAllocation(outRecordE, whAllocationE, new ArrayList<>());
        } else {
            //出库仓不为中台,不支持
            throw new RomeException(ResCode.ORDER_ERROR_6039, ResCode.ORDER_ERROR_6039_DESC);
        }
	}

	/**
	 * 根据需求单、原料明细创建调拨单
	 */
	@Override
	public void createDemandAllot(PackDemandE packDemandE, List<DemandAllotDetailDTO> demandAllotDetailList) {
		Integer businessType = null;
        if(packDemandE.getOutRealWarehouse().getFactoryCode().equals(packDemandE.getInRealWarehouse().getFactoryCode())){
            //如果出库工厂和入库工厂设置一致，设置为内部调拨
            businessType = WhAllocationConstants.INNER_ALLOCATION;
        }else {
            businessType = WhAllocationConstants.RDC_ALLOCATION;
        }
        WhAllocationE whAllocationE = new  WhAllocationE();
        //生成调拨单号
        String allotCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.WAREHOUSE_ALLOCATION_RECORD.getCode());
        whAllocationE.setRecordCode(allotCode);
        whAllocationE.setBusinessType(businessType);
        whAllocationE.setAddType(WhAllocationConstants.ADD_TYPE_PACK_DEMAND);
        whAllocationE.setInWarehouse(packDemandE.getInRealWarehouse());
        whAllocationE.setInWarehouseId(packDemandE.getInRealWarehouse().getId());
        whAllocationE.setInRealWarehouseCode(packDemandE.getInRealWarehouse().getRealWarehouseOutCode());
        whAllocationE.setInFactoryCode(packDemandE.getInRealWarehouse().getFactoryCode());
        whAllocationE.setOutWarehouse(packDemandE.getOutRealWarehouse());
        whAllocationE.setOutWarehouseId(packDemandE.getOutRealWarehouse().getId());
        whAllocationE.setOutRealWarehouseCode(packDemandE.getOutRealWarehouse().getRealWarehouseOutCode());
        whAllocationE.setOutFactoryCode(packDemandE.getOutRealWarehouse().getFactoryCode());
        whAllocationE.setAllotTime(new Date());
        whAllocationE.setExpeAogTime(new Date());
        whAllocationE.setIsQualityAllotcate(WhAllocationConstants.NOT_QUALITY_ALLOCATE);
        whAllocationE.setIsReturnAllotcate(WhAllocationConstants.NOT_RETURN_ALLOCATE);
        whAllocationE.setCreator(packDemandE.getModifier());
        whAllocationE.setModifier(packDemandE.getModifier());
        whAllocationE.setRecordStatus(WhAllocationConstants.CONFIM_STATUS);
        whAllocationE.setSyncStatus(WhAllocationConstants.WAIT_SYNC_STATUS);
        whAllocationE.setIsDisparity(WhAllocationConstants.IS_DISPARITY_FALSE);  
        whAllocationE.setOrginId(-1L);
        Integer whType = this.dealWhType(whAllocationE);
        whAllocationE.setWhType(whType);
        List<WhAllocationDetailE> detailList = new ArrayList<>();
        for (DemandAllotDetailDTO demandAllotDetailDTO : demandAllotDetailList) {
            WhAllocationDetailE detail = new WhAllocationDetailE();
            detail.setAllotQty(demandAllotDetailDTO.getAllotQty());
            detail.setSkuQty(demandAllotDetailDTO.getAllotQty());
            detail.setSkuCode(demandAllotDetailDTO.getSkuCode());
            detail.setUnit(demandAllotDetailDTO.getUnit());
            detail.setUnitCode(demandAllotDetailDTO.getUnitCode());
            detail.setOrginQty(demandAllotDetailDTO.getAllotQty());
            detail.setOrderLineNo(demandAllotDetailDTO.getOrderLineNo());
            detailList.add(detail);
        }
        whAllocationE.setFrontRecordDetails((detailList));
        //设置skuCode或者skuId
        itemInfoTool.convertSkuCode(whAllocationE.getFrontRecordDetails());
        //发货单位向上取整后再转换成基础单位
        skuQtyUnitTool.convertRealToBasicSku(whAllocationE.getFrontRecordDetails(), SkuUnitTypeEnum.TRANSPORT_UNIT.getId(), BigDecimal.ROUND_UP);
        this.addAllocation(whAllocationE, false);
        
        //保存调拨业务单据关联关系
        this.svaeAllotRecordRelation(packDemandE.getRecordCode(), whAllocationE.getFrontRecordDetails());
        
        List<String> undercarriageCodes = new ArrayList<>();
        this.calculateForCreateDisparity(whAllocationE, undercarriageCodes, false);
       //过滤（预）下市商品并更新预下市商品的调拨数量
       if(CollectionUtils.isNotEmpty(undercarriageCodes)){
          //预下市商品直接提示出去
           throw new RomeException(ResCode.ORDER_ERROR_6038, ResCode.ORDER_ERROR_6038_DESC + StringUtils.join(undercarriageCodes,","));
       }
       //出入都是中台或者出仓为中台
       if (whType.equals(WhAllocationConstants.WH_TYPE_ALL_MIDDLE) || whType.equals(WhAllocationConstants.WH_TYPE_OUT_MIDDLE)) {
        //生成出库单
       	WarehouseRecordE outRecordE = new WarehouseRecordE();
       	outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
       	outRecordE.setVirtualWarehouseCode(packDemandE.getOutVirtualWarehouseCode());
       	this.whOutRecordByWhAllocation(outRecordE, whAllocationE, new ArrayList<>());
       } else {
           //出库仓不为中台,不支持
           throw new RomeException(ResCode.ORDER_ERROR_6039, ResCode.ORDER_ERROR_6039_DESC);
       }
	}

	/**
	 * 保存调拨业务单据关联关系
	 * 
	 * @param recordCode
	 * @param whAllocationDetailEList
	 */
	private void svaeAllotRecordRelation(String recordCode, List<WhAllocationDetailE> whAllocationDetailEList) {
		List<AllotRecordRelationE> allotRecordRelationEList = new ArrayList<AllotRecordRelationE>();
		whAllocationDetailEList.forEach(e -> {
			AllotRecordRelationE allotRecordRelationE = new AllotRecordRelationE();
			allotRecordRelationE.setAllotCode(e.getRecordCode());
			allotRecordRelationE.setRecordCode(recordCode);
			allotRecordRelationE.setType(1);
			allotRecordRelationE.setLineNo(String.valueOf(e.getId()));
			allotRecordRelationE.setOrderLineNo(e.getOrderLineNo());
			allotRecordRelationEList.add(allotRecordRelationE);
		});
		if(CollectionUtils.isNotEmpty(allotRecordRelationEList)) {
			allotRecordRelationMapper.batchInsertAllotRecordRelation(allotRecordRelationEList);
		}
	}

	/**
	 * 取消调拨单（取消预约单时用）
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancleAllot(String recordCode, Long userId) {
		WhAllocationE whAllocationE = whAllocationMapper.queryByRecordCode(recordCode);
		if(whAllocationE == null) {
			throw new RomeException(ResCode.ORDER_ERROR_6023, ResCode.ORDER_ERROR_6023_DESC);
		}
		Integer i = whAllocationMapper.updateAuditFail(whAllocationE.getId(), userId);
		AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_6015, ResCode.ORDER_ERROR_6015_DESC);
		List<WarehouseRecordE> warehouseRecordEList = this.queryWarehouseRecordByFrontRecordCode(whAllocationE.getRecordCode());
		if(CollectionUtils.isEmpty(warehouseRecordEList)) {
			throw new RomeException(ResCode.ORDER_ERROR_6026, ResCode.ORDER_ERROR_6026_DESC);
		}
		//仓库调拨出库单
		WarehouseRecordE warehouseRecordE = warehouseRecordEList.get(0);
		Integer recordStatus = warehouseRecordE.getRecordStatus();
		if(WarehouseRecordStatusEnum.DISABLED.getStatus().equals(recordStatus)) {
			return;
		}
		if(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus().equals(recordStatus)) {
			throw new RomeException(ResCode.ORDER_ERROR_6041, ResCode.ORDER_ERROR_6041_DESC);
		}
		// 取消后置单
		int j = warehouseRecordMapper.updateCancelOutRecord(warehouseRecordE.getRecordCode(), userId);
		AlikAssert.isTrue(j > 0, ResCode.ORDER_ERROR_6015, ResCode.ORDER_ERROR_6015_DESC);
		List<CancelRecordDTO> cancelRecordList = new ArrayList<CancelRecordDTO>();
		CancelRecordDTO cancelRecordDTO = new CancelRecordDTO();
		cancelRecordDTO.setRecordCode(warehouseRecordE.getRecordCode());
		cancelRecordDTO.setRecordType(warehouseRecordE.getRecordType());
		cancelRecordDTO.setIsForceCancel(1);
		cancelRecordList.add(cancelRecordDTO);
		List<CancelResultDTO> cancelResultList = stockRecordFacade.cancelRecord(cancelRecordList);
		List<Boolean> statusList =  cancelResultList.stream().filter(cancelResultDTO -> cancelResultDTO.getStatus() == false).map(CancelResultDTO :: getStatus).distinct().collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(statusList)) {
			throw new RomeException(ResCode.ORDER_ERROR_6032, ResCode.ORDER_ERROR_6032_DESC);
		}
	}

}