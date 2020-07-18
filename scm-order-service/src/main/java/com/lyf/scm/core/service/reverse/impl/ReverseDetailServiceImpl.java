package com.lyf.scm.core.service.reverse.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.reverse.ReverseRecordStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.reverse.ReverseDetailDTO;
import com.lyf.scm.core.domain.convert.reverse.ReverseConvert;
import com.lyf.scm.core.domain.convert.reverse.ReverseDetailConvert;
import com.lyf.scm.core.domain.entity.reverse.ReverseDetailE;
import com.lyf.scm.core.mapper.reverse.ReverseDetailMapper;
import com.lyf.scm.core.mapper.reverse.ReverseMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.service.reverse.ReverseDetailService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/17
 */
@Slf4j
@Service("reverseDetailService")
public class ReverseDetailServiceImpl  implements ReverseDetailService {


    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;

    @Resource
    private ReverseDetailMapper reverseDetailMapper;

    @Resource
    private ReverseDetailConvert reverseDetailConvert;


    /**
     * 批量创建冲销单明细
     * @param reverseDetailDTOList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateReverseDetail(List<ReverseDetailDTO> reverseDetailDTOList,Long userId) {
        //参数校验
        if (CollectionUtils.isEmpty(reverseDetailDTOList)) {

            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        String recordCode = reverseDetailDTOList.get(0).getRecordCode();
        List<ReverseDetailE> reverseDetailES = reverseDetailMapper.queryByRecordCode(recordCode);
        if (CollectionUtils.isEmpty(reverseDetailES)) {
            //创建
            List<ReverseDetailE> reverseDetailEList =  reverseDetailConvert.convertDTOList2EList(reverseDetailDTOList);
            reverseDetailEList.forEach(reverseDetailE -> {
                reverseDetailE.setCreator(userId);
                reverseDetailE.setModifier(userId);
                reverseDetailE.setActualQty(BigDecimal.ZERO);
            });
            reverseDetailMapper.batchInsertReverseDetail(reverseDetailEList);
        }else {
            //根据冲销单code删除已有数据
           // reverseDetailMapper.deleteReverseDetailEByRequireCode(recordCode);
          Iterator<ReverseDetailDTO> iterator =  reverseDetailDTOList.iterator();
          //循环遍历修改数据
            reverseDetailES.forEach(reverseDetailE -> {
                while (iterator.hasNext()){
                    ReverseDetailDTO reverseDetailDTO = iterator.next();
                    if (reverseDetailE.getId().equals(reverseDetailDTO.getId())){
                        reverseDetailE.setBatchRemark(reverseDetailDTO.getBatchRemark());
                        reverseDetailE.setModifier(userId);
                        reverseDetailE.setUpdateTime(new Date());
                        reverseDetailMapper.updateReverseDetail(reverseDetailE);
                        iterator.remove();
                        break;
                    }
                }
            });
        }



    }
}
