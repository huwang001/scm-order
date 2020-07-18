package com.lyf.scm.core.service.online.impl;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WDTRecordConst;
import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.api.dto.online.QueryWDTOrderDTO;
import com.lyf.scm.core.api.dto.online.WDTOrderPageInfoDTO;
import com.lyf.scm.core.api.dto.online.WDTStockLogisticInfoParamDTO;
import com.lyf.scm.core.domain.convert.online.WDTSaleConvertor;
import com.lyf.scm.core.domain.entity.online.RecordPoolE;
import com.lyf.scm.core.domain.entity.online.WDTSaleE;
import com.lyf.scm.core.domain.model.online.WDTSaleDO;
import com.lyf.scm.core.mapper.online.RecordPoolMapper;
import com.lyf.scm.core.mapper.online.WDTSaleDetailMapper;
import com.lyf.scm.core.mapper.online.WDTSaleMapper;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.service.online.AddressService;
import com.lyf.scm.core.service.online.WDTOrderService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */

@Slf4j
@Service("wdtOrderService")
public class WDTOrderServiceImpl implements WDTOrderService {
    @Resource
    private AddressService addressService;
    @Resource
    private WDTSaleMapper wdtSaleMapper;
    @Resource
    private WDTSaleDetailMapper wdtSaleDetailMapper;

    @Resource
    private RecordPoolMapper recordPoolMapper;

    @Resource
    private WDTSaleConvertor wdtSaleConvertor;


    @Override
    public RealWarehouse lockStockByRecord(OnlineOrderDTO onlineOrderDTO) {
        return null;
    }

    @Override
    public void saveLogisticInfo(List<WDTStockLogisticInfoParamDTO> paramDTO) {

    }

    @Override
    public PageInfo<WDTOrderPageInfoDTO> queryForSplitPage(QueryWDTOrderDTO queryWDTOrderDTO) {
        return null;
    }

    @Override
    public WDTOrderPageInfoDTO querySplitDetail(WDTOrderPageInfoDTO wdtOrderPageInfoDTO) {
        return null;
    }

    @Override
    public boolean splitOrder(WDTOrderPageInfoDTO wdtOrderPageInfoDTO) {
        return false;
    }

    @Override
    public boolean recalculateHouse(WDTOrderPageInfoDTO wdtOrderPageInfoDTO) {
        return false;
    }

    @Override
    public void cancelChildDo(String doCode) {
        log.info("=====取消旺店通子do单：doCode={}", doCode);
        //1.第一步校验子do是否存在 且 不是取消状态，否则抛异常
        RecordPoolE pool = recordPoolMapper.queryByDoCode(doCode);
        AlikAssert.isNotNull(pool, ResCode.ORDER_ERROR_1003, "取消子do单单号不存在：" + doCode);
        if (FrontRecordStatusEnum.DISABLED.getStatus().equals(pool.getRecordStatus())) {
            throw new RomeException(ResCode.ORDER_ERROR_5017, ResCode.ORDER_ERROR_5017_DESC + ":doCode=" + doCode);
        }

        //2、第二步校验前置单是否存在，不存在直接抛异常
        //查询前置SO单数据
        WDTSaleDO wdtSaleDO = wdtSaleMapper.selectFrSaleRecordByRecordCode(pool.getFrontRecordCode());
        AlikAssert.isNotNull(wdtSaleDO, ResCode.ORDER_ERROR_1014, ResCode.ORDER_ERROR_1014_DESC + ":doCode=" + doCode);
        WDTSaleE wdtSaleE = wdtSaleConvertor.wdtSaleDOTowdtSaleE(wdtSaleDO);

        //3、第三步 将该do单改为取消状态
        int executeResult = recordPoolMapper.updateToCanceledById(pool.getId());
        AlikAssert.isTrue(executeResult > 0, ResCode.ORDER_ERROR_1030, ResCode.ORDER_ERROR_1030_DESC + ":doCode=" + doCode);

        //4、第四步根据前置单ID查询对应的Do池数据，如果该前置单下的所有子单都取消了，则将前置单置为取消
        List<RecordPoolE> recordPoolEList = recordPoolMapper.queryNotCanceledByFrontRecordId(wdtSaleE.getRecordCode());
        if (recordPoolEList.size() < 1) {
            //小于1表示该前置单下其他子单都是取消状态,
            if (WDTRecordConst.WDT_ALLOT_OVER.equals(wdtSaleE.getAllotStatus())) {
                //并且该订单已经拆单结束了 -- 更新前置单状态为已取消
                wdtSaleMapper.updateToCanceled(wdtSaleE.getId());
            }
        }

        //如果该do单已合单



        log.info("=====取消子do单成功结束：doCode={}", doCode);
    }
}
