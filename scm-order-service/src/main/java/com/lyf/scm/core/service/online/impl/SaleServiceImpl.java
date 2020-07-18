package com.lyf.scm.core.service.online.impl;

import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.domain.convert.online.SaleConvertor;
import com.lyf.scm.core.domain.entity.online.SaleE;
import com.lyf.scm.core.mapper.online.SaleDetailMapper;
import com.lyf.scm.core.mapper.online.SaleMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.service.online.SaleService;
import com.lyf.scm.core.service.order.OrderUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("saleService")
public class SaleServiceImpl implements SaleService {

    @Resource
    private SaleMapper saleMapper;
    @Resource
    private SaleDetailMapper saleDetailMapper;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private SaleConvertor saleConvertor;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;

    @Override
    public Boolean judgeExistByOutRecordCode(String outRecordCode) {
        return saleMapper.selectFrSaleRecordByOutRecordCode(outRecordCode) != null;
    }

    @Override
    public SaleE addSaleFrontRecord(OnlineOrderDTO onlineOrder) {
        SaleE saleE = saleConvertor.orderDtoToSaleEntity(onlineOrder);
        //获取单据编号
        String code = orderUtilService.queryOrderCode(FrontRecordTypeEnum.ONLINE_SALE_RECORD.getCode());
        saleE.setRecordCode(code);
        //设置商品code或id
        itemInfoTool.convertSkuCode(saleE.getFrontRecordDetails());
        //单位转换
        skuQtyUnitTool.convertRealToBasic(saleE.getFrontRecordDetails());
        saleE.setRecordStatus(FrontRecordStatusEnum.SO_UNPAID.getStatus());
        saleE.setRecordType(FrontRecordTypeEnum.ONLINE_SALE_RECORD.getType());
        //插入前置单数据
        saleMapper.saveFrSaleRecord(saleE);
        //电商零售详情关联数据
        saleE.getFrontRecordDetails().forEach(record -> {
            record.setFrontRecordId(saleE.getId());
            record.setRecordCode(saleE.getRecordCode());
        });
        //保存电商零售前置单明细
        saleDetailMapper.saveFrSaleRecordDetails(saleE.getFrontRecordDetails());
        return saleE;
    }
}
